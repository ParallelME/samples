/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import android.support.v8.renderscript.RenderScript;

import org.parallelme.userlibrary.image.RGBE;

public class ReinhardCompilerOperatorRS implements ReinhardOperator {
    ParallelMEReinhardCompilerOperator $mParallelMEReinhardCompilerOperator;

 	public ReinhardCompilerOperatorRS(RenderScript $mRS) {
        $mParallelMEReinhardCompilerOperator = new ParallelMEReinhardCompilerOperatorRS($mRS);
	}

    private float sum;
    private float max;
    private float scaleFactor;
    private float lmax2;

    public void runOp(RGBE.ResourceData $imageResourceData, float key, float power, Bitmap bitmap) {
        $mParallelMEReinhardCompilerOperator.createHDRImage($imageResourceData);

        this.toYxy();
        this.logAverage(key);
        this.tonemap();
        this.toRgb();
        this.clamp(power);
        $mParallelMEReinhardCompilerOperator.toBitmap(bitmap);
    }

    public void waitFinish() {
        
    }

    private void toYxy(){
        $mParallelMEReinhardCompilerOperator.iterator1();
    }

    private void logAverage(float key) {
        sum = 0.0f;
        max = 0.0f;

        $mParallelMEReinhardCompilerOperator.setSumIterator2(sum);
        $mParallelMEReinhardCompilerOperator.setMaxIterator2(max);
        $mParallelMEReinhardCompilerOperator.iterator2();
        sum = $mParallelMEReinhardCompilerOperator.getSumIterator2();
        max = $mParallelMEReinhardCompilerOperator.getMaxIterator2();

        float average = (float) Math.exp(sum /(float)(
                $mParallelMEReinhardCompilerOperator.getHeight()
                        * $mParallelMEReinhardCompilerOperator.getWidth()));
        scaleFactor = key * (1.0f / average);

        lmax2 = (float) Math.pow(max * scaleFactor, 2);
    }

    private void tonemap() {
        final float fScaleFactor = scaleFactor;
        final float fLmax2 = lmax2;
        $mParallelMEReinhardCompilerOperator.setScaleFactorIterator3(fScaleFactor);
        $mParallelMEReinhardCompilerOperator.setLmax2Iterator3(fLmax2);
        $mParallelMEReinhardCompilerOperator.iterator3();
    }

    private void toRgb(){
        $mParallelMEReinhardCompilerOperator.iterator4();
    }

    private void clamp(final float power) {
        $mParallelMEReinhardCompilerOperator.setPowerIterator5(power);
        $mParallelMEReinhardCompilerOperator.iterator5();
    }
}
