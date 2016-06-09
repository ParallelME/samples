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
	"__kernel void toFloatBitmapImage(__global uchar4 *PM_dataIn, __global float4 *PM_dataOut) {\n"
	"	int PM_gid = get_global_id(0);\n"
	"	uchar4 PM_in = PM_dataIn[PM_gid];\n"
	"	float4 PM_out;\n"
	"	PM_out.s0 = (float) PM_in.s0;\n"
	"	PM_out.s1 = (float) PM_in.s1;\n"
	"	PM_out.s2 = (float) PM_in.s2;\n"
	"	PM_out.s3 = 0.0f;\n"
	"	PM_dataOut[PM_gid] = PM_out;\n"
	"}\n"
	"\n"
	"__kernel void foreach1(__global float4* PM_data) {\n"
	"	int PM_gid = get_global_id(0);\n"
	"	float4 pixel = PM_data[PM_gid]; \n"
	"				pixel.s0 /= 255;\n"
	"				pixel.s1 /= 255;\n"
	"				pixel.s2 /= 255;\n"
	"                float foo_r, foo_g, foo_b;\n"
	"                foo_r = foo_g = foo_b = 0.0f;\n"
	"                foo_r += 0.5141364f * pixel.s0;\n"
	"                foo_r += 0.3238786f * pixel.s1;\n"
	"                foo_r += 0.16036376f * pixel.s2;\n"
	"                foo_g += 0.265068f * pixel.s0;\n"
	"                foo_g += 0.67023428f * pixel.s1;\n"
	"                foo_g += 0.06409157f * pixel.s2;\n"
	"                foo_b += 0.0241188f * pixel.s0;\n"
	"                foo_b += 0.1228178f * pixel.s1;\n"
	"                foo_b += 0.84442666f * pixel.s2;\n"
	"                float w = foo_r + foo_g + foo_b;\n"
	"                if (w > 0.0f) {\n"
	"                    pixel.s0 = foo_g;\n"
	"                    pixel.s1 = foo_r / w;\n"
	"                    pixel.s2 = foo_g / w;\n"
	"                } else {\n"
	"                    pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;\n"
	"                }\n"
	"             PM_data[PM_gid] = pixel;\n"
	"\n"
	"}\n"
	"\n"
	"__kernel void foreach2(__global float4* PM_data) {\n"
	"	int PM_gid = get_global_id(0);\n"
	"	float4 pixel = PM_data[PM_gid]; \n"
	"                float xVal, zVal;\n"
	"                float yVal = pixel.s0;       \n"
	"\n"
	"                if (yVal > 0.0f && pixel.s1 > 0.0f && pixel.s2 > 0.0f) {\n"
	"                    xVal = pixel.s1 * yVal / pixel.s2;\n"
	"                    zVal = xVal / pixel.s1 - xVal - yVal;\n"
	"                } else {\n"
	"                    xVal = zVal = 0.0f;\n"
	"                }\n"
	"                pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;\n"
	"                pixel.s0 += 2.5651f * xVal;\n"
	"                pixel.s0 += -1.1665f * yVal;\n"
	"                pixel.s0 += -0.3986f * zVal;\n"
	"                pixel.s1 += -1.0217f * xVal;\n"
	"                pixel.s1 += 1.9777f * yVal;\n"
	"                pixel.s1 += 0.0439f * zVal;\n"
	"                pixel.s2 += 0.0753f * xVal;\n"
	"                pixel.s2 += -0.2543f * yVal;\n"
	"                pixel.s2 += 1.1892f * zVal;\n"
	"				pixel.s0 *= 255;\n"
	"				pixel.s1 *= 255;\n"
	"				pixel.s2 *= 255;\n"
	"             PM_data[PM_gid] = pixel;\n"
	"\n"
	"}\n"
	"\n"
	"__kernel void toBitmapBitmapImage(__global float4 *PM_dataIn, __global uchar4 *PM_dataOut) {\n"
	"	int PM_gid = get_global_id(0);\n"
	"	float4 PM_in = PM_dataIn[PM_gid];\n"
	"	uchar4 PM_out;\n"
	"	PM_out.x = (uchar) PM_in.s0;\n"
	"	PM_out.y = (uchar) PM_in.s1;\n"
	"	PM_out.z = (uchar) PM_in.s2;\n"
	"	PM_out.w = 255;\n"
	"	PM_dataOut[PM_gid] = PM_out;\n"
	"}\n"
	"\n";
#endif