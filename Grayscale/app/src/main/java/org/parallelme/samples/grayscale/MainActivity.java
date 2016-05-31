package org.parallelme.samples.grayscale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private GrayscaleOperator operator;
    private ImageView imageView;

    public void showOriginal(View view) {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow));
    }

    public void showGrayscale(View view) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.rainbow);
        operator.grayscale(image);
        imageView.setImageBitmap(image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operator = new GrayscaleOperator();
        imageView = (ImageView) findViewById(R.id.image_view);
    }
}
