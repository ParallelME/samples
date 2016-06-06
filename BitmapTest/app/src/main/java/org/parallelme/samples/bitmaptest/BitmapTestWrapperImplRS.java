/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

package org.parallelme.samples.bitmaptest;

import android.graphics.Bitmap;
import android.support.v8.renderscript.*;

public class BitmapTestWrapperImplRS implements BitmapTestWrapper {
	private Allocation PM_image4In, PM_image4Out;
	private RenderScript PM_mRS;
	private ScriptC_BitmapTest PM_kernel;
	public BitmapTestWrapperImplRS(RenderScript PM_mRS) {
		this.PM_mRS = PM_mRS;
		this.PM_kernel = new ScriptC_BitmapTest(PM_mRS);
	}

	public boolean isValid() {
		return true;
	}

	public void inputBind1(Bitmap bitmap) {
		Type PM_imageInDataType;
		PM_image4In = Allocation.createFromBitmap(PM_mRS, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);
		PM_imageInDataType = new Type.Builder(PM_mRS, Element.F32_4(PM_mRS))
			.setX(PM_image4In.getType().getX())
			.setY(PM_image4In.getType().getY())
			.create();
		PM_image4Out = Allocation.createTyped(PM_mRS, PM_imageInDataType);
		PM_kernel.forEach_toFloatBitmapImage(PM_image4In, PM_image4Out);
	}

	public void foreach1() {
		PM_kernel.forEach_foreach1(PM_image4Out, PM_image4Out);
	}

	public void foreach2() {
		PM_kernel.forEach_foreach2(PM_image4Out, PM_image4Out);
	}

	public void outputBind1(Bitmap bitmap) {
		PM_kernel.forEach_toBitmapBitmapImage(PM_image4Out, PM_image4In);
		PM_image4In.copyTo(bitmap);
	}
}
