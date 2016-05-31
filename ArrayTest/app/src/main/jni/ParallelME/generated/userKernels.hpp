/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#ifndef USERKERNELS_HPP
#define USERKERNELS_HPP

const char userKernels[] =
	"\n"
	"\n"
	"__kernel void foreach1(__global int* PM_data, int varTeste, __global int* PM_varTeste, int PM_length)\n"
	" {\n"
	"	for (int PM_x = 0; PM_x < PM_length; ++PM_x) {\n"
	"		int element = PM_data[PM_x];\n"
	"		varTeste += 1;\n"
	"						element = element + varTeste;\n"
	"		PM_data[PM_x] = element;\n"
	"	}\n"
	"	*PM_varTeste = varTeste;\n"
	"}\n"
	"\n"
	"\n"
	"\n";
#endif
