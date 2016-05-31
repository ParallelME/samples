/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

package org.parallelme.samples.arraytest;

import org.parallelme.ParallelMERuntime;

public class ArrayTestWrapperImplPM implements ArrayTestWrapper {
	private long PM_array7Ptr;
	 
	private native void foreach1(long runtimePtr, long varPtr, int varTeste, int[] PM_varTeste);
	 
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

	public void inputBind1(int[] tmp) {
		PM_array7Ptr = ParallelMERuntime.getInstance().createArray(tmp);
	}

	public void foreach1(int[] varTeste) {
		foreach1(ParallelMERuntime.getInstance().runtimePointer, PM_array7Ptr, varTeste[0], varTeste);
	}

	public void outputBind1(int[] tmp) {
		ParallelMERuntime.getInstance().toArray(PM_array7Ptr, tmp);
	}
}
