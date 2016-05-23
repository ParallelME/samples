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
	boolean isValid();

	void inputBind1(byte[] data, int width, int height);

	void iterator1();

	void iterator2(float[] sum, float[] max);

	void iterator3(float fScaleFactor, float fLmax2);

	void iterator4();

	void iterator5(float power);

	void outputBind1(Bitmap bitmap);

	int getHeight1();

	int getWidth2();
}
