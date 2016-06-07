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
import org.parallelme.userlibrary.image.Pixel;

public interface ReinhardCompilerOperatorWrapper {
	boolean isValid();

	void inputBind1(byte[] data, int width, int height);

	void foreach1();

	Pixel reduce2();

	void foreach3(float fScaleFactor, float fLmax2);

	void foreach4();

	void foreach5(float power);

	void outputBind1(Bitmap bitmap);

	int getHeight1();

	int getWidth2();
}
