/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

#pragma version(1)
#pragma rs java_package_name(org.parallelme.samples.tonemapreinhard)
float4 __attribute__((kernel)) toFloatHDRImage(uchar4 PM_in, uint32_t x, uint32_t y) {
	float4 PM_out;
	if (PM_in.s3 != 0) {
		float f = ldexp(1.0f, (PM_in.s3 & 0xFF) - (128 + 8));
		PM_out.s0 = (PM_in.s0 & 0xFF) * f;
		PM_out.s1 = (PM_in.s1 & 0xFF) * f;
		PM_out.s2 = (PM_in.s2 & 0xFF) * f;
	} else {
		PM_out.s0 = 0.0f;
		PM_out.s1 = 0.0f;
		PM_out.s2 = 0.0f;
	}
	return PM_out;
}

float4 __attribute__((kernel)) foreach1(float4 pixel, uint32_t x, uint32_t y) {
                float result0, result1, result2;
                float w;

                result0 = result1 = result2 = 0.0f;
                result0 += 0.5141364f * pixel.s0;
                result0 += 0.3238786f * pixel.s1;
                result0 += 0.16036376f * pixel.s2;
                result1 += 0.265068f * pixel.s0;
                result1 += 0.67023428f * pixel.s1;
                result1 += 0.06409157f * pixel.s2;
                result2 += 0.0241188f * pixel.s0;
                result2 += 0.1228178f * pixel.s1;
                result2 += 0.84442666f * pixel.s2;
                w = result0 + result1 + result2;
                if (w > 0.0) {
                    pixel.s0 = result1;
                    pixel.s1 = result0 / w;
                    pixel.s2 = result1 / w;
                } else {
                    pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;
                }
            
	return pixel;
}
rs_allocation PM_gInputImageForeach2;
rs_allocation PM_gOutputSumForeach2;
rs_allocation PM_gOutputMaxForeach2;
int PM_gInputXSizeForeach2;
int PM_gInputYSizeForeach2;
float PM_gSumForeach2;
float PM_gMaxForeach2;

void foreach2() 
 {
	float4 pixel;
	for (int PM_x = 0; PM_x < PM_gInputXSizeForeach2; PM_x++) {
		for (int PM_y = 0; PM_y < PM_gInputYSizeForeach2; PM_y++) {
			pixel = rsGetElementAt_float4(PM_gInputImageForeach2, PM_x, PM_y);
			PM_gSumForeach2 += log(0.00001f + pixel.s0);

			                if(pixel.s0 > PM_gMaxForeach2)
			                    PM_gMaxForeach2 = pixel.s0;
			rsSetElementAt_float4(PM_gInputImageForeach2, pixel, PM_x, PM_y);
		}
	}
	rsSetElementAt_float(PM_gOutputSumForeach2, PM_gSumForeach2, 0);
rsSetElementAt_float(PM_gOutputMaxForeach2, PM_gMaxForeach2, 0);
}
float PM_gFScaleFactorForeach3;
float PM_gFLmax2Foreach3;

float4 __attribute__((kernel)) foreach3(float4 pixel, uint32_t x, uint32_t y) {
                
                pixel.s0 *= PM_gFScaleFactorForeach3;

                
                pixel.s0 *= (1.0f + pixel.s0 / PM_gFLmax2Foreach3) / (1.0f + pixel.s0);
            
	return pixel;
}

float4 __attribute__((kernel)) foreach4(float4 pixel, uint32_t x, uint32_t y) {
                float _x, _y, _z, g, b;

                _y = pixel.s0;    
                g = pixel.s1;   
                b = pixel.s2;    

                if (_y > 0.0f && g > 0.0f && b > 0.0f) {
                    _x = g * _y / b;
                    _z = _x / g - _x - _y;
                } else {
                    _x = _z = 0.0f;
                }

                
                pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;
                pixel.s0 += 2.5651f * _x;
                pixel.s0 += -1.1665f * _y;
                pixel.s0 += -0.3986f * _z;
                pixel.s1 += -1.0217f * _x;
                pixel.s1 += 1.9777f * _y;
                pixel.s1 += 0.0439f * _z;
                pixel.s2 += 0.0753f * _x;
                pixel.s2 += -0.2543f * _y;
                pixel.s2 += 1.1892f * _z;
            
	return pixel;
}
float PM_gPowerForeach5;

float4 __attribute__((kernel)) foreach5(float4 pixel, uint32_t x, uint32_t y) {
                
                if (pixel.s0 > 1.0f) pixel.s0 = 1.0f;
                if (pixel.s1 > 1.0f) pixel.s1 = 1.0f;
                if (pixel.s2 > 1.0f) pixel.s2 = 1.0f;

                pixel.s0 = (float) pow(pixel.s0, PM_gPowerForeach5);
                pixel.s1 = (float) pow(pixel.s1, PM_gPowerForeach5);
                pixel.s2 = (float) pow(pixel.s2, PM_gPowerForeach5);
            
	return pixel;
}
uchar4 __attribute__((kernel)) toBitmapHDRImage(float4 PM_in, uint32_t x, uint32_t y) {
	uchar4 PM_out;
	PM_out.r = (uchar) (PM_in.s0 * 255.0f);
	PM_out.g = (uchar) (PM_in.s1 * 255.0f);
	PM_out.b = (uchar) (PM_in.s2 * 255.0f);
	PM_out.a = 255;
	return PM_out;
}