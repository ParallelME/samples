/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.samples.tonemapreinhard;
import android.graphics.Bitmap;
import org.parallelme.userlibrary.image.RGBE;

public interface ParallelMEReinhardCompilerOperator {
    void createHDRImage(RGBE.ResourceData $imageResourceData);
    void toBitmap(Bitmap bitmap);
    int getHeight();
    int getWidth();
    void iterator1();
    void setSumIterator2(float sum);
    void setMaxIterator2(float max);
    void iterator2();
    float getSumIterator2();
    float getMaxIterator2();
    void setScaleFactorIterator3(float scaleFactor);
    void setLmax2Iterator3(float lmax2);
    void iterator3();
    float getScaleFactorIterator3();
    float getLmax2Iterator3();
    void iterator4();
    void setPowerIterator5(float power);
    void iterator5();
}
