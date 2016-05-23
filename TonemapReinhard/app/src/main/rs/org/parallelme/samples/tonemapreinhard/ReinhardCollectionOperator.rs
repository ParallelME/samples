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
float4 __attribute__((kernel)) toFloatHDRImage(uchar4 $in, uint32_t x, uint32_t y) {
	float4 $out;
	if ($in.s3 != 0) {
		float f = ldexp(1.0f, ($in.s3 & 0xFF) - (128 + 8));
		$out.s0 = ($in.s0 & 0xFF) * f;
		$out.s1 = ($in.s1 & 0xFF) * f;
		$out.s2 = ($in.s2 & 0xFF) * f;
	} else {
		$out.s0 = 0.0f;
		$out.s1 = 0.0f;
		$out.s2 = 0.0f;
	}
	return $out;
}

float4 __attribute__((kernel)) iterator1(float4 pixel, uint32_t x, uint32_t y) {
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
rs_allocation $gInputImageIterator2;
rs_allocation $gOutputSumIterator2;
rs_allocation $gOutputMaxIterator2;
int $gInputXSizeIterator2;
int $gInputYSizeIterator2;
float $gSumIterator2;
float $gMaxIterator2;

void iterator2() 
 {
	float4 pixel;
	for (int $x = 0; $x < $gInputXSizeIterator2; $x++) {
		for (int $y = 0; $y < $gInputYSizeIterator2; $y++) {
			pixel = rsGetElementAt_float4($gInputImageIterator2, $x, $y);
			$gSumIterator2 += log(0.00001f + pixel.s0);

			                if(pixel.s0 > $gMaxIterator2)
			                    $gMaxIterator2 = pixel.s0;
			rsSetElementAt_float4($gInputImageIterator2, pixel, $x, $y);
		}
	}
	rsSetElementAt_float($gOutputSumIterator2, $gSumIterator2, 0);
rsSetElementAt_float($gOutputMaxIterator2, $gMaxIterator2, 0);
}
float $gFScaleFactorIterator3;
float $gFLmax2Iterator3;

float4 __attribute__((kernel)) iterator3(float4 pixel, uint32_t x, uint32_t y) {
                
                pixel.s0 *= $gFScaleFactorIterator3;

                
                pixel.s0 *= (1.0f + pixel.s0 / $gFLmax2Iterator3) / (1.0f + pixel.s0);
            
	return pixel;
}

float4 __attribute__((kernel)) iterator4(float4 pixel, uint32_t x, uint32_t y) {
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
float $gPowerIterator5;

float4 __attribute__((kernel)) iterator5(float4 pixel, uint32_t x, uint32_t y) {
                
                if (pixel.s0 > 1.0f) pixel.s0 = 1.0f;
                if (pixel.s1 > 1.0f) pixel.s1 = 1.0f;
                if (pixel.s2 > 1.0f) pixel.s2 = 1.0f;

                pixel.s0 = (float) pow(pixel.s0, $gPowerIterator5);
                pixel.s1 = (float) pow(pixel.s1, $gPowerIterator5);
                pixel.s2 = (float) pow(pixel.s2, $gPowerIterator5);
            
	return pixel;
}
uchar4 __attribute__((kernel)) toBitmapHDRImage(float4 $in, uint32_t x, uint32_t y) {
	uchar4 $out;
	$out.r = (uchar) ($in.s0 * 255.0f);
	$out.g = (uchar) ($in.s1 * 255.0f);
	$out.b = (uchar) ($in.s2 * 255.0f);
	$out.a = 255;
	return $out;
}