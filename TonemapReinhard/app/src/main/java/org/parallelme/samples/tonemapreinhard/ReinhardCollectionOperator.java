

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;

import org.parallelme.userlibrary.function.ForeachFunction;
import org.parallelme.userlibrary.image.HDRImage;
import org.parallelme.userlibrary.image.Pixel;
import org.parallelme.userlibrary.image.RGBE;


import android.support.v8.renderscript.*;

public class ReinhardCollectionOperator implements ReinhardOperator {
	private ReinhardCollectionOperatorWrapper $parallelME;

	public ReinhardCollectionOperator(RenderScript $mRS) {
		this.$parallelME = new ReinhardCollectionOperatorWrapperImplPM();
		if (!this.$parallelME.isValid())
			this.$parallelME = new ReinhardCollectionOperatorWrapperImplRS($mRS);
	}

    private float sum;
    private float max;
    private float scaleFactor;
    private float lmax2;

    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap) {
        $parallelME.inputBind1(resourceData.data, resourceData.width, resourceData.height);

        this.toYxy();
        this.logAverage(key);
        this.tonemap();
        this.toRgb();
        this.clamp(power);
        $parallelME.outputBind1(bitmap);
    }

    public void waitFinish() {
        
    }

    private void toYxy(){
        $parallelME.iterator1();
    }


    
    private void logAverage(float key) {
        sum = 0.0f;
        max = 0.0f;

        float[] $sum = new float[1];
$sum[0] = sum;
float[] $max = new float[1];
$max[0] = max;
$parallelME.iterator2($sum, $max);
sum = $sum[0];max = $max[0];

        
        float average = (float) Math.exp(sum / (float)($parallelME.getHeight1() * $parallelME.getWidth2()));
        scaleFactor = key * (1.0f / average);

        
        lmax2 = (float) Math.pow(max * scaleFactor, 2);
    }

    private void tonemap() {
        final float fScaleFactor = scaleFactor;
        final float fLmax2 = lmax2;
        $parallelME.iterator3(fScaleFactor, fLmax2);
    }

    private void toRgb(){
        $parallelME.iterator4();
    }

    private void clamp(final float power) {
        $parallelME.iterator5(power);
    }
}
