#include <parallelme/ParallelME.hpp>
#include "org_parallelme_samples_grayscale_GrayscaleOperator.h"
using namespace parallelme;

const static char gKernels[] =
    "__kernel void grayscale(__global uchar4 *image) { \n"
    "    int gid = get_global_id(0);                   \n"
    "    uchar4 pixel = image[gid];                    \n"
    "                                                  \n"
    "    uchar luminosity = 0.21f * pixel.x            \n"
    "        + 0.72f * pixel.y + 0.07f * pixel.z;      \n"
    "    pixel.x = pixel.y = pixel.z = luminosity;     \n"
    "                                                  \n"
    "    image[gid] = pixel;                           \n"
    "}                                                 \n";


struct NativeData {
    std::shared_ptr<Runtime> runtime;
    std::shared_ptr<Program> program;
};

JNIEXPORT jlong JNICALL Java_org_parallelme_samples_grayscale_GrayscaleOperator_nativeInit
        (JNIEnv *env, jobject self) {
    JavaVM *jvm;
    env->GetJavaVM(&jvm);
    if(!jvm) return (jlong) nullptr;

    auto dataPointer = new NativeData();
    dataPointer->runtime = std::make_shared<Runtime>(jvm);
    dataPointer->program = std::make_shared<Program>(dataPointer->runtime, gKernels);

    return (jlong) dataPointer;
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_grayscale_GrayscaleOperator_nativeCleanUp
        (JNIEnv *env, jobject self, jlong dataLong) {
    auto dataPointer = (NativeData *) dataLong;
    delete dataPointer;
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_grayscale_GrayscaleOperator_nativeGrayscale
        (JNIEnv *env, jobject self, jlong dataLong, jobject bitmap, jint width, jint height) {
    auto dataPointer = (NativeData *) dataLong;
    auto imageSize = width * height;
    auto bitmapBuffer = std::make_shared<Buffer>(Buffer::sizeGenerator(imageSize, Buffer::RGBA));
    bitmapBuffer->setAndroidBitmapSource(env, bitmap);

    auto task = std::make_unique<Task>(dataPointer->program);
    task->addKernel("grayscale");
    task->setConfigFunction([=] (DevicePtr &device, KernelHash &kernelHash) {
        kernelHash["grayscale"]
            ->setArg(0, bitmapBuffer)
            ->setWorkSize(imageSize);
    });

    dataPointer->runtime->submitTask(std::move(task));
    dataPointer->runtime->finish();

    bitmapBuffer->copyToAndroidBitmap(env, bitmap);
}
