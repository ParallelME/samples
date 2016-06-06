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
import org.parallelme.ParallelMERuntime;

public class BitmapTestWrapperImplPM implements BitmapTestWrapper {
	private long PM_image4Ptr;
	 
	private native void foreach1(long runtimePtr, long varPtr);
	private native void foreach2(long runtimePtr, long varPtr);
	 
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

	public void inputBind1(Bitmap bitmap) {
		PM_image4Ptr = ParallelMERuntime.getInstance().createBitmapImage(bitmap);
	}

	public void foreach1() {
		foreach1(ParallelMERuntime.getInstance().runtimePointer, PM_image4Ptr);
	}

	public void foreach2() {
		foreach2(ParallelMERuntime.getInstance().runtimePointer, PM_image4Ptr);
	}

	public void outputBind1(Bitmap bitmap) {
		ParallelMERuntime.getInstance().toBitmapBitmapImage(PM_image4Ptr, bitmap);
	}
}
