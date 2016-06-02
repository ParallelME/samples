###                                               _    __ ____
 #   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 #  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 #  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 #  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 #
 # Code created automatically by ParallelME compiler.
 ##

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ParallelMEGenerated
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../runtime/include
LOCAL_CPPFLAGS := -Ofast -Wall -Wextra -Werror -Wno-unused-parameter -std=c++14 -fexceptions
LOCAL_CPP_FEATURES += exceptions
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := ParallelMERuntime
LOCAL_SRC_FILES := org_parallelme_ParallelMERuntime.cpp \
	org_parallelme_samples_arraytest_ArrayTestWrapperImplPM.cpp
include $(BUILD_SHARED_LIBRARY)
