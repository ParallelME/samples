/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#ifndef SCHEDULEDTONEMAPPER_HPP
#define SCHEDULEDTONEMAPPER_HPP

#include <memory>
#include <vector>
#include <jni.h>
#include <parallelme/ParallelME.hpp>

/**
 * Class responsible for the scheduled implementation of Eric Reinhard's
 * tonemapping algorithm.
 *
 * @author Renato Utsch
 */
class ScheduledTonemapper {
    std::shared_ptr<parallelme::Runtime> _runtime;
    std::shared_ptr<parallelme::Program> _program;
    JavaVM *_jvm;

public:
    ScheduledTonemapper(JavaVM *jvm);
    ~ScheduledTonemapper();

    /**
     * Runs the Tonemapping algorithm.
     */
    void tonemap(int width, int height, float key, float power, JNIEnv *mainEnv,
            jbyteArray imageDataArray, jobject bitmap);

    /**
     * Waits for the tonemap to finish.
     */
    void waitFinish();
};

#endif // !SCHEDULEDTONEMAPPER_H_
