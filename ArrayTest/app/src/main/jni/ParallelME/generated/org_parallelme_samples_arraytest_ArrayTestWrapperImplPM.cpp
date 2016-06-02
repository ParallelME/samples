/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#include "org_parallelme_samples_arraytest_ArrayTestWrapperImplPM.h"

#include <memory>
#include <stdexcept>
#include <android/log.h>
#include <parallelme/ParallelME.hpp>
#include <parallelme/SchedulerHEFT.hpp>
#include "ParallelMEData.hpp"

using namespace parallelme;

JNIEXPORT void JNICALL Java_org_parallelme_samples_arraytest_ArrayTestWrapperImplPM_foreach1
		(JNIEnv *env, jobject self, jlong rtmPtr, jlong varPtr, int varTeste, jintArray PM_varTeste) {
	auto runtimePtr = (ParallelMERuntimeData *) rtmPtr;
	auto variablePtr = (ArrayData *) varPtr;
	auto varTesteBuffer = std::make_shared<Buffer>(sizeof(varTeste));
	auto task = std::make_unique<Task>(runtimePtr->program, Task::Score(1.0f, 2.0f));
	task->addKernel("foreach1");
	task->setConfigFunction([=](DevicePtr &device, KernelHash &kernelHash) {
		kernelHash["foreach1"]
			->setArg(0, variablePtr->buffer)
			->setArg(1, varTeste)
			->setArg(2, varTesteBuffer)
			->setArg(3, variablePtr->length)
			->setWorkSize(1);
	});
	runtimePtr->runtime->submitTask(std::move(task));
	runtimePtr->runtime->finish();
	varTesteBuffer->copyToJArray(env, PM_varTeste);
}
