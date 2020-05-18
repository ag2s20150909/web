//
// Created by asus on 2017/11/17.
//
#include <jni.h>
#include<android/bitmap.h>
#include <android/log.h>
#define LIBNAME "native-lib"
#ifndef eprintf
#define eprintf(...) __android_log_print(ANDROID_LOG_ERROR,LIBNAME,__VA_ARGS__)
#endif

#define RGBA_A(p) (((p) & 0xFF000000) >> 24)
#define RGBA_R(p) (((p) & 0x00FF0000) >> 16)
#define RGBA_G(p) (((p) & 0x0000FF00) >>  8)
#define RGBA_B(p)  ((p) & 0x000000FF)
#define MAKE_RGBA(r,g,b,a) (((a) << 24) | ((r) << 16) | ((g) << 8) | (b))

extern "C" {


JNIEXPORT jboolean
JNICALL Java_cn_liuyin_ndk_MyNdkTool_toHeiBai(JNIEnv *env, jclass clazz,//保留的
                                              jobject bmpObj) {
    AndroidBitmapInfo bmpInfo;
    if(ANDROID_BITMAP_RESULT_SUCCESS!=AndroidBitmap_getInfo(env,bmpObj,&bmpInfo))
    {
        eprintf("get bitmap info failed");
        return (jboolean) false;
    }

    //锁定当前bitmap,使其在AndroidBitmap_unlockPixels(env,bmpObj);之前不被其它程序更改
    int* dataFromBmp=NULL;
    if(AndroidBitmap_lockPixels(env,bmpObj,(void**)&dataFromBmp))
    {
        return (jboolean) false;
    }
    int w=bmpInfo.width;//bitmap的宽
    int h=bmpInfo.height;//bitmap 的高
    int32_t *bmpPixs= (int32_t *) bmpObj;
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j <w ; ++j) {
            int color=bmpPixs[i*w+j];//i j 位置的像素
            int a=RGBA_A(color);
            int r=RGBA_R(color);
            int g=RGBA_G(color);
            int b=RGBA_B(color);
            //对rgb进行操作。。。。
            color=MAKE_RGBA(r,g,b,a);
            bmpPixs[i*w+j]=color;

        }
    }
    AndroidBitmap_unlockPixels(env,bmpObj);
    return (jboolean) true;
}


}
