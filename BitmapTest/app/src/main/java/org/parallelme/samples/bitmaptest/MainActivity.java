package org.parallelme.samples.bitmaptest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v8.renderscript.RenderScript;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    BitmapUserLibraryTest mTestUL;
    BitmapTest mTestPM;
    BitmapTestRS mTestRS;
    ImageView mImageView;

    public void showImage(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rainbow);
        bitmap = mTestUL.load(bitmap);
        bitmap = mTestPM.load(bitmap);
        bitmap = mTestRS.load(bitmap);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestUL = new BitmapUserLibraryTest();
        mTestPM = new BitmapTest(RenderScript.create(this));
        mTestRS = new BitmapTestRS(RenderScript.create(this));
        mImageView = (ImageView) findViewById(R.id.image_view);
    }
}
