package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import org.parallelme.userlibrary.image.RGBE;

public interface ReinhardOperator {
    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap);
    public void waitFinish();
}
