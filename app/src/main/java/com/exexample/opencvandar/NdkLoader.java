package com.exexample.opencvandar;

/**
 * Created by Administrator on 2016/11/2.
 */

public class NdkLoader {
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static native String validate(long matAddrGr, long matAddrRgba);

    //图像处理
    public static native int[] getGrayImage(int[] pixels, int w, int h);

    //face 检测
    public static native void detectFace(String eyePath,
                                          String cascade, long nativeMat);
    public static native int[] gestureDetection2(String srcImg,String dataDir);
    public static native int gestureRecognization(long nativeMat);
    public static native void concexHull(long nativeMat,long dstMat);
    public static native void detectPow(String handPath,long nativeMat);
    public static native void skinYCrCb(long nativeMat,long dstMat);
}
