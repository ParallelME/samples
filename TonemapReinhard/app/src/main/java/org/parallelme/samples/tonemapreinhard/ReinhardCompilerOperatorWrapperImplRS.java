/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import android.support.v8.renderscript.*;
import org.parallelme.userlibrary.image.Pixel;

public class ReinhardCompilerOperatorWrapperImplRS implements ReinhardCompilerOperatorWrapper {
	private Allocation PM_image2In, PM_image2Out;
	private RenderScript PM_mRS;
	private ScriptC_ReinhardCompilerOperator PM_kernel;
	public ReinhardCompilerOperatorWrapperImplRS(RenderScript PM_mRS) {
		this.PM_mRS = PM_mRS;
		this.PM_kernel = new ScriptC_ReinhardCompilerOperator(PM_mRS);
	}

	public boolean isValid() {
		return true;
	}

	public void inputBind1(byte[] data, int width, int height) {
		Type PM_imageInDataType = new Type.Builder(PM_mRS, Element.RGBA_8888(PM_mRS))
			.setX(width)
			.setY(height)
			.create();
		Type PM_imageOutDataType = new Type.Builder(PM_mRS, Element.F32_4(PM_mRS))
			.setX(width)
			.setY(height)
			.create();
		PM_image2In = Allocation.createTyped(PM_mRS, PM_imageInDataType, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		PM_image2Out = Allocation.createTyped(PM_mRS, PM_imageOutDataType);
		PM_image2In.copyFrom(data);
		PM_kernel.forEach_toFloatHDRImage(PM_image2In, PM_image2Out);
	}

	public void foreach1() {
		PM_kernel.forEach_foreach1(PM_image2Out, PM_image2Out);
	}

	public Pixel reduce2() {
		Type PM_tileType = new Type.Builder(PM_mRS, Element.F32_4(PM_mRS))
				.setX(PM_image2Out.getType().getX())
				.create();
		Type PM_redType = new Type.Builder(PM_mRS, Element.F32_4(PM_mRS))
				.setX(1)
				.create();
		Allocation PM_tile = Allocation.createTyped(PM_mRS, PM_tileType);
		Allocation PM_red = Allocation.createTyped(PM_mRS, PM_redType);

		PM_kernel.set_PM_gInputImageReduce2((PM_image2Out));
		PM_kernel.set_PM_gTileReduce2((PM_tile));
		PM_kernel.set_PM_gInputXSizeReduce2(PM_image2Out.getType().getX());
		PM_kernel.set_PM_gInputYSizeReduce2(PM_image2Out.getType().getY());
		PM_kernel.forEach_reduce2_tile(PM_tile);
		PM_kernel.forEach_reduce2(PM_red);

		float[] PM_outRed = new float[4];
		PM_red.copyTo(PM_outRed);
		return new Pixel(PM_outRed[0], PM_outRed[1], PM_outRed[2], PM_outRed[3], -1, -1);
	}

	public void foreach3(float fScaleFactor, float fLmax2) {
		PM_kernel.set_PM_gFScaleFactorForeach3(fScaleFactor);
		PM_kernel.set_PM_gFLmax2Foreach3(fLmax2);
		PM_kernel.forEach_foreach3(PM_image2Out, PM_image2Out);
	}

	public void foreach4() {
		PM_kernel.forEach_foreach4(PM_image2Out, PM_image2Out);
	}

	public void foreach5(float power) {
		PM_kernel.set_PM_gPowerForeach5(power);
		PM_kernel.forEach_foreach5(PM_image2Out, PM_image2Out);
	}

	public void outputBind1(Bitmap bitmap) {
		PM_kernel.forEach_toBitmapHDRImage(PM_image2Out, PM_image2In);
		PM_image2In.copyTo(bitmap);
	}

	public int getHeight1() {
		return PM_image2In.getType().getY();
	}

	public int getWidth2() {
		return PM_image2In.getType().getX();
	}
}
