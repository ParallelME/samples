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
LOCAL_MODULE := TonemapReinhard
LOCAL_C_INCLUDES := $(LOCAL_PATH)/clUtils/include $(PM_JNI_PATH)/runtime/include
LOCAL_CFLAGS := -Ofast -Wall -Wextra -Werror
LOCAL_CPPFLAGS := -Ofast -Wall -Wextra -Werror -Wno-unused-parameter -std=c++14 -fexceptions
LOCAL_CPP_FEATURES += exceptions
LOCAL_LDLIBS := -llog -ldl -ljnigraphics
LOCAL_SHARED_LIBRARIES := ParallelMERuntime
LOCAL_SRC_FILES := clUtils/src/clUtils.c clUtils/src/clLoader.c \
	org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorCPU.cpp \
	org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorGPU.cpp \
	org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator.cpp \
	ReinhardOpenCLOperator.cpp tonemapper/Tonemapper.cpp \
	tonemapper/ScheduledTonemapper.cpp
include $(BUILD_SHARED_LIBRARY)
include $(wildcard $(PM_JNI_PATH)/**/Android.mk)
