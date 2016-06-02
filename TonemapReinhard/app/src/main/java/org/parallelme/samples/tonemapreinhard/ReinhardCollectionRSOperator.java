

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import android.support.v8.renderscript.RenderScript;

import org.parallelme.userlibrary.image.RGBE;

public class ReinhardCollectionRSOperator implements ReinhardOperator {
	private ReinhardCollectionOperatorWrapper PM_parallelME;

	public ReinhardCollectionRSOperator(RenderScript PM_mRS) {
        // Force RS
        this.PM_parallelME = new ReinhardCollectionOperatorWrapperImplRS(PM_mRS);
	}

    
    private float sum;
    private float max;
    private float scaleFactor;
    private float lmax2;

    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap) {
        PM_parallelME.inputBind1(resourceData.data, resourceData.width, resourceData.height);

        this.toYxy();
        this.logAverage(key);
        this.tonemap();
        this.toRgb();
        this.clamp(power);
        PM_parallelME.outputBind1(bitmap);
    }

    public void waitFinish() {
        
    }

    private void toYxy(){
        PM_parallelME.foreach1();
    }


    
    private void logAverage(float key) {
        sum = 0.0f;
        max = 0.0f;

        float[] PM_sum = new float[1];
PM_sum[0] = sum;
float[] PM_max = new float[1];
PM_max[0] = max;
PM_parallelME.foreach2(PM_sum, PM_max);
sum = PM_sum[0];max = PM_max[0];

        
        float average = (float) Math.exp(sum / (float)(PM_parallelME.getHeight1() * PM_parallelME.getWidth2()));
        scaleFactor = key * (1.0f / average);

        
        lmax2 = (float) Math.pow(max * scaleFactor, 2);
    }

    private void tonemap() {
        final float fScaleFactor = scaleFactor;
        final float fLmax2 = lmax2;
        PM_parallelME.foreach3(fScaleFactor, fLmax2);
    }

    private void toRgb(){
        PM_parallelME.foreach4();
    }

    private void clamp(final float power) {
        PM_parallelME.foreach5(power);
    }
}
