/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

package org.parallelme.samples.arraytest;

import android.support.v8.renderscript.*;

public class ArrayTestWrapperImplRS implements ArrayTestWrapper {
	private Allocation PM_array7In;
	private RenderScript PM_mRS;
	private ScriptC_ArrayTest PM_kernel;
	public ArrayTestWrapperImplRS(RenderScript PM_mRS) {
		this.PM_mRS = PM_mRS;
		this.PM_kernel = new ScriptC_ArrayTest(PM_mRS);
	}

	public boolean isValid() {
		return true;
	}

	public void inputBind1(int[] tmp) {
		PM_array7In = Allocation.createSized(PM_mRS, Element.I32(PM_mRS), tmp.length);
		PM_array7In.copyFrom(tmp);
	}

	public void foreach1(int[] varTeste) {
		Allocation PM_gVarTesteForeach1_Allocation = Allocation.createSized(PM_mRS, Element.I32(PM_mRS), 1);
		PM_kernel.set_PM_gVarTesteForeach1(varTeste[0]);
		PM_kernel.set_PM_gOutputVarTesteForeach1(PM_gVarTesteForeach1_Allocation);
		PM_kernel.set_PM_gInputArrayForeach1(PM_array7In);
		PM_kernel.set_PM_gInputXSizeForeach1(PM_array7In.getType().getX());
		PM_kernel.invoke_foreach1();
		PM_gVarTesteForeach1_Allocation.copyTo(varTeste);
	}

	public void outputBind1(int[] tmp) {
		PM_array7In.copyTo(tmp);
	}
}
