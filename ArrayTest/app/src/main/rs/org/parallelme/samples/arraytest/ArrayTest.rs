/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#pragma version(1)
#pragma rs java_package_name(org.parallelme.samples.arraytest)
rs_allocation PM_gInputArrayForeach1;
rs_allocation PM_gOutputVarTesteForeach1;
int PM_gInputXSizeForeach1;
int PM_gInputYSizeForeach1;
int PM_gVarTesteForeach1;

void foreach1() 
 {
	int element;
	for (int PM_x = 0; PM_x < PM_gInputXSizeForeach1; PM_x++) {
		element = rsGetElementAt_int(PM_gInputArrayForeach1, PM_x);
		PM_gVarTesteForeach1 += 1;
						element = element + PM_gVarTesteForeach1;
		rsSetElementAt_int(PM_gInputArrayForeach1, element, PM_x);
	}
	rsSetElementAt_int(PM_gOutputVarTesteForeach1, PM_gVarTesteForeach1, 0);
}
