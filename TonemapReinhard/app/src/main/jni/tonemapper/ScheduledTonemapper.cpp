/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#include <memory>
#include <jni.h>
#include "ScheduledTonemapper.hpp"
#include <parallelme/ParallelME.hpp>
#include "kernels.h"
using namespace parallelme;

ScheduledTonemapper::ScheduledTonemapper(JavaVM *jvm) : _jvm(jvm) {
    _runtime = std::make_shared<Runtime>(_jvm);
    _program = std::make_shared<Program>(_runtime, tonemapSource,
            "-Werror -cl-strict-aliasing -cl-mad-enable -cl-no-signed-zeros "
            "-cl-finite-math-only");
}

ScheduledTonemapper::~ScheduledTonemapper() = default;

void ScheduledTonemapper::tonemap(int width, int height, float key, float power,
        JNIEnv *env, jbyteArray imageDataArray, jobject bitmap) {
    size_t workSize = width * height;
    size_t imageSize = workSize * 4; // RGBA.

    // Get references to the imageDataArray and bitmap.
    auto outputBitmap = env->NewGlobalRef(bitmap);
    auto imageBuffer = std::make_shared<Buffer>(imageSize);
    imageBuffer->copyFromJNI(env, imageDataArray);
    auto dataBuffer = std::make_shared<Buffer>(imageSize * sizeof(float));

    auto task = std::make_unique<Task>(_program);
    task->addKernel("to_float")
        ->addKernel("to_yxy")
        ->addKernel("line_log_average")
        ->addKernel("log_average")
        ->addKernel("tonemap")
        ->addKernel("to_rgb")
        ->addKernel("to_bitmap");

    task->setConfigFunction([=] (DevicePtr &device, KernelHash &kernelHash) {
        kernelHash["to_float"]
            ->setArg(0, imageBuffer)
            ->setArg(1, dataBuffer)
            ->setWorkSize(workSize);

        kernelHash["to_yxy"]
            ->setArg(0, dataBuffer)
            ->setWorkSize(workSize);

        kernelHash["line_log_average"]
            ->setArg(0, dataBuffer)
            ->setArg(1, width)
            ->setWorkSize(height);

        kernelHash["log_average"]
            ->setArg(0, dataBuffer)
            ->setArg(1, width)
            ->setArg(2, height)
            ->setArg(3, key)
            ->setWorkSize(1);

        kernelHash["tonemap"]
            ->setArg(0, dataBuffer)
            ->setWorkSize(workSize);

        kernelHash["to_rgb"]
            ->setArg(0, dataBuffer)
            ->setWorkSize(workSize);

        kernelHash["to_bitmap"]
            ->setArg(0, dataBuffer)
            ->setArg(1, imageBuffer)
            ->setArg(2, power)
            ->setWorkSize(workSize);
    });

    task->setFinishFunction([=] (DevicePtr &device, KernelHash &kernelHash) {
        imageBuffer->copyToJNI(device->JNIEnv(), outputBitmap);
        device->JNIEnv()->DeleteGlobalRef(outputBitmap);
    });

    _runtime->submitTask(std::move(task));
}

void ScheduledTonemapper::waitFinish() {
    _runtime->finish();
}
