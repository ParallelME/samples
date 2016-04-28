package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import org.parallelme.userlibrary.image.RGBE;

public class ReinhardOpenCLOperatorGPU implements ReinhardOperator {
    /// Stores a pointer to the OpenCL tonemapping state.
    private long tonemapperPtr;

    private native long init();
    private native void cleanUp(long tonemapperPtr);
    public native void nativeRunOp(long tonemapperPtr, int width, int height, byte[] data, float key, float power, Bitmap bitmap);

    public void runOp(RGBE.ResourceData resourceData, float key, float gamma, Bitmap bitmap) {
        nativeRunOp(tonemapperPtr, resourceData.width, resourceData.height, resourceData.data, key, gamma, bitmap);
    }

    public void waitFinish() {
        // Do nothing.
    }

    ReinhardOpenCLOperatorGPU() {
        tonemapperPtr = init();
    }

    public boolean inited() {
        return tonemapperPtr != 0;
    }

    protected void finalize() throws Throwable {
        try {
            if(tonemapperPtr != 0)
                cleanUp(tonemapperPtr);
        } catch(Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }
    }

    static {
        System.loadLibrary("TonemapReinhard");
    }
}
