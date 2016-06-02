package org.parallelme.samples.grayscale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private GrayscaleOperator operator;
    private ImageView imageView;

    public void showOriginal(View view) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.rainbow);
        imageView.setImageBitmap(image);
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
