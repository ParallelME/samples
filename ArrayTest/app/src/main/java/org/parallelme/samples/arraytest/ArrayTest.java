package org.parallelme.samples.arraytest;

import org.parallelme.userlibrary.datatypes.Int32;
import org.parallelme.userlibrary.function.Foreach;

import android.support.v8.renderscript.*;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class ArrayTest {
	private ArrayTestWrapper PM_parallelME;
	private int varTeste;

	public ArrayTest(RenderScript PM_mRS) {
		this.PM_parallelME = new ArrayTestWrapperImplPM();
		if (!this.PM_parallelME.isValid())
			this.PM_parallelME = new ArrayTestWrapperImplRS(PM_mRS);
	}

	public void method(TextView t) {
		int i = 0;
		int[] tmp = new int[4];
		for (int x = 0; x < tmp.length; x++) {
			tmp[x] = ++i;
		}
		PM_parallelME.inputBind1(tmp);
		varTeste = 0;
		int[] PM_varTeste = new int[1];
PM_varTeste[0] = varTeste;
PM_parallelME.foreach1(PM_varTeste);
varTeste = PM_varTeste[0];
		PM_parallelME.outputBind1(tmp);

		t.setText(String.format(Locale.getDefault(),
				"%s\n ArrayTest: %d | %d %d %d %d", t.getText(), varTeste, tmp[0], tmp[1], tmp[2], tmp[3]));

		
		
		
		
	}
}
