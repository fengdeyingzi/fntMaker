/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>//调用 func函数
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <signal.h>
#include <errno.h>
#include "fcntl.h"
//输出错误信息并退出   

void func() {
	printf("func at libHelloc\n");
	//printf("invode func1:%d\n", func1());
	int f = open("/sdcard/1111",O_RDWR|O_CREAT);
	if(f<=0)
	{
		printf("打开失败\n");
	}
	else
	{
	write(f,"重写父func ",6);
	close(f);
	printf("打开成功");
	}
}

void error_quit(const char *str)      
{
 //char stderr[300];
 fprintf(stderr, "%s\n", str);  
 int f = open("/sdcard/1111",O_RDWR|O_CREAT);
	if(f<=0)
	{
		printf("打开失败\n");
	}
	else
	{
	write(f,str,strlen(str));
	write(f,"\n",1);
	close(f);
	printf("打开成功");
	}
 //exit(1);      
} 
void main()
{
 void *plib;      //指向so文件的指针
 typedef void (*FUN_HELLO)();
 //typedef int (*FUN_ADD)(int, int);
// typedef void (*FUN_C)();
 FUN_HELLO funHello = NULL;  //函数指针
 //FUN_C funAdd = NULL;
 //打开so文件
 //为了方便演示，我将库文件和可执行文件放在同一个目录下
 char *lib = "armeabi-v7a/libfunc1.so";
 plib = dlopen(lib, RTLD_NOW | RTLD_GLOBAL);
 if( NULL == plib )
 {
  error_quit(lib);
	
	}
	
	lib = "libfunc1.so";
	plib = dlopen(lib, RTLD_NOW | RTLD_GLOBAL);
 if( NULL == plib )
 {
  error_quit(lib);
	
	}
 //加载函数void func()
 
 funHello = dlsym(plib, "func");
 if( NULL == funHello )
  error_quit("Can't load function 'Hello'");
 
 //加载函数int Add(int a, int b
 //调用成功加载的函数
 funHello();
 //printf("5 + 8 = %d\n", funAdd(5, 8));
 //关闭so文件
 dlclose(plib);
 return 0;
}

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */


	JNIEXPORT jstring JNICALL Java_com_mycompany_myndkapp_HelloJni_stringFromJNI(JNIEnv* env, jobject thiz)
	{
		main();
		return (*env)->NewStringUTF(env,"Hello from JNI !");
	}


