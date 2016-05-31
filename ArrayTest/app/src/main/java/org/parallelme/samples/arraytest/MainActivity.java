package org.parallelme.samples.arraytest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v8.renderscript.RenderScript;
import android.view.View;
import android.widget.TextView;

import org.parallelme.ParallelMERuntime;

public class MainActivity extends Activity {
    private ArrayUserTest mArrayUserTest;
    private ArrayTest mArrayTest;
    private TextView mText;

    public void runTests(View view) {
        mArrayUserTest.method(mText);
        mArrayTest.method(mText);
        /*
        long result = ParallelMERuntime.getInstance().createArray(new int[2]);
        int[] arr = new int[2];
        ParallelMERuntime.getInstance().toArray(result, arr);
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView) findViewById(R.id.text);
        mArrayUserTest = new ArrayUserTest();
        mArrayTest = new ArrayTest(RenderScript.create(this));
    }
}
