# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.
# 指定cmke版本
cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add.library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.


# add_library()命令用于向CMake添加依赖源文件或库
# 指令需传入三个参数（函数库名称、库类型、依赖源文件相对路径）

file(GLOB source_files "src/main/cpp/*.cpp")
add_library(
             # Specifies the name of the library.
             # 生成函数库的名称，即libnative-lib.so或libnative-lib.a(lib和.so/.a默认缺省)
             native-lib
             # Sets the library as a shared library.
             SHARED# 生成库类型：动态库为SHARED，静态库为STATIC
             # Provides a relative path to your source file(s).
             # 依赖的c/cpp文件(相对路径)
             ${source_files}
             )

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )



# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib
                       -ljnigraphics

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )