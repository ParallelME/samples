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

/**
 * Interface for the different Tonemap Reinhard algorithm implementations.
 *
 * @author Renato Utsch
 */
public interface ReinhardOperator {
    void runOp(RGBE.ResourceData resourceData, float key, float power, Bitmap bitmap);
    void waitFinish();
}
