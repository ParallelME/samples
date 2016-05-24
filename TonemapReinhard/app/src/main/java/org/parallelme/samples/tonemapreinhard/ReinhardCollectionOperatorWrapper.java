/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 * Code created automatically by ParallelME compiler.
 */

package org.parallelme.samples.tonemapreinhard;

import android.graphics.Bitmap;

public interface ReinhardCollectionOperatorWrapper {
	public boolean isValid();

	public void inputBind1(byte[] data, int width, int height);

	public void iterator1();

	public void iterator2(float[] sum, float[] max);

	public void iterator3(float fScaleFactor, float fLmax2);

	public void iterator4();

	public void iterator5(float power);

	public void outputBind1(Bitmap bitmap);

	public int getHeight1();

	public int getWidth2();
}
