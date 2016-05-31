###                                               _    __ ____
 #   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 #  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 #  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 #  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 #
 ##

PM_JNI_PATH := $(call my-dir)/ParallelME
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := Grayscale
LOCAL_ARM_MODE := arm
LOCAL_C_INCLUDES := $(LOCAL_PATH)/ParallelME/runtime/include
LOCAL_CPPFLAGS := -O3 -std=c++14
LOCAL_LDLIBS := -ljnigraphics
LOCAL_SHARED_LIBRARIES := libParallelMERuntime
LOCAL_SRC_FILES := org_parallelme_samples_grayscale_GrayscaleOperator.cpp

include $(BUILD_SHARED_LIBRARY)
include $(PM_JNI_PATH)/Android.mk
