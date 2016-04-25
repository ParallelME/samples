#include "org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorGPU.h"
#include "ReinhardOpenCLOperator.h"

JNIEXPORT jlong JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorGPU_init
        (JNIEnv *env, jobject a) {
    return reinhardInit(1);
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorGPU_cleanUp
        (JNIEnv *env, jobject a, jlong tonemapperPtr) {
    reinhardCleanUp(tonemapperPtr);
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorGPU_nativeRunOp
        (JNIEnv *env, jobject a, jlong tonemapperPtr, jint width, jint height,
         jbyteArray imageDataArray, jfloat key, jfloat power, jobject bitmap) {
    reinhardRunOp(env, tonemapperPtr, width, height, imageDataArray, key, power,
            bitmap);
}

