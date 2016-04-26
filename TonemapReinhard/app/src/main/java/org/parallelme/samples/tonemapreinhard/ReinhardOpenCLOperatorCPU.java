package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import br.ufmg.dcc.parallelme.userlibrary.image.RGBE;

public class ReinhardOpenCLOperatorCPU implements ReinhardOperator {
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

    // Returns true in case the tonemapper was initialized successfully.
    public boolean inited() {
       return tonemapperPtr != 0;
    }

    ReinhardOpenCLOperatorCPU() {
        tonemapperPtr = init();
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
