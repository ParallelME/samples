/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.samples.tonemapreinhard;

import android.support.v8.renderscript.*;
import android.graphics.Bitmap;
import org.parallelme.userlibrary.image.RGBE;

public class ReinhardRenderScriptOperator implements ReinhardOperator {
    RenderScript mRS;
    Allocation mDataAllocation;
    Allocation mImageAllocation;
    int mWidth, mHeight;

    ScriptC_tonemapper mTonemapperScript;

    private void createAllocations(RGBE.ResourceData resourceData) {
        mWidth = resourceData.width;
        mHeight = resourceData.height;

        Type imageType = new Type.Builder(mRS, Element.RGBA_8888(mRS))
                .setX(mWidth)
                .setY(mHeight)
                .create();
        Type dataType = new Type.Builder(mRS, Element.F32_4(mRS))
                .setX(mWidth)
                .setY(mHeight)
                .create();

        mImageAllocation = Allocation.createTyped(mRS, imageType,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        mDataAllocation = Allocation.createTyped(mRS, dataType);
        mImageAllocation.copyFrom(resourceData.data);

        mTonemapperScript.forEach_to_float(mImageAllocation, mDataAllocation);
    }

    public void toBitmap(Bitmap bitmap, float power) {
        mTonemapperScript.set_toBitmapPower(power);
        mTonemapperScript.forEach_to_bitmap(mDataAllocation, mImageAllocation);

        mImageAllocation.copyTo(bitmap);
    }

    public void toYxy() {
        mTonemapperScript.forEach_to_yxy(mDataAllocation, mDataAllocation);
    }

    public void toRgb() {
        mTonemapperScript.forEach_to_rgb(mDataAllocation, mDataAllocation);
    }

    public void logAverage(float key) {
        Script.LaunchOptions scLine = new Script.LaunchOptions();
        scLine.setX(0, 1);
        scLine.setY(0, mDataAllocation.getType().getY());

        mTonemapperScript.set_lineLogAverageData(mDataAllocation);
        mTonemapperScript.set_lineLogAverageWidth(mDataAllocation.getType().getX());
        mTonemapperScript.forEach_line_log_average(mDataAllocation, mDataAllocation, scLine);

        Script.LaunchOptions scLog = new Script.LaunchOptions();
        scLog.setX(0, 1);
        scLog.setY(0, 1);

        mTonemapperScript.set_logAverageData(mDataAllocation);
        mTonemapperScript.set_logAverageWidth(mDataAllocation.getType().getX());
        mTonemapperScript.set_logAverageHeight(mDataAllocation.getType().getY());
        mTonemapperScript.set_logAverageKey(key);
        mTonemapperScript.forEach_log_average(mDataAllocation, mDataAllocation, scLog);
    }
    public void tonemap() {
        mTonemapperScript.set_tonemapData(mDataAllocation);
        mTonemapperScript.forEach_tonemap(mDataAllocation, mDataAllocation);
    }

    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap) {
        createAllocations(resourceData);

        toYxy();
        logAverage(key);
        tonemap();
        toRgb();
        toBitmap(bitmap, power);

        mImageAllocation = null;
        mDataAllocation = null;
    }

    public void waitFinish() {
        // Do nothing.
    }

    ReinhardRenderScriptOperator(RenderScript rs) {
        mRS = rs;
        mTonemapperScript = new ScriptC_tonemapper(mRS);
    }
}
