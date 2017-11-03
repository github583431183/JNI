# JNI

## C 与 Java 的区别
1. C 没有 boolean 和 byte
2. 数据长度
  1. long 4 字节
  2. char 1 字节
3. signed 与 unsigned 只能用来修饰 char short int long
4. print
5. C 的字符串 char 数组，char数组结束符 '\0'
  ```
   char array[] = {'a','b','\0'};
   char array[] = "abcd";
   char* str = "abcd";
  ```
## JNI开发流程
1. 将 c -> .so , 编译命令 ndk-build 
  ![ndk-build](pic\ndk-build.jpg)

2. 使用 native 声明本地方法，具体实现在 C 中
    ```
    java
    // 加载 so 文件
    static{
    	System.loadLibrary("hello");
    }

     public native String helloJNI();

    c

    // c 本地函数命名规则  Java_包名_类名_本地方法名
    // jobject  调用本地函数的java对象 在这个例子中 就是MainActivity的实例
    // JNIEnv* env 是结构体JNINativeInterface 的二级指针
      // JNIEnv 是结构体JNINativeInterface 的一级指针
      // JNINativeInterface结构体中定义了大量的函数指针 这些函数指针在jni开发中很常用
      // 调用自定义结构体中的函数指针 (*env)->
       
    jstring Java_com_itheima_jnihello_MainActivity_hello_1FromC(JNIEnv* env,jobject thiz){
        char* cstr = "hello from c!";
        return (*env)->NewStringUTF(env,cstr);
    }

    ```

3. Android.mk
  ```
  LOCAL_PATH := $(call my-dir)

    include $(CLEAR_VARS)

    LOCAL_MODULE    := hello # 编译成为文件名 前缀 lib 和 .so 编译器会自动添加
    LOCAL_SRC_FILES := hello.c # 编译的文件名

    include $(BUILD_SHARED_LIBRARY)
  ```
4. Application.mk

     ```
     APP_ABI := armeabi #APP_ABI := all 全平台

     APP_PLATFORM := android-14 #制定最小版本
     ```

5. 常见错误

  * java.lang.UnsatisfiedLinkError: Native method not found: 本地方法没有找到
  * 本地函数名写错
  * 忘记加载.so文件 没有调用System.loadlibrary 
* findLibrary returned null
  * System.loadLibrary("libhello"); 加载动态链接库时 动态链接库名字写错
  * 平台类型错误 把只支持arm平台的.so文件部署到了 x86cpu的设备上 
    * 在jni目录下创建 Application.mk 在里面指定 
    * APP_ABI := armeabi #APP_ABI := all 全平台
      APP_PLATFORM := android-14
* javah 
  * jdk 1.7 项目 src目录下运行javah
  * jdk 1.6 项目 bin目录下 classes文件夹
  * javah native方法声明的java类的全类名 

