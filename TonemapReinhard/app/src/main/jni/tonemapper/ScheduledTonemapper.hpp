#ifndef SCHEDULEDTONEMAPPER_HPP
#define SCHEDULEDTONEMAPPER_HPP

#include <memory>
#include <vector>
#include <jni.h>

class Runtime;
class Program;
class Task;
class Buffer;

/**
 * Class responsible for the scheduled implementation of Eric Reinhard's
 * tonemapping algorithm.
 */
class ScheduledTonemapper {
    std::shared_ptr<Runtime> _runtime;
    std::vector< std::shared_ptr<Program> > _programs;
    JavaVM *_jvm;

public:
    ScheduledTonemapper(JavaVM *jvm, jobject androidContext, jstring scriptcName);
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
