# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

#工程路径
set(pathToProject D:/adroid_studio_projects/OpenCV/MyApplication)
#OpenCV-android-sdk路径
set(pathToOpenCv D:/OpenCV/OpenCV-android-sdk)

#CMake版本信息
cmake_minimum_required(VERSION 3.4.1)




#CPP文件夹下带编译的cpp文件
add_library( native-lib SHARED src/main/jni/native-lib.cpp )
#配置加载native依赖
include_directories(${pathToOpenCv}/sdk/native/jni/include)

#动态方式加载
add_library( lib_opencv SHARED IMPORTED )

#引入libopencv_java3.so文件
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION
${pathToProject}/app/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)

#C++日志
find_library( log-lib log )

#target_link_libraries( native-lib $\{log-lib} )
target_link_libraries( native-lib $\{log-lib} lib_opencv)






