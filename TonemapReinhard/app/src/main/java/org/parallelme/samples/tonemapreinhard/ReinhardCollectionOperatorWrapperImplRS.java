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

public class ReinhardCollectionOperatorWrapperImplRS implements ReinhardCollectionOperatorWrapper {
	private Allocation $image2In, $image2Out;
	private RenderScript $mRS;
	private ScriptC_ReinhardCollectionOperator $kernel;
	public ReinhardCollectionOperatorWrapperImplRS(RenderScript $mRS) {
		this.$mRS = $mRS;
		this.$kernel = new ScriptC_ReinhardCollectionOperator($mRS);
	}

	public boolean isValid() {
		return true;
	}

	public void inputBind1(byte[] data, int width, int height) {
		Type $imageInDataType = new Type.Builder($mRS, Element.RGBA_8888($mRS))
			.setX(width)
			.setY(height)
			.create();
		Type $imageOutDataType = new Type.Builder($mRS, Element.F32_4($mRS))
			.setX(width)
			.setY(height)
			.create();
		$image2In = Allocation.createTyped($mRS, $imageInDataType, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		$image2Out = Allocation.createTyped($mRS, $imageOutDataType);
		$image2In.copyFrom(data);
		$kernel.forEach_toFloatHDRImage($image2In, $image2Out);
	}

	public void iterator1() {
		$kernel.forEach_iterator1($image2Out, $image2Out);
	}

	public void iterator2(float[] sum, float[] max) {
		Allocation $gSumIterator2_Allocation = Allocation.createSized($mRS, Element.F32($mRS), 1);
		$kernel.set_$gSumIterator2(sum[0]);
		$kernel.set_$gOutputSumIterator2($gSumIterator2_Allocation);
		Allocation $gMaxIterator2_Allocation = Allocation.createSized($mRS, Element.F32($mRS), 1);
		$kernel.set_$gMaxIterator2(max[0]);
		$kernel.set_$gOutputMaxIterator2($gMaxIterator2_Allocation);
		$kernel.set_$gInputImageIterator2($image2Out);
		$kernel.set_$gInputXSizeIterator2($image2Out.getType().getX());
		$kernel.set_$gInputYSizeIterator2($image2Out.getType().getY());
		$kernel.invoke_iterator2();
		$gSumIterator2_Allocation.copyTo(sum);$gMaxIterator2_Allocation.copyTo(max);
	}

	public void iterator3(float fScaleFactor, float fLmax2) {
		$kernel.set_$gFScaleFactorIterator3(fScaleFactor);
		$kernel.set_$gFLmax2Iterator3(fLmax2);
		$kernel.forEach_iterator3($image2Out, $image2Out);
	}

	public void iterator4() {
		$kernel.forEach_iterator4($image2Out, $image2Out);
	}

	public void iterator5(float power) {
		$kernel.set_$gPowerIterator5(power);
		$kernel.forEach_iterator5($image2Out, $image2Out);
	}

	public void outputBind1(Bitmap bitmap) {
		$kernel.forEach_toBitmapHDRImage($image2Out, $image2In);
		$image2In.copyTo(bitmap);
	}

	public int getHeight1() {
		return $image2In.getType().getY();
	}

	public int getWidth2() {
		return $image2In.getType().getX();
	}
}
