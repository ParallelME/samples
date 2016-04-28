/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#include "org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator.h"
#include "tonemapper/ScheduledTonemapper.hpp"
#include "error.h"
#include <stdexcept>

#include <android/bitmap.h>

JavaVM *gJvm;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    gJvm = jvm; // Cache the JVM.
    return JNI_VERSION_1_6;
}

JNIEXPORT jlong JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator_init
        (JNIEnv *env, jobject a, jobject ctx, jstring scriptcName) {
    try {
        auto tonemapper = new ScheduledTonemapper(gJvm, ctx, scriptcName);
        return reinterpret_cast<jlong>(tonemapper);
    }
    catch(std::runtime_error &e) {
        stop_if(true, "Error when initializing runtime: %s", e.what());
    }
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator_cleanUp
        (JNIEnv *env, jobject a, jlong tonemapperPtr) {
    auto tonemapper = reinterpret_cast<ScheduledTonemapper *>(tonemapperPtr);
    delete tonemapper;
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator_nativeRunOp
        (JNIEnv *env, jobject a, jlong tonemapperPtr, jint width, jint height,
         jbyteArray imageDataArray, jfloat key, jfloat power, jobject bitmap) {
    auto tonemapper = reinterpret_cast<ScheduledTonemapper *>(tonemapperPtr);

    try {
        tonemapper->tonemap(width, height, key, power, env, imageDataArray, bitmap);
    }
    catch(std::runtime_error &e) {
        stop_if(true, "Error when running tonemap: %s", e.what());
    }
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator_nativeWaitFinish
  (JNIEnv *env, jobject a, jlong tonemapperPtr) {
    auto tonemapper = reinterpret_cast<ScheduledTonemapper *>(tonemapperPtr);
    tonemapper->waitFinish();
}
