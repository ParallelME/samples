package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;

import org.parallelme.userlibrary.image.RGBE;

public class ParallelMEReinhardCompilerOperatorCL implements ParallelMEReinhardCompilerOperator {
    private long mPtr; // Stores the native implementation pointer.
    private native long nativeInit();
    private native void nativeCleanUp(long ptr);
    private native void nativeCreateHDRImage(long ptr, byte[] data, int width, int height);
    private native void nativeToBitmap(long ptr, Bitmap bitmap);
    private native int nativeGetHeight(long ptr);
    private native int nativeGetWidth(long ptr);
    private native void nativeIterator1(long ptr);
    private native void nativeSetSumIterator2(long ptr, float sum);
    private native void nativeSetMaxIterator2(long ptr, float max);
    private native void nativeIterator2(long ptr);
    private native float nativeGetSumIterator2(long ptr);
    private native float nativeGetMaxIterator2(long ptr);
    private native void nativeSetScaleFactorIterator3(long ptr, final float scaleFactor);
    private native void nativeSetLmax2Iterator3(long ptr, final float lmax2);
    private native void nativeIterator3(long ptr);
    private native void nativeIterator4(long ptr);
    private native void nativeSetPowerIterator5(long ptr, final float power);
    private native void nativeIterator5(long ptr);

    public ParallelMEReinhardCompilerOperatorCL() {
        mPtr = nativeInit();
    }

    protected void finalize() throws Throwable {
        try {
            nativeCleanUp(mPtr);
            mPtr = 0;
        } catch(Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }
    }

    static {
        System.loadLibrary("TonemapReinhard");
    }

    public boolean valid() {
        return mPtr != 0;
    }

    public void createHDRImage(RGBE.ResourceData imageResourceData) {
        nativeCreateHDRImage(mPtr, imageResourceData.data, imageResourceData.width,
                imageResourceData.height);
    }

    public void toBitmap(Bitmap bitmap) {
        nativeToBitmap(mPtr, bitmap);
    }

    public int getHeight() {
        return nativeGetHeight(mPtr);
    }

    public int getWidth() {
        return nativeGetWidth(mPtr);
    }

    public void iterator1() {
        nativeIterator1(mPtr);
    }

    public void setSumIterator2(float sum) {
        nativeSetSumIterator2(mPtr, sum);
    }

    public void setMaxIterator2(float max) {
        nativeSetMaxIterator2(mPtr, max);
    }

    public void iterator2() {
        nativeIterator2(mPtr);
    }

    public float getSumIterator2() {
        return nativeGetSumIterator2(mPtr);
    }

    public float getMaxIterator2() {
        return nativeGetMaxIterator2(mPtr);
    }

    public void setScaleFactorIterator3(final float scaleFactor) {
        nativeSetScaleFactorIterator3(mPtr, scaleFactor);
    }

    public void setLmax2Iterator3(final float lmax2) {
        nativeSetLmax2Iterator3(mPtr, lmax2);
    }

    public void iterator3() {
        nativeIterator3(mPtr);
    }

    public void iterator4() {
        nativeIterator4(mPtr);
    }

    public void setPowerIterator5(final float power) {
        nativeSetPowerIterator5(mPtr, power);
    }

    public void iterator5() {
        nativeIterator5(mPtr);
    }
}
