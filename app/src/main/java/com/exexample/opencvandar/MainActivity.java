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

                break;
            default:

        }
    }



    public native String stringFromJNI();
}
