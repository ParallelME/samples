#include "br_ufmg_dcc_tonemapreinhard_ReinhardOpenCLOperatorCPU.h"
#include "ReinhardOpenCLOperator.h"

JNIEXPORT jlong JNICALL Java_br_ufmg_dcc_tonemapreinhard_ReinhardOpenCLOperatorCPU_init
        (JNIEnv *env, jobject a) {
    return reinhardInit(0);
}

JNIEXPORT void JNICALL Java_br_ufmg_dcc_tonemapreinhard_ReinhardOpenCLOperatorCPU_cleanUp
        (JNIEnv *env, jobject a, jlong tonemapperPtr) {
    reinhardCleanUp(tonemapperPtr);
}

JNIEXPORT void JNICALL Java_br_ufmg_dcc_tonemapreinhard_ReinhardOpenCLOperatorCPU_nativeRunOp
        (JNIEnv *env, jobject a, jlong tonemapperPtr, jint width, jint height,
         jbyteArray imageDataArray, jfloat key, jfloat power, jobject bitmap) {
    reinhardRunOp(env, tonemapperPtr, width, height, imageDataArray, key, power,
            bitmap);
}

