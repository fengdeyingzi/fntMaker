LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_ARM_MODE  := arm
LOCAL_C_INCLUDES := $(LOCAL_PATH)../ 
LOCAL_C_INCLUDES += $(LOCAL_PATH)/
LOCAL_MODULE    := hello
# LOCAL_SHARED_LIBRARIES   := libzip.so

LOCAL_SRC_FILES := hello-jni.c
#LOCAL_SRC_FILES += register_natives.c
LOCAL_LDLIBS    += -llog -lm
LOCAL_CFLAGS += -I~/android-2.0/system/core/include 
include $(BUILD_SHARED_LIBRARY)

