/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#include "ReinhardOpenCLOperator.h"
#include "tonemapper/Tonemapper.hpp"
#include "error.h"

#include <clLoader.h>
#include <android/bitmap.h>

jlong reinhardInit(int useGPU) {
    Tonemapper *tonemapper;

    if(!loadOpenCL()) {
        printError("failed to load OpenCL.");
        return 0;
    }

    tonemapper = new Tonemapper(useGPU);
    return reinterpret_cast<jlong>(tonemapper);
}

void reinhardCleanUp(jlong tonemapperPtr) {
    Tonemapper *tonemapper = reinterpret_cast<Tonemapper *>(tonemapperPtr);

    closeOpenCL();
    delete tonemapper;
}

void reinhardRunOp(JNIEnv *env, jlong tonemapperPtr, jint width, jint height,
        jbyteArray imageDataArray, jfloat key, jfloat gamma, jobject bitmap) {
    Tonemapper *tonemapper = reinterpret_cast<Tonemapper *>(tonemapperPtr);
    unsigned char *imageData;
    unsigned char *bitmapData;

    imageData = (unsigned char *) env->GetByteArrayElements(imageDataArray, NULL);
    stop_if(!imageData, "failed to lock input image for reading.");

    int ret = AndroidBitmap_lockPixels(env, bitmap, (void **) &bitmapData);
    stop_if(ret < 0, "failed to lock bitmap for writing.");

    tonemapper->tonemap(width, height, key, gamma, imageData, bitmapData);

    bitmapData = NULL;
    AndroidBitmap_unlockPixels(env, bitmap);

    env->ReleaseByteArrayElements(imageDataArray, (signed char *) imageData, 0);
}
