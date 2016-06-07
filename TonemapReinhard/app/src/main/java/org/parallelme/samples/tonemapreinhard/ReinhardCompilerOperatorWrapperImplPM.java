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
import org.parallelme.ParallelMERuntime;
import org.parallelme.userlibrary.image.Pixel;

public class ReinhardCompilerOperatorWrapperImplPM implements ReinhardCompilerOperatorWrapper {
	private long PM_image2Ptr;
	 
	private native void foreach1(long runtimePtr, long varPtr);
	private native void reduce2(long runtimePtr, long varPtr, float[] PM_ret);
	private native void foreach3(long runtimePtr, long varPtr, float fScaleFactor, float fLmax2);
	private native void foreach4(long runtimePtr, long varPtr);
	private native void foreach5(long runtimePtr, long varPtr, float power);
	 
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	 
	static {
		System.loadLibrary("ParallelMEGenerated");
	}

	public boolean isValid() {
		return ParallelMERuntime.getInstance().runtimePointer != 0;
	}

	public void inputBind1(byte[] data, int width, int height) {
		PM_image2Ptr = ParallelMERuntime.getInstance().createHDRImage(data, width, height);
	}

	public void foreach1() {
		foreach1(ParallelMERuntime.getInstance().runtimePointer, PM_image2Ptr);
	}

	public Pixel reduce2() {
		float[] PM_ret = new float[4];
		reduce2(ParallelMERuntime.getInstance().runtimePointer, PM_image2Ptr, PM_ret);
		return new Pixel(PM_ret[0], PM_ret[1], PM_ret[2], PM_ret[3], -1, -1);
	}

	public void foreach3(float fScaleFactor, float fLmax2) {
		foreach3(ParallelMERuntime.getInstance().runtimePointer, PM_image2Ptr, fScaleFactor, fLmax2);
	}


	public void foreach4() {
		foreach4(ParallelMERuntime.getInstance().runtimePointer, PM_image2Ptr);
	}

	public void foreach5(float power) {
		foreach5(ParallelMERuntime.getInstance().runtimePointer, PM_image2Ptr, power);
	}

	public void outputBind1(Bitmap bitmap) {
		ParallelMERuntime.getInstance().toBitmapHDRImage(PM_image2Ptr, bitmap);
	}

	public int getHeight1() {
		return ParallelMERuntime.getInstance().getHeight(PM_image2Ptr);
	}

	public int getWidth2() {
		return ParallelMERuntime.getInstance().getWidth(PM_image2Ptr);
	}
}
