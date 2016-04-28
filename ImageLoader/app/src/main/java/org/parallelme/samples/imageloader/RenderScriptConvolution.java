/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.samples.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

/**
 * @author Pedro Caldeira
 */
public class RenderScriptConvolution {

    private RenderScript mRS;

    public RenderScriptConvolution(RenderScript mRS){
        this.mRS = mRS;
    }

    public Bitmap convolute(Resources res, int resource){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        Allocation mInAllocation = Allocation.createFromBitmap(this.mRS, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);

        Allocation mOutAllocation = Allocation.createTyped(this.mRS, mInAllocation.getType());

        ScriptC_convolution mScript = new ScriptC_convolution(mRS, res, R.raw.convolution);

        mScript.set_in(mInAllocation);
        mScript.set_a_out(mOutAllocation);
        mScript.set_script(mScript);
        mScript.invoke_convolute(bitmap.getWidth(), bitmap.getHeight());
        mOutAllocation.copyTo(bitmap);

        return bitmap;
    }
}