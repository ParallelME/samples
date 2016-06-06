package org.parallelme.samples.bitmaptest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.parallelme.userlibrary.function.*;
import org.parallelme.userlibrary.image.Image;
import org.parallelme.userlibrary.image.RGB;


import android.support.v8.renderscript.*;

public class BitmapTest {
	private BitmapTestWrapper PM_parallelME;

	public BitmapTest(RenderScript PM_mRS) {
		this.PM_parallelME = new BitmapTestWrapperImplPM();
		if (!this.PM_parallelME.isValid())
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
