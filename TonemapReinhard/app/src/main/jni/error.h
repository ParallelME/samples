/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

#ifndef ERROR_H
#define ERROR_H

// Define this to replace the program name printed in logcat on android.
#ifndef PROGRAM_NAME
#   define PROGRAM_NAME "ReinhardOpenCLOperator"
#endif

#ifdef __ANDROID__ // use __android_log_print
#   include <android/log.h>
#   include <stdio.h>
#   define ERROR_PRINTER_FUNC(...) __android_log_print(ANDROID_LOG_ERROR, PROGRAM_NAME, __VA_ARGS__)
#else // use fprintf
#   include <stdio.h>
#   define ERROR_PRINTER_FUNC(...) fprintf(stderr, __VA_ARGS__)
#endif

#define printError(...) do { ERROR_PRINTER_FUNC("%s:%d:%s: ", __FILE__, __LINE__, __func__); ERROR_PRINTER_FUNC(__VA_ARGS__); ERROR_PRINTER_FUNC("\n"); fflush(stdout); } while(0)

#include <stdlib.h>

/**
 * Stops the program if the condition given is false.
 * You can write an error message in the same style as printf(), but to stderr.
 */
#define stop_if(cond, ...) do { if(cond) { printError(__VA_ARGS__); exit(1); } } while(0)

/**
 * Returns (for void-returning functions) if the condition given is false.
 * You can write an error message in the same style as printf(), but to stderr.
 */
#define return_if(cond, ...) do { if(cond) { printError(__VA_ARGS__); return; } } while(0)

#endif // !ERROR_H
