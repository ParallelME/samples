/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;
import org.parallelme.userlibrary.image.RGBE;

public interface ReinhardOperator {
    public void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap);
    public void waitFinish();
}
