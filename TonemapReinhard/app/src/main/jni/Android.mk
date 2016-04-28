###                                               _    __ ____
 #   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 #  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 #  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 #  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 #
 ##

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := TonemapReinhard
LOCAL_ARM_MODE := arm
LOCAL_C_INCLUDES := $(LOCAL_PATH)/clUtils/include $(LOCAL_PATH)/runtime/include
LOCAL_CFLAGS := -O3 -Wall -Wextra -Werror -Wno-unused-parameter -Wno-extern-c-compat
LOCAL_CPPFLAGS := -O3 -std=c++14 -fexceptions
LOCAL_CPP_FEATURES += exceptions
LOCAL_LDLIBS := -llog -ldl -ljnigraphics -latomic
LOCAL_SHARED_LIBRARIES := libParallelMERuntime
LOCAL_SRC_FILES := clUtils/src/clUtils.c clUtils/src/clLoader.c \
	org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorCPU.cpp \
	org_parallelme_samples_tonemapreinhard_ReinhardOpenCLOperatorGPU.cpp \
	org_parallelme_samples_tonemapreinhard_ReinhardScheduledOperator.cpp \
	ReinhardOpenCLOperator.cpp tonemapper/Tonemapper.cpp \
	tonemapper/ScheduledTonemapper.cpp
include $(BUILD_SHARED_LIBRARY)
include $(LOCAL_PATH)/runtime/Android.mk
