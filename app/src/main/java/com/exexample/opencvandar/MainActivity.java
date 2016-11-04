package com.exexample.opencvandar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends Activity {

    private static String TAG="MainActivity";
    static {
        if (OpenCVLoader.initDebug()){
            Log.i(TAG,"success");
        }else {
            Log.i(TAG,"false");
        }
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

    }
    public void jump(View v){
        switch (v.getId()){
            case R.id.toVideo:
                Intent intent=new Intent(getApplicationContext(),VideoActivity.class);
                startActivity(intent);
                break;
            case R.id.toImage:
                Intent intent1=new Intent(getApplicationContext(),ImageActivity.class);
                startActivity(intent1);
                break;
            case R.id.gestureDetection:
                Intent intent2=new Intent(getApplicationContext(),GestureActivity.class);
                startActivity(intent2);
                break;
            case R.id.gestureDetection2:
                Intent intent3=new Intent(getApplicationContext(),ImageManipulationsActivity.class);
                startActivity(intent3);
                break;
            default:

        }
    }

}
