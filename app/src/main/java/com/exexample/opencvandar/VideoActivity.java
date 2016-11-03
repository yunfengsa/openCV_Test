package com.exexample.opencvandar;

import android.app.Activity;
import android.os.Bundle;
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

public class VideoActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mCVCamera;

    private static String TAG="VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_layout);
        mCVCamera=((CameraBridgeViewBase)findViewById(R.id.camera_view));
        mCVCamera.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(),"初始化失败",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    private int status=0;
    public void changeState(View v){
        if (status<3){
            status++;
        }else {
            status=0;
        }
    }
    private Mat mRgba;//对每帧数据进行缓存
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba=new Mat(height,width, CvType.CV_8UC4);
    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Size sizeRgba = inputFrame.rgba().size();
        int rows = (int) sizeRgba.height;
        int cols = (int) sizeRgba.width;
        switch (status){
            case 0: //灰度处理
                Imgproc.cvtColor(inputFrame.gray(),mRgba,Imgproc.COLOR_GRAY2RGBA,4);
                break;
            case 1: //Canny边缘检测
                Imgproc.Canny(inputFrame.gray(),mRgba,80,100);
                break;
            case 2:
                //ZOOM放大镜
                Mat zoomCorner = mRgba.submat(0, rows / 2 - rows / 10, 0, cols / 2 - cols / 10);
                Mat mZoomWindow = mRgba.submat(rows / 2 - 9 * rows / 100, rows / 2 + 9 * rows / 100, cols / 2 - 9 * cols / 100, cols / 2 + 9 * cols / 100);
                Imgproc.resize(mZoomWindow, zoomCorner, zoomCorner.size());
                Size wsize = mZoomWindow.size();
                Imgproc.rectangle(mZoomWindow, new Point(1, 1), new Point(wsize.width - 2, wsize.height - 2), new Scalar(255, 0, 0, 255), 2);
                zoomCorner.release();
                mZoomWindow.release();
                break;
            default://正常值
                mRgba=inputFrame.rgba();
        }
        return mRgba;
    }
    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

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
