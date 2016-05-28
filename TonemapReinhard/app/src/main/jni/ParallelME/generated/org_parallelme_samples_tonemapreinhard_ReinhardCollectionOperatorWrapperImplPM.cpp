/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#include "org_parallelme_samples_tonemapreinhard_ReinhardCollectionOperatorWrapperImplPM.h"

#include <memory>
#include <stdexcept>
#include <android/log.h>
#include <parallelme/ParallelME.hpp>
#include <parallelme/SchedulerHEFT.hpp>
#include "ParallelMEData.hpp"

using namespace parallelme;

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCollectionOperatorWrapperImplPM_foreach1
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

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCollectionOperatorWrapperImplPM_foreach2
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr, float sum, jfloatArray PM_sum, float max, jfloatArray PM_max) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto sumBuffer = std::make_shared<Buffer>(sizeof(sum));
	auto maxBuffer = std::make_shared<Buffer>(sizeof(max));
	auto task = std::make_unique<Task>(runtimePtr->program, Task::Score(1.0f, 2.0f));
	task->addKernel("foreach2");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach2"]
			->setArg(0, variablePtr->outputBuffer)
			->setArg(1, sum)
			->setArg(2, sumBuffer)
			->setArg(3, max)
			->setArg(4, maxBuffer)
			->setArg(5, variablePtr->width)
			->setArg(6, variablePtr->height)
			->setWorkSize(1);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
	sumBuffer->copyToJNI(env, PM_sum);
	maxBuffer->copyToJNI(env, PM_max);
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCollectionOperatorWrapperImplPM_foreach3
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr, float fScaleFactor, float fLmax2) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto task = std::make_unique<Task>(runtimePtr->program);
	task->addKernel("foreach3");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach3"]
			->setArg(0, variablePtr->outputBuffer)
			->setArg(1, fScaleFactor)
			->setArg(2, fLmax2)
			->setWorkSize(variablePtr->workSize);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCollectionOperatorWrapperImplPM_foreach4
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto task = std::make_unique<Task>(runtimePtr->program);
	task->addKernel("foreach4");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach4"]
			->setArg(0, variablePtr->outputBuffer)
			->setWorkSize(variablePtr->workSize);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCollectionOperatorWrapperImplPM_foreach5
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr, float power) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto task = std::make_unique<Task>(runtimePtr->program);
	task->addKernel("foreach5");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach5"]
			->setArg(0, variablePtr->outputBuffer)
			->setArg(1, power)
			->setWorkSize(variablePtr->workSize);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
}