/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#include <jni.h>

#ifndef _Included_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM
#define _Included_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach1
		(JNIEnv *, jobject , jlong , jlong );

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_reduce2
		(JNIEnv *, jobject , jlong , jlong , jfloatArray);

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach3
		(JNIEnv *, jobject , jlong , jlong , float, float);

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach4
		(JNIEnv *, jobject , jlong , jlong );

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach5
		(JNIEnv *, jobject , jlong , jlong , float);

#ifdef __cplusplus
}
#endif
#endif
