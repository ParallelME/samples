package org.parallelme.samples.arraytest;

import android.util.Log;
import android.widget.TextView;

import org.parallelme.userlibrary.Array;
import org.parallelme.userlibrary.datatypes.Int32;
import org.parallelme.userlibrary.function.Foreach;

import java.util.Locale;

public class ArrayUserTest {
	private int varTeste;

	public void method(TextView t) {
		int i = 0;
		int[] tmp = new int[4];
		for (int x = 0; x < tmp.length; x++) {
			tmp[x] = ++i;
		}
		Array<Int32> array = new Array<Int32>(tmp, Int32.class);
        varTeste = 0;
		array.foreach(new Foreach<Int32>() {
			@Override
			public void function(Int32 element) {
				varTeste += 1;
				element.value = element.value + varTeste;
			}
		});
		array.toJavaArray(tmp);
        t.setText(String.format(Locale.getDefault(),
                "%s\n ArrayUserTest: %d | %d %d %d %d", t.getText(), varTeste, tmp[0], tmp[1], tmp[2], tmp[3]));

		//Int32 result = array.reduce(new Reduce<Int32>() {
		//	@Override
		//	public Int32 function(Int32 element1, Int32 element2) {
		//		element1.value += 10;
		//		return element1;
		//	}
		//});
	}
}
