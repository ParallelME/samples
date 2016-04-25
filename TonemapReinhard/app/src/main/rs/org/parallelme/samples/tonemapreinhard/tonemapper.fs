#pragma version(1)
#pragma rs java_package_name(org.parallelme.samples.tonemapreinhard)

float4 __attribute__((kernel)) to_float(uchar4 in, uint32_t x, uint32_t y) {
    float4 out;
    float f;

    if(in.s3 != 0) {
        f = ldexp(1.0f, (in.s3 & 0xFF) - (128 + 8));
        out.s0 = (in.s0 & 0xFF) * f;
        out.s1 = (in.s1 & 0xFF) * f;
        out.s2 = (in.s2 & 0xFF) * f;
    }
    else {
        out.s0 = 0.0f;
        out.s1 = 0.0f;
        out.s2 = 0.0f;
    }

    return out;
}

float4 __attribute__((kernel)) to_yxy(float4 in, uint32_t x, uint32_t y) {
    float4 result, out;

    // These constants are the conversion coefficients.
    result.s0 = result.s1 = result.s2 = 0.0f;
    result.s0 += 0.5141364f * in.s0;
    result.s0 += 0.3238786f * in.s1;
    result.s0 += 0.16036376f * in.s2;
    result.s1 += 0.265068f * in.s0;
    result.s1 += 0.67023428f * in.s1;
    result.s1 += 0.06409157f * in.s2;
    result.s2 += 0.0241188f * in.s0;
    result.s2 += 0.1228178f * in.s1;
    result.s2 += 0.84442666f * in.s2;
    float w = result.s0 + result.s1 + result.s2;

    if(w > 0.0f) {
        out.s0 = result.s1;     // Y
        out.s1 = result.s0 / w; // x
        out.s2 = result.s1 / w; // y
    }
    else {
        out = 0.0f;
    }

    return out;
}

// Calculates the sum of the entire column.
rs_allocation lineLogAverageData;
int lineLogAverageWidth;
float4 __attribute__((kernel)) line_log_average(float4 in, uint32_t x, uint32_t y) {
    float sum = 0.0f;
    float max = 0.0f;

    for(int i = 0; i < lineLogAverageWidth; ++i) {
        float4 val = rsGetElementAt_float4(lineLogAverageData, i, y);
        sum += log(0.00001f + val.s0);

        if(val.s0 > max)
            max = val.s0;
    }

    float4 d1 = rsGetElementAt_float4(lineLogAverageData, 1, y);
    d1.s3 = max;
    rsSetElementAt_float4(lineLogAverageData, d1, 1, y);

    in.s3 = sum;
    return in;
}

rs_allocation logAverageData;
float logAverageKey;
int logAverageWidth;
int logAverageHeight;
float4 __attribute__((kernel)) log_average(float4 in, uint32_t x, uint32_t y)  {
    float4 f;
    float sum = 0.0f;
    float max = 0.0f;

    for(int i = 0; i < logAverageHeight; ++i) {
        float4 d0 = rsGetElementAt_float4(logAverageData, 0, i);
        float4 d1 = rsGetElementAt_float4(logAverageData, 1, i);

        sum += d0.s3;
        if(d1.s3 > max)
            max = d1.s3;
    }

    // Calculate the scale factor.
    float average = exp(sum / (float)(logAverageWidth * logAverageHeight));
    float scaleFactor = logAverageKey * (1.0f / average);

    // Get where the data will be saved.
    //float4 d0 = rsGetElementAt_float4(logAverageData, 0, 0);
    float4 d1 = rsGetElementAt_float4(logAverageData, 0, 1);

    // Save scale factor on logAverageData[0][0].s3.
    in.s3 = scaleFactor;
    //rsSetElementAt_float4(logAverageData, d0, 0, 0);

    // Save lmax2  on logAverageData[0][1].s3.
    d1.s3 = pow(max * scaleFactor, 2);
    rsSetElementAt_float4(logAverageData, d1, 0, 1);

    return in;
}

rs_allocation tonemapData;
float4 __attribute__((kernel)) tonemap(float4 in, uint32_t x, uint32_t y) {
    // Scale to midtones.
    // gData[0][0].s3 contains the scale factor.
    float4 d0 = rsGetElementAt_float4(tonemapData, 0, 0);
    in.s0 *= d0.s3;

    // Tonemap.
    // gData[0][1].s3 contains lmax2.
    float4 d1 = rsGetElementAt_float4(tonemapData, 0, 1);
    in.s0 *= (1.0f + in.s0 / d1.s3) / (1.0f + in.s0);

    return in;
}

float4 __attribute__((kernel)) to_rgb(float4 in, uint32_t x, uint32_t y) {
    float4 result, val, out;

    val.y = in.s0;     // Y
    result.s1 = in.s1; // x
    result.s2 = in.s2; // y

    if(val.y > 0.0f && result.s1 > 0.0f && result.s2 > 0.0f) {
        val.x = result.s1 * val.y / result.s2;
        val.z = val.x / result.s1 - val.x - val.y;
    }
    else {
        val.x = val.z = 0.0f;
    }

    // These constants are the conversion coefficients.
    out.s0 = out.s1 = out.s2 = 0.0f;
    out.s0 += 2.5651f * val.x;
    out.s0 += -1.1665f * val.y;
    out.s0 += -0.3986f * val.z;
    out.s1 += -1.0217f * val.x;
    out.s1 += 1.9777f * val.y;
    out.s1 += 0.0439f * val.z;
    out.s2 += 0.0753f * val.x;
    out.s2 += -0.2543f * val.y;
    out.s2 += 1.1892f * val.z;

    return out;
}

float toBitmapPower;
uchar4 __attribute__((kernel)) to_bitmap(float4 in, uint32_t x, uint32_t y) {
    uchar4 out;

    // Clamp.
    if(in.s0 > 1.0f) in.s0 = 1.0f;
    if(in.s0 < 0.0f) in.s0 = 0.0f;
    if(in.s1 > 1.0f) in.s1 = 1.0f;
    if(in.s1 < 0.0f) in.s1 = 0.0f;
    if(in.s2 > 1.0f) in.s2 = 1.0f;
    if(in.s2 < 0.0f) in.s2 = 0.0f;

    out.r = (uchar) (255.0f * pow(in.s0, toBitmapPower));
    out.g = (uchar) (255.0f * pow(in.s1, toBitmapPower));
    out.b = (uchar) (255.0f * pow(in.s2, toBitmapPower));
    out.a = 255;
    return out;
}
