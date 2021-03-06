/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#include "org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorCPU.h"
#include "ReinhardOpenCLOperator.hpp"

JNIEXPORT jlong JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorCPU_init
        (JNIEnv *env, jobject a) {
    return reinhardInit(0);
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorCPU_cleanUp
        (JNIEnv *env, jobject a, jlong tonemapperPtr) {
    reinhardCleanUp(tonemapperPtr);
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorCPU_nativeRunOp
        (JNIEnv *env, jobject a, jlong tonemapperPtr, jint width, jint height,
         jbyteArray imageDataArray, jfloat key, jfloat power, jobject bitmap) {
    reinhardRunOp(env, tonemapperPtr, width, height, imageDataArray, key, power,
            bitmap);
}

