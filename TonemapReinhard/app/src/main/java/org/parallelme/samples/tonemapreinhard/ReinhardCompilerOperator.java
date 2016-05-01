

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;






import android.support.v8.renderscript.*;
import org.parallelme.userlibrary.image.RGBE;

public class ReinhardCompilerOperator implements ReinhardOperator {
	RenderScript $mRS;
	ScriptC_ReinhardCompilerOperator $kernel_ReinhardCompilerOperator;

 	public ReinhardCompilerOperator(RenderScript $mRS) {
		this.$mRS = $mRS;
		this.$kernel_ReinhardCompilerOperator = new ScriptC_ReinhardCompilerOperator($mRS);
	}

    private Allocation $imageIn, $imageOut;

    private float sum;
    private float max;
    private float scaleFactor;
    private float lmax2;

    public void runOp(RGBE.ResourceData $imageResourceData, float key, float power, Bitmap bitmap) {
        Type $imageInDataType = new Type.Builder($mRS, Element.RGBA_8888($mRS))
            .setX($imageResourceData.width)
            .setY($imageResourceData.height)
            .create();
        Type $imageOutDataType = new Type.Builder($mRS, Element.F32_4($mRS))
            .setX($imageResourceData.width)
            .setY($imageResourceData.height)
            .create();
        $imageIn = Allocation.createTyped($mRS, $imageInDataType);
        $imageOut = Allocation.createTyped($mRS, $imageOutDataType);
        $imageIn.copyFrom($imageResourceData.data);
        $kernel_ReinhardCompilerOperator.forEach_toFloat($imageIn, $imageOut);

        this.toYxy();
        this.logAverage(key);
        this.tonemap();
        this.toRgb();
        this.clamp(power);
        $kernel_ReinhardCompilerOperator.forEach_toBitmap($imageOut, $imageIn);
        $imageIn.copyTo(bitmap);
        $imageIn = null;
        $imageOut = null;
    }

    public void waitFinish() {
        
    }

    private void toYxy(){
        $kernel_ReinhardCompilerOperator.forEach_iterator1($imageOut, $imageOut);

    }


    
    private void logAverage(float key) {
        sum = 0.0f;
        max = 0.0f;

        float[] $sum = new float[1];
        Allocation $gSumIterator2_Allocation = Allocation.createSized($mRS, Element.F32($mRS), 1);
        $kernel_ReinhardCompilerOperator.set_gSumIterator2(sum);
        $kernel_ReinhardCompilerOperator.set_gOutputSumIterator2($gSumIterator2_Allocation);
        float[] $max = new float[1];
        Allocation $gMaxIterator2_Allocation = Allocation.createSized($mRS, Element.F32($mRS), 1);
        $kernel_ReinhardCompilerOperator.set_gMaxIterator2(max);
        $kernel_ReinhardCompilerOperator.set_gOutputMaxIterator2($gMaxIterator2_Allocation);
        $kernel_ReinhardCompilerOperator.set_gInputImageIterator2($imageOut);
        $kernel_ReinhardCompilerOperator.set_gInputXSizeIterator2($imageOut.getType().getX());
        $kernel_ReinhardCompilerOperator.set_gInputYSizeIterator2($imageOut.getType().getY());
        $kernel_ReinhardCompilerOperator.invoke_iterator2();
        $gSumIterator2_Allocation.copyTo($sum);
        sum = $sum[0];
        $gMaxIterator2_Allocation.copyTo($max);
        max = $max[0];

        float average = (float) Math.exp(sum / (float)($imageIn.getType().getY() * $imageIn.getType().getX()));
        scaleFactor = key * (1.0f / average);

        lmax2 = (float) Math.pow(max * scaleFactor, 2);
    }

    private void tonemap() {
        float[] $scaleFactor = new float[1];
        Allocation $gScaleFactorIterator3_Allocation = Allocation.createSized($mRS, Element.F32($mRS), 1);
        $kernel_ReinhardCompilerOperator.set_gScaleFactorIterator3(scaleFactor);
        $kernel_ReinhardCompilerOperator.set_gOutputScaleFactorIterator3($gScaleFactorIterator3_Allocation);
        float[] $lmax2 = new float[1];
        Allocation $gLmax2Iterator3_Allocation = Allocation.createSized($mRS, Element.F32($mRS), 1);
        $kernel_ReinhardCompilerOperator.set_gLmax2Iterator3(lmax2);
        $kernel_ReinhardCompilerOperator.set_gOutputLmax2Iterator3($gLmax2Iterator3_Allocation);
        $kernel_ReinhardCompilerOperator.set_gInputImageIterator3($imageOut);
        $kernel_ReinhardCompilerOperator.set_gInputXSizeIterator3($imageOut.getType().getX());
        $kernel_ReinhardCompilerOperator.set_gInputYSizeIterator3($imageOut.getType().getY());
        $kernel_ReinhardCompilerOperator.invoke_iterator3();
        $gScaleFactorIterator3_Allocation.copyTo($scaleFactor);
        scaleFactor = $scaleFactor[0];
        $gLmax2Iterator3_Allocation.copyTo($lmax2);
        lmax2 = $lmax2[0];
    }

    private void toRgb(){
        $kernel_ReinhardCompilerOperator.forEach_iterator4($imageOut, $imageOut);

    }

    private void clamp(final float power) {
        $kernel_ReinhardCompilerOperator.set_gPowerIterator5(power);
        $kernel_ReinhardCompilerOperator.forEach_iterator5($imageOut, $imageOut);

    }
}
