/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#include "Tonemapper.hpp"
#include "kernels.h"
#include "../error.h"

#include <clLoader.h>
#include <clUtils.h>
#include <stdexcept>
#include <cstdlib>
#include <cstring>

struct Tonemapper::CLData {
    cl_platform_id platform;
    cl_device_id device;
    cl_context context;
    cl_command_queue queue;
    cl_program program;

    cl_mem imageBuffer;
    cl_mem dataBuffer;

    cl_kernel toFloatKernel;
    cl_kernel toYxyKernel;
    cl_kernel lineLogAverageKernel;
    cl_kernel logAverageKernel;
    cl_kernel tonemapKernel;
    cl_kernel toRgbKernel;
    cl_kernel toBitmapKernel;
};

Tonemapper::Tonemapper(int useGPU) {
    int err;

    clData = new CLData();
    stop_if(!clData, "failed to construct the Tonemapper internal data.");

    err = clGetPlatformIDs(1, &clData->platform, NULL);
    stop_if(err < 0, "failed to find an OpenCL platform.");

    err = clGetDeviceIDs(clData->platform,
            useGPU ? CL_DEVICE_TYPE_GPU : CL_DEVICE_TYPE_CPU,
            1, &clData->device, NULL);
    stop_if(err < 0, "failed to find an OpenCL device.");

    clData->context = clCreateContext(NULL, 1, &clData->device, NULL, NULL, &err);
    stop_if(err < 0, "failed to create an OpenCL context.");

    clData->queue = clCreateCommandQueue(clData->context, clData->device, 0, &err);
    stop_if(err < 0, "failed to create an OpenCL command queue.");

    clData->program = cluBuildProgram(clData->context,
            clData->device, tonemapSource, strlen(tonemapSource) + 1,
            "-Werror -cl-strict-aliasing -cl-mad-enable -cl-no-signed-zeros "
            "-cl-finite-math-only", &err);
            // "-cl-unsafe-math-optimizations -cl-fast-relaxed-math" crashing (Qualcomm).
    stop_if(err < 0, "failed to compile the OpenCL program.");

    clData->toFloatKernel = clCreateKernel(clData->program, "to_float", &err);
    stop_if(err < 0, "failed to create the toFloat OpenCL kernel.");

    clData->toYxyKernel = clCreateKernel(clData->program, "to_yxy", &err);
    stop_if(err < 0, "failed to create the toYxy OpenCL kernel.");

    clData->lineLogAverageKernel = clCreateKernel(clData->program,
            "line_log_average", &err);
    stop_if(err < 0, "failed to create the lineLogAverage OpenCL kernel.");

    clData->logAverageKernel = clCreateKernel(clData->program, "log_average", &err);
    stop_if(err < 0, "failed to create the logAverage OpenCL kernel.");

    clData->tonemapKernel = clCreateKernel(clData->program, "tonemap", &err);
    stop_if(err < 0, "failed to create the tonemap OpenCL kernel.");

    clData->toRgbKernel = clCreateKernel(clData->program, "to_rgb", &err);
    stop_if(err < 0, "failed to create the toRgb OpenCL kernel.");

    clData->toBitmapKernel = clCreateKernel(clData->program, "to_bitmap", &err);
    stop_if(err < 0, "failed to create the toBitmap OpenCL kernel.");
}

Tonemapper::~Tonemapper() {
    clReleaseKernel(clData->toFloatKernel);
    clReleaseKernel(clData->toYxyKernel);
    clReleaseKernel(clData->lineLogAverageKernel);
    clReleaseKernel(clData->logAverageKernel);
    clReleaseKernel(clData->tonemapKernel);
    clReleaseKernel(clData->toRgbKernel);
    clReleaseKernel(clData->toBitmapKernel);
    clReleaseCommandQueue(clData->queue);
    clReleaseProgram(clData->program);
    clReleaseContext(clData->context);

    delete clData;
}

void Tonemapper::runToFloat(int workDims, size_t *offset, size_t *workSize) {
    int err;

    err = clSetKernelArg(clData->toFloatKernel, 0, sizeof(clData->imageBuffer),
            &clData->imageBuffer);
    stop_if(err < 0, "failed to set first toFloat kernel argument.");

    err = clSetKernelArg(clData->toFloatKernel, 1, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set second toFloat kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->toFloatKernel, workDims,
            offset, workSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the toFloat kernel for execution.");
}

void Tonemapper::runToYxy(int workDims, size_t *offset, size_t *workSize) {
    int err;

    err = clSetKernelArg(clData->toYxyKernel, 0, sizeof(clData->dataBuffer),
        &clData->dataBuffer);
    stop_if(err < 0, "failed to set the first toYxy kernel argument.");

    err = clSetKernelArg(clData->toYxyKernel, 1, sizeof(clData->dataBuffer),
        &clData->dataBuffer);
    stop_if(err < 0, "failed to set the second toYxy kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->toYxyKernel, workDims,
            offset, workSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the toYxy kernel for execution.");
}

void Tonemapper::runLogAverage(int width, int height, float key) {
    int err;
    size_t offset[] = {0, 0};
    size_t lineWorkSize[] = {1, (size_t) height};
    size_t finalWorkSize[] = {1, 1};

    err = clSetKernelArg(clData->lineLogAverageKernel, 0,
            sizeof(clData->dataBuffer), &clData->dataBuffer);
    stop_if(err < 0, "failed to set the first lineLogAverage kernel argument.");

    err = clSetKernelArg(clData->lineLogAverageKernel, 1,
            sizeof(clData->dataBuffer), &clData->dataBuffer);
    stop_if(err < 0, "failed to set the second lineLogAverage kernel argument.");

    err = clSetKernelArg(clData->lineLogAverageKernel, 2,
            sizeof(clData->dataBuffer), &clData->dataBuffer);
    stop_if(err < 0, "failed to set the third lineLogAverage kernel argument.");

    err = clSetKernelArg(clData->lineLogAverageKernel, 3, sizeof(width), &width);
    stop_if(err < 0, "failed to set the fourth lineLogAverage kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->lineLogAverageKernel, 2,
            offset, lineWorkSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the lineLogAverage kernel for execution.");

    err = clSetKernelArg(clData->logAverageKernel, 0, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set the first logAverage kernel argument.");

    err = clSetKernelArg(clData->logAverageKernel, 1, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set the second logAverage kernel argument.");

    err = clSetKernelArg(clData->logAverageKernel, 2, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set the third logAverage kernel argument.");

    err = clSetKernelArg(clData->logAverageKernel, 3, sizeof(width), &width);
    stop_if(err < 0, "failed to set the fourth logAverage kernel argument.");

    err = clSetKernelArg(clData->logAverageKernel, 4, sizeof(height), &height);
    stop_if(err < 0, "failed to set the fifth logAverage kernel argument.");

    err = clSetKernelArg(clData->logAverageKernel, 5, sizeof(key), &key);
    stop_if(err < 0, "failed to set the sixth logAverage kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->logAverageKernel, 2,
            offset, finalWorkSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the logAverage kernel for execution.");
}

void Tonemapper::runTonemap(int workDims, size_t *offset, size_t *workSize) {
    int err;

    err = clSetKernelArg(clData->tonemapKernel, 0, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set the first tonemap kernel argument.");

    err = clSetKernelArg(clData->tonemapKernel, 1, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set the second tonemap kernel argument.");

    err = clSetKernelArg(clData->tonemapKernel, 2, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set the third tonemap kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->tonemapKernel,
            workDims, offset, workSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the tonemap kernel for execution.");
}

void Tonemapper::runToRgb(int workDims, size_t *offset, size_t *workSize) {
    int err;

    err = clSetKernelArg(clData->toRgbKernel, 0, sizeof(clData->dataBuffer),
        &clData->dataBuffer);
    stop_if(err < 0, "failed to set the first toRgb kernel argument.");

    err = clSetKernelArg(clData->toRgbKernel, 1, sizeof(clData->dataBuffer),
        &clData->dataBuffer);
    stop_if(err < 0, "failed to set the second toRgb kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->toRgbKernel, workDims,
            offset, workSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the toRgb kernel for execution.");
}

void Tonemapper::runToBitmap(int workDims, size_t *offset, size_t *workSize,
        float power) {
    int err;

    err = clSetKernelArg(clData->toBitmapKernel, 0, sizeof(clData->dataBuffer),
            &clData->dataBuffer);
    stop_if(err < 0, "failed to set first toBitmap kernel argument.");

    err = clSetKernelArg(clData->toBitmapKernel, 1, sizeof(clData->imageBuffer),
            &clData->imageBuffer);
    stop_if(err < 0, "failed to set second toBitmap kernel argument.");

    err = clSetKernelArg(clData->toBitmapKernel, 2, sizeof(power), &power);
    stop_if(err < 0, "failed to set third toBitmap kernel argument.");

    err = clEnqueueNDRangeKernel(clData->queue, clData->toBitmapKernel, workDims,
            offset, workSize, NULL, 0, NULL, NULL);
    stop_if(err < 0, "failed to enqueue the toBitmap kernel for  execution. Error %d.", err);
}

void Tonemapper::tonemap(int width, int height, float key, float power,
        unsigned char *input, unsigned char *output) {
    int err;
    size_t numElems = width * height * 4;
    size_t imageBufferSize = numElems * sizeof(*input);
    size_t dataBufferSize = numElems * sizeof(float);

    clData->imageBuffer = clCreateBuffer(clData->context, CL_MEM_READ_WRITE,
            imageBufferSize, NULL, &err);
    stop_if(err < 0, "failed to create the image buffer.");

    unsigned char *inData = (unsigned char *) clEnqueueMapBuffer(clData->queue,
            clData->imageBuffer, CL_TRUE, CL_MAP_WRITE, 0, imageBufferSize, 0,
            NULL, NULL, &err);
    stop_if(err < 0, "failed to map input data.");

    memcpy(inData, input, imageBufferSize);

    clEnqueueUnmapMemObject(clData->queue, clData->imageBuffer, inData, 0, NULL,
            NULL);

    clData->dataBuffer = clCreateBuffer(clData->context, CL_MEM_READ_WRITE,
            dataBufferSize, NULL, &err);
    stop_if(err < 0, "failed to create the data buffer.");

    size_t offset[] = { 0, 0 };
    size_t workSize[] = { (size_t) width, (size_t) height };
    runToFloat(2, offset, workSize);
    runToYxy(2, offset, workSize);
    runLogAverage(width, height, key);
    runTonemap(2, offset, workSize);
    runToRgb(2, offset, workSize);
    runToBitmap(2, offset, workSize, power);

    err = clFinish(clData->queue);
    stop_if(err < 0, "failed to finish command queue.");

    unsigned char *outData = (unsigned char *) clEnqueueMapBuffer(clData->queue,
            clData->imageBuffer, CL_TRUE, CL_MAP_READ, 0, imageBufferSize, 0,
            NULL, NULL, &err);
    stop_if(err < 0, "failed to map output data.");

    memcpy(output, outData, imageBufferSize);

    clEnqueueUnmapMemObject(clData->queue, clData->imageBuffer, outData, 0, NULL,
            NULL);

    clFinish(clData->queue);
    stop_if(err < 0, "failed to finish command queue.");

    clReleaseMemObject(clData->imageBuffer);
    clReleaseMemObject(clData->dataBuffer);
}

