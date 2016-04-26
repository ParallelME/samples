package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.function.ForeachFunction;
import br.ufmg.dcc.parallelme.userlibrary.image.HDRImage;
import br.ufmg.dcc.parallelme.userlibrary.image.Pixel;
import br.ufmg.dcc.parallelme.userlibrary.image.RGBE;

public class ReinhardCollectionOperator implements ReinhardOperator {
    private HDRImage image;
    private float sum;
    private float max;
    private float scaleFactor;
    private float lmax2;

    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap) {
        image = new HDRImage(resourceData);

        this.toYxy();
        this.logAverage(key);
        this.tonemap();
        this.toRgb();
        this.clamp(power);
        image.toBitmap(bitmap);

        image = null;
    }

    public void waitFinish() {
        // Do nothing.
    }

    private void toYxy(){
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                float[] result = new float[3];
                float w;

                result[0] = result[1] = result[2] = 0.0f;
                result[0] += 0.5141364f * pixel.rgba.red;
                result[0] += 0.3238786f * pixel.rgba.green;
                result[0] += 0.16036376f * pixel.rgba.blue;
                result[1] += 0.265068f * pixel.rgba.red;
                result[1] += 0.67023428f * pixel.rgba.green;
                result[1] += 0.06409157f * pixel.rgba.blue;
                result[2] += 0.0241188f * pixel.rgba.red;
                result[2] += 0.1228178f * pixel.rgba.green;
                result[2] += 0.84442666f * pixel.rgba.blue;
                w = result[0] + result[1] + result[2];
                if (w > 0.0) {
                    pixel.rgba.red = result[1];
                    pixel.rgba.green = result[0] / w;
                    pixel.rgba.blue = result[1] / w;
                } else {
                    pixel.rgba.red = pixel.rgba.green = pixel.rgba.blue = 0.0f;
                }
            }
        });
    }


    //This is a good example. We lack a way to return single/multiple values from all kernel instances.
    private void logAverage(float key) {
        sum = 0.0f;
        max = 0.0f;

        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                sum += Math.log(0.00001f + pixel.rgba.red);

                if(pixel.rgba.red > max)
                    max = pixel.rgba.red;
            }
        });

        // Calculate the scale factor.
        float average = (float) Math.exp(sum / (float)(image.getHeight() * image.getWidth()));
        scaleFactor = key * (1.0f / average);

        // lmax2.
        lmax2 = (float) Math.pow(max * scaleFactor, 2);
    }

    private void tonemap() {
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                // Scale to midtone.
                pixel.rgba.red *= scaleFactor;

                // Tonemap.
                pixel.rgba.red *= (1.0f + pixel.rgba.red / lmax2) / (1.0f + pixel.rgba.red);
            }
        });
    }

    private void toRgb(){
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                float x, y, z, g, b;

                y = pixel.rgba.red;     // Y
                g = pixel.rgba.green;   // x
                b = pixel.rgba.blue;    // y

                if (y > 0.0f && g > 0.0f && b > 0.0f) {
                    x = g * y / b;
                    z = x / g - x - y;
                } else {
                    x = z = 0.0f;
                }

                // These constants are the conversion coefficients.
                pixel.rgba.red = pixel.rgba.green = pixel.rgba.blue = 0.0f;
                pixel.rgba.red += 2.5651f * x;
                pixel.rgba.red += -1.1665f * y;
                pixel.rgba.red += -0.3986f * z;
                pixel.rgba.green += -1.0217f * x;
                pixel.rgba.green += 1.9777f * y;
                pixel.rgba.green += 0.0439f * z;
                pixel.rgba.blue += 0.0753f * x;
                pixel.rgba.blue += -0.2543f * y;
                pixel.rgba.blue += 1.1892f * z;
            }
        });
    }

    private void clamp(final float power) {
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                // Clamp.
                if (pixel.rgba.red > 1.0f) pixel.rgba.red = 1.0f;
                if (pixel.rgba.green > 1.0f) pixel.rgba.green = 1.0f;
                if (pixel.rgba.blue > 1.0f) pixel.rgba.blue = 1.0f;
            }
        });
    }
}