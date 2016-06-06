/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#include "org_parallelme_samples_bitmaptest_BitmapTestWrapperImplPM.h"

#include <memory>
#include <stdexcept>
#include <android/log.h>
#include <parallelme/ParallelME.hpp>
#include <parallelme/SchedulerHEFT.hpp>
#include "ParallelMEData.hpp"

using namespace parallelme;

JNIEXPORT void JNICALL Java_org_parallelme_samples_bitmaptest_BitmapTestWrapperImplPM_foreach1
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto task = std::make_unique<Task>(runtimePtr->program);
	task->addKernel("foreach1");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach1"]
			->setArg(0, variablePtr->outputBuffer)
			->setWorkSize(variablePtr->workSize);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_bitmaptest_BitmapTestWrapperImplPM_foreach2
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto task = std::make_unique<Task>(runtimePtr->program);
	task->addKernel("foreach2");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach2"]
			->setArg(0, variablePtr->outputBuffer)
			->setWorkSize(variablePtr->workSize);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
}
