package com.exexample.opencvandar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
    }
    private ImageView mImageView;
    private void initView() {
        mImageView= (ImageView) findViewById(R.id.mImage);
    }
    public void change(View v){
        grayPic(R.mipmap.ic_launcher);
        v.setVisibility(View.GONE);
    }
    private void grayPic(int id){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),id);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pixels = new int[w*h];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        //recall JNI
        int[] resultInt = NdkLoader.getGrayImage(pixels, w, h);
        Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);
        mImageView.setImageBitmap(resultImg);
    }

}
