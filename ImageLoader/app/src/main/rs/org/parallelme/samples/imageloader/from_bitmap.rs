/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#pragma version(1)
#pragma rs java_package_name(org.parallelme.samples.imageloader)

typedef struct __attribute__((packed, aligned(4))) ABCD {
    float3 abcd;
} ABCD_t;
ABCD_t *abcd;

ABCD_t __attribute__((kernel)) root(ABCD_t pixel, uint32_t x, uint32_t y) {
    return pixel;
}
