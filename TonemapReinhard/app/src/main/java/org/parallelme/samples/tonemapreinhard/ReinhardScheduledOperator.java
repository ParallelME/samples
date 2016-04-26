package org.parallelme.samples.tonemapreinhard;

import android.content.Context;
import android.graphics.Bitmap;
import br.ufmg.dcc.parallelme.userlibrary.image.RGBE;

public class ReinhardScheduledOperator implements ReinhardOperator {
    private long tonemapperPtr;

    private native long init(Context ctx, String scriptc);
    private native void cleanUp(long tonemapperPtr);
    private native void nativeRunOp(long tonemapperPtr, int width, int height, byte[] data, float key, float power, Bitmap bitmap);
    private native void nativeWaitFinish(long tonemapperPtr);

    public void runOp(RGBE.ResourceData resourceData, float key, float gamma, Bitmap bitmap) {
        nativeRunOp(tonemapperPtr, resourceData.width, resourceData.height, resourceData.data, key, gamma, bitmap);
    }

    public void waitFinish() {
        nativeWaitFinish(tonemapperPtr);
    }

    // Returns true in case the tonemapper was initialized successfully.
    public boolean inited() {
        return tonemapperPtr != 0;
    }

    ReinhardScheduledOperator(Context ctx) {
        tonemapperPtr = init(ctx, "org/parallelme/samples/tonemapreinhard/ScriptC_tonemapper");
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
