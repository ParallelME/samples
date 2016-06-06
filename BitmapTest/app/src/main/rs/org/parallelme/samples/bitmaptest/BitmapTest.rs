/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#pragma version(1)
#pragma rs java_package_name(org.parallelme.samples.bitmaptest)
float4 __attribute__((kernel)) toFloatBitmapImage(uchar4 PM_in, uint32_t x, uint32_t y) {
	float4 PM_out;
	PM_out.s0 = (float) PM_in.r;
	PM_out.s1 = (float) PM_in.g;
	PM_out.s2 = (float) PM_in.b;
	return PM_out;
}

float4 __attribute__((kernel)) foreach1(float4 pixel, uint32_t x, uint32_t y) {
				pixel.s0 /= 255;
				pixel.s1 /= 255;
				pixel.s2 /= 255;
                float foo_r, foo_g, foo_b;
                foo_r = foo_g = foo_b = 0.0f;
                foo_r += 0.5141364f * pixel.s0;
                foo_r += 0.3238786f * pixel.s1;
                foo_r += 0.16036376f * pixel.s2;
                foo_g += 0.265068f * pixel.s0;
                foo_g += 0.67023428f * pixel.s1;
                foo_g += 0.06409157f * pixel.s2;
                foo_b += 0.0241188f * pixel.s0;
                foo_b += 0.1228178f * pixel.s1;
                foo_b += 0.84442666f * pixel.s2;
                float w = foo_r + foo_g + foo_b;
                if (w > 0.0f) {
                    pixel.s0 = foo_g;
                    pixel.s1 = foo_r / w;
                    pixel.s2 = foo_g / w;
                } else {
                    pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;
                }
            
	return pixel;
}

float4 __attribute__((kernel)) foreach2(float4 pixel, uint32_t x, uint32_t y) {
                float xVal, zVal;
                float yVal = pixel.s0;

                if (yVal > 0.0f && pixel.s1 > 0.0f && pixel.s2 > 0.0f) {
                    xVal = pixel.s1 * yVal / pixel.s2;
                    zVal = xVal / pixel.s1 - xVal - yVal;
                } else {
                    xVal = zVal = 0.0f;
                }
                pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;
                pixel.s0 += 2.5651f * xVal;
                pixel.s0 += -1.1665f * yVal;
                pixel.s0 += -0.3986f * zVal;
                pixel.s1 += -1.0217f * xVal;
                pixel.s1 += 1.9777f * yVal;
                pixel.s1 += 0.0439f * zVal;
                pixel.s2 += 0.0753f * xVal;
                pixel.s2 += -0.2543f * yVal;
                pixel.s2 += 1.1892f * zVal;
				pixel.s0 *= 255;
				pixel.s1 *= 255;
				pixel.s2 *= 255;
            
	return pixel;
}
uchar4 __attribute__((kernel)) toBitmapBitmapImage(float4 PM_in, uint32_t x, uint32_t y) {
	uchar4 PM_out;
	PM_out.r = (uchar) (PM_in.s0);
	PM_out.g = (uchar) (PM_in.s1);
	PM_out.b = (uchar) (PM_in.s2);
	PM_out.a = 255;
	return PM_out;
}