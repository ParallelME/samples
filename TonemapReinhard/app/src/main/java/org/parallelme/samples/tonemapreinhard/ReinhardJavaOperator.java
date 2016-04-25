package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import android.graphics.Color;
import org.parallelme.samples.tonemapreinhard.formats.RGBE;

public class ReinhardJavaOperator implements ReinhardOperator {
    public float[][][] data;
    public int width, height;

    private void loadImage(RGBE.ResourceData resourceData) {
        width = resourceData.width;
        height = resourceData.height;
        data = new float[height][width][3];

        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
                RGBE.rgbe2float(data[y][x], resourceData.data, 4 * (y * width + x));
    }

    /** Saves the current data on the given bitmap. Must be the same size. */
    private void toBitmap(Bitmap bitmap, float power) {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                bitmap.setPixel(x, y, Color.rgb(
                        (int) (255.0f * Math.pow(data[y][x][0], power)),
                        (int) (255.0f * Math.pow(data[y][x][1], power)),
                        (int) (255.0f * Math.pow(data[y][x][2], power))
                ));
            }
        }
    }

    private void toRgb() {
        float[] result = new float[3];
        float xVal, yVal, zVal;

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                yVal = data[y][x][0];       // Y
                result[1] = data[y][x][1];  // x
                result[2] = data[y][x][2];  // y

                if(yVal > 0.0f && result[1] > 0.0f && result[2] > 0.0f) {
                    xVal = result[1] * yVal / result[2];
                    zVal = xVal / result[1] - xVal - yVal;
                }
                else {
                    xVal = zVal = 0.0f;
                }

                data[y][x][0] = data[y][x][1] = data[y][x][2] = 0.0f;
                data[y][x][0] += 2.5651f * xVal;
                data[y][x][0] += -1.1665f * yVal;
                data[y][x][0] += -0.3986f * zVal;
                data[y][x][1] += -1.0217f * xVal;
                data[y][x][1] += 1.9777f * yVal;
                data[y][x][1] += 0.0439f * zVal;
                data[y][x][2] += 0.0753f * xVal;
                data[y][x][2] += -0.2543f * yVal;
                data[y][x][2] += 1.1892f * zVal;
            }
        }
    }

    private void toYxy() {
        float[] result = new float[3];
        float w;

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                result[0] = result[1] = result[2] = 0.0f;
                result[0] += 0.5141364f * data[y][x][0];
                result[0] += 0.3238786f * data[y][x][1];
                result[0] += 0.16036376f * data[y][x][2];
                result[1] += 0.265068f * data[y][x][0];
                result[1] += 0.67023428f * data[y][x][1];
                result[1] += 0.06409157f * data[y][x][2];
                result[2] += 0.0241188f * data[y][x][0];
                result[2] += 0.1228178f * data[y][x][1];
                result[2] += 0.84442666f * data[y][x][2];
                w = result[0] + result[1] + result[2];

                if(w > 0.0) {
                    data[y][x][0] = result[1];      // Y
                    data[y][x][1] = result[0] / w;  // x
                    data[y][x][2] = result[1] / w;  // y
                }
                else {
                    data[y][x][0] = data[y][x][1] = data[y][x][2] = 0.0f;
                }
            }
        }
    }

    private void clamp() {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                if(data[y][x][0] > 1.0f) data[y][x][0] = 1.0f;
                if(data[y][x][1] > 1.0f) data[y][x][1] = 1.0f;
                if(data[y][x][2] > 1.0f) data[y][x][2] = 1.0f;
            }
        }
    }

    private static class LogAverageReturn {
        public float scaleFactor;
        public float lmax2;
    }

    private LogAverageReturn logAverage(float key) {
        float sum = 0.0f;
        float max = 0.0f;

        for(int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                sum += Math.log(0.00001 + data[y][x][0]);

                if(data[y][x][0] > max)
                    max = data[y][x][0];
            }
        }

        // Calculate the average.
        float average = (float) Math.exp(sum / (float)(height * width));
        float scaleFactor = key * (1.0f / average);

        // Save the scale factor and lmax2.
        LogAverageReturn ret = new LogAverageReturn();
        ret.scaleFactor = key * (1.0f / average);
        ret.lmax2 = (float) Math.pow(max * scaleFactor, 2);

        return ret;
    }

    private void tonemap(float scaleFactor, float lmax2) {
        for(int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // Scale to midtone.
                data[y][x][0] *= scaleFactor;

                // Tonemap.
                data[y][x][0] *= (1.0f + data[y][x][0] / lmax2)
                        / (1.0f + data[y][x][0]);
            }
        }
    }

    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap) {
        loadImage(resourceData);

        toYxy();
        LogAverageReturn ret = logAverage(key);
        tonemap(ret.scaleFactor, ret.lmax2);
        toRgb();
        clamp();

        toBitmap(bitmap, power);

        data = null;
    }

    public void waitFinish() {
        // Do nothing.
    }
}
