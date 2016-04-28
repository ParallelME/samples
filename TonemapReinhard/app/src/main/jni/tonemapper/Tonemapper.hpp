/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#ifndef TONEMAPPER_HPP
#define TONEMAPPER_HPP

#include <cstdlib>

/**
 * Class responsible for running Eric Reinhard's tonemapping algorithm.
 */
class Tonemapper {
    struct CLData;
    CLData *clData;

    /// Runs the toFloat kernel.
    void runToFloat(int workDims, size_t *offset, size_t *workSize);

    /// Runs the toYxy kernel.
    void runToYxy(int workDims, size_t *offset, size_t *workSize);

    /// Runs the lineLogAverage and  logAverage kernels.
    void runLogAverage(int width, int height, float key);

    /// Runs the tonemap kernel.
    void runTonemap(int workDims, size_t *offset, size_t *workSize);

    /// Runs the toRgb kernel.
    void runToRgb(int workDims, size_t *offset, size_t *workSize);

    /// Runs the toBitmap kernel.
    void runToBitmap(int workDims, size_t *offset, size_t *workSize, float power);

public:
    Tonemapper(int useGPU);
    ~Tonemapper();

    /**
     * Runs the Tonemapping algorithm.
     */
    void tonemap(int width, int height, float key, float power,
            unsigned char *input, unsigned char *output);
};

#endif // !TONEMAPPER_HPP
