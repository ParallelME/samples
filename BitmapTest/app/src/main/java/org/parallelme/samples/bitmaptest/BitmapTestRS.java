package org.parallelme.samples.bitmaptest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v8.renderscript.RenderScript;

public class BitmapTestRS {
	private BitmapTestWrapper PM_parallelME;

	public BitmapTestRS(RenderScript PM_mRS) {
        // Test RS only
		//this.PM_parallelME = new BitmapTestWrapperImplPM();
		//if (!this.PM_parallelME.isValid())
			this.PM_parallelME = new BitmapTestWrapperImplRS(PM_mRS);
	}

    public Bitmap load(Bitmap bitmap) {
        PM_parallelME.inputBind1(bitmap);
        
        PM_parallelME.foreach1();
        
        PM_parallelME.foreach2();
        PM_parallelME.outputBind1(bitmap);

        return bitmap;
    }
}
