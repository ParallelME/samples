/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#include "org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM.h"

#include <memory>
#include <stdexcept>
#include <android/log.h>
#include <parallelme/ParallelME.hpp>
#include <parallelme/SchedulerHEFT.hpp>
#include "ParallelMEData.hpp"

using namespace parallelme;

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach1
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

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_reduce2
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr, jfloatArray red) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ImageData *) varPtr;
	auto task = std::make_unique<Task>(runtimePtr->program);
    auto tileElemSize = Buffer::sizeGenerator(1, Buffer::FLOAT4);
	auto tileBuffer = std::make_shared<Buffer>(tileElemSize * variablePtr->height);
    auto redBuffer = std::make_shared<Buffer>(tileElemSize);
	task->addKernel("reduce2_tile");
	task->addKernel("reduce2");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["reduce2_tile"]
			->setArg(0, variablePtr->outputBuffer)
			->setArg(1, tileBuffer)
			->setArg(2, variablePtr->width)
			->setWorkSize(variablePtr->height);
		kernelHash["reduce2"]
			->setArg(0, tileBuffer)
            ->setArg(1, redBuffer)
            ->setArg(2, variablePtr->height)
			->setWorkSize(1);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
	redBuffer->copyToJArray(env, red);
}

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach3
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

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach4
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

JNIEXPORT void JNICALL Java_org_parallelme_samples_tonemapreinhard_ReinhardCompilerOperatorWrapperImplPM_foreach5
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
