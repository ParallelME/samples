#ifndef REINHARD_OPENCL_OPERATOR_H
#define REINHARD_OPENCL_OPERATOR_H

#include <jni.h>

// This file is intended to abstract the different OpenCL Operator
// class options.

/// Returns the value of the pointer to the tonemapper or 0 in case of failure.
jlong reinhardInit(int useGPU);

/// Assumes tonemapperPtr is valid.
void reinhardCleanUp(jlong tonemapperPtr);

/// Runs the tonemap operations. Assumes tonemapperPtr is valid.
void reinhardRunOp(JNIEnv *env, jlong tonemapperPtr, jint width, jint height,
        jbyteArray imageDataArray, jfloat key, jfloat gamma, jobject bitmap);

#endif // !REINHARD_OPENCL_OPERATOR_H
