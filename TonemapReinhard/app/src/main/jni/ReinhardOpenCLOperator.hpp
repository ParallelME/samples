/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#ifndef REINHARD_OPENCL_OPERATOR_HPP
#define REINHARD_OPENCL_OPERATOR_HPP

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

#endif // !REINHARD_OPENCL_OPERATOR_HPP
