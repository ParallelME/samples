#include "ScheduledTonemapper.hpp"
#include <Runtime.hpp>
#include <cl/CLProgram.hpp>
#include <rs/RSProgram.hpp>
#include "kernels.h"
#include <android/bitmap.h>
#include <memory>
#include <jni.h>
#include <sched/SchedulerHEFT.hpp>
#include <sched/SchedulerPAMS.hpp>

ScheduledTonemapper::ScheduledTonemapper(JavaVM *jvm, jobject androidContext,
        jstring scriptcName)
        : _jvm(jvm) {
    _runtime = std::make_shared<Runtime>(_jvm, androidContext);
    _programs.push_back(std::make_shared<CLProgram>(_runtime, tonemapSource,
            "-Werror -cl-strict-aliasing -cl-mad-enable -cl-no-signed-zeros "
            "-cl-finite-math-only"));
    _programs.push_back(std::make_shared<RSProgram>(_runtime, scriptcName));
}

ScheduledTonemapper::~ScheduledTonemapper() = default;

void ScheduledTonemapper::tonemap(int width, int height, float key, float power,
        JNIEnv *mainEnv, jbyteArray imageDataArray, jobject bitmap) {
    // Get references to the imageDataArray and bitmap.
    auto inputArray = reinterpret_cast<jbyteArray>(mainEnv->NewGlobalRef(imageDataArray));
    auto outputBitmap = mainEnv->NewGlobalRef(bitmap);

    auto task = std::make_unique<Task>(_programs);
    task->addKernel("to_float")
        ->addKernel("to_yxy")
        ->addKernel("line_log_average")
        ->addKernel("log_average")
        ->addKernel("tonemap")
        ->addKernel("to_rgb")
        ->addKernel("to_bitmap");

    task->setConfigFunction([=] (Device &device, KernelHash &kernelHash) {
        auto imageBuffer = device.createBuffer(Buffer::RGBA, width, height);
        imageBuffer->copyFrom(inputArray);
        device.jenv()->DeleteGlobalRef(inputArray);

        auto dataBuffer = device.createBuffer(Buffer::FLOAT4, width, height);

        kernelHash["to_float"]
            ->setInputBuffer(imageBuffer)
            ->setOutputBuffer(dataBuffer);

        kernelHash["to_yxy"]
            ->setInputBuffer(dataBuffer)
            ->setOutputBuffer(dataBuffer);

        kernelHash["line_log_average"]
            ->setInputBuffer(dataBuffer)
            ->setOutputBuffer(dataBuffer)
            ->setArg("lineLogAverageData", dataBuffer)
            ->setArg("lineLogAverageWidth", width)
            ->setWorkRange(1, height);

        kernelHash["log_average"]
            ->setInputBuffer(dataBuffer)
            ->setOutputBuffer(dataBuffer)
            ->setArg("logAverageData", dataBuffer)
            ->setArg("logAverageWidth", width)
            ->setArg("logAverageHeight", height)
            ->setArg("logAverageKey", key)
            ->setWorkRange(1, 1);

        kernelHash["tonemap"]
            ->setInputBuffer(dataBuffer)
            ->setOutputBuffer(dataBuffer)
            ->setArg("tonemapData", dataBuffer);

        kernelHash["to_rgb"]
            ->setInputBuffer(dataBuffer)
            ->setOutputBuffer(dataBuffer);

        kernelHash["to_bitmap"]
            ->setInputBuffer(dataBuffer)
            ->setOutputBuffer(imageBuffer)
            ->setArg("toBitmapPower", power);
    });

    task->setFinishFunction([=] (Device &device, KernelHash &kernelHash) {
        kernelHash["to_bitmap"]->outputBuffer()->copyTo(outputBitmap);
        device.jenv()->DeleteGlobalRef(outputBitmap);
    });

    _runtime->submitTask(std::move(task));
}

void ScheduledTonemapper::waitFinish() {
    _runtime->finish();
}
