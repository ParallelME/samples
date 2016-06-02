package org.parallelme.samples.grayscale;

import android.graphics.Bitmap;

/**
 * Runs the grayscale operator using the ParallelME Runtime.
 * @author Renato Utsch
 */
public class GrayscaleOperator {
    private long dataPointer = nativeInit();

    private native long nativeInit();
    private native void nativeCleanUp(long dataPointer);
    private native void nativeGrayscale(long dataPointer, Bitmap bitmap, int width, int height);

    @Override
    protected void finalize() throws Throwable {
        nativeCleanUp(dataPointer);
        super.finalize();
    }

    public void grayscale(Bitmap image) {
        nativeGrayscale(dataPointer, image, image.getWidth(), image.getHeight());
    }

    static {
        System.loadLibrary("Grayscale");
    }
}
