package com.exexample.opencvandar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Administrator on 2016/11/2.
 */

public class GestureActivity extends Activity
//        implements CameraBridgeViewBase.CvCameraViewListener2
{

    private CameraBridgeViewBase mCVCamera;

    private static String TAG="GestureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_layout);
//        mCVCamera=((CameraBridgeViewBase)findViewById(R.id.camera_view));
//        mCVCamera.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(),"初始化失败",Toast.LENGTH_SHORT).show();
            finish();
        } else {
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();

    }
    private Mat mRgba;//对每帧数据进行缓存
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//        mRgba=new Mat(height,width, CvType.CV_8UC4);
//    }
    private void start(){
    String jpgAddr= Environment.getExternalStorageDirectory()+"/face.jpg";
    String faceArAddr=Environment.getExternalStorageDirectory().getAbsolutePath();
    Log.d(TAG,jpgAddr+"-------"+faceArAddr);
        final int[] faces=NdkLoader.gestureDetection2(jpgAddr,faceArAddr);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),faces==null?"为空":faces.length+"",Toast.LENGTH_LONG).show();
            }
        });

    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//
//        Size sizeRgba = inputFrame.rgba().size();
//        int rows = (int) sizeRgba.height;
//        int cols = (int) sizeRgba.width;
//        return mRgba;
//    }
//    @Override
//    public void onCameraViewStopped() {
//        mRgba.release();
//    }

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status){
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG,"OpenCV loaded successfully");
                    mCVCamera.enableView();
                    break;
                default:
                    break;
            }
        }
    };

}
