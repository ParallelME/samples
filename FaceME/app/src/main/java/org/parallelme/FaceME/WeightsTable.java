package org.parallelme.FaceME;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The weights table holds within itself the biases of the network. Biases were input along with the weights, in the same matrix, to avoid extra I/O
 * operations.
 * Given an image 28x28, or example, operations will be:
 *
 * Input -> 785 columns: 28*28 for the pixels, +1 for the bias, initialized as 1
 * Layer1 -> 901 columns, for 900 nodes +1 for the bias, initialized as 1
 * Weights table -> 785x901; Each column represents all the weights that converge to a single node in the next layer, and each row represent the
 *                  weights that leave a node in the previous layer.
 *                  Here's the trick: the bias for the input layer is on row 785. It's values are always multiplied by the 1 in the 785th layer and
 *                  added to the result. Layer1 has it's own bias column, having therefore 901 columns. But the weights input refer only to the 900
 *                  nodes of the layer. For the operation to work, we add an extra column in the table, and initialize it as 0, so it won't change
 *                  the final result. Therefore we do the operation with the 785*901 weights table and everything works.
 *
 * @author AlbertoSCA
 * @since 09/12/2015.
 */
public class WeightsTable {
    private float[][] W;
    private int w, h;
    public WeightsTable(int _w, int _h){
        w = _w;
        h = _h;
        W = new float[_w][_h];
        for(int i = 0; i < _w; i++){
            for(int j = 0; j < _h; j++){
                W[i][j] = 0;
            }
        }
    }
    public void setField(float _v, int _w, int _h){
        W[_w][_h] = _v;
    }

    public float getField(int _w, int _h){
        return W[_w][_h];
    }

    public float[] getColumn(int _w){return W[_w];}

    public void readAndInit(String _filename, AssetManager _assets) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(_assets.open(_filename), "UTF-8"));

        String mLine;
        String[] aLine = {""};
        for (int i = 0; i < w; i++) {
            while ((mLine = in.readLine()) != null) {
                aLine = mLine.split("\\D+");//"\\D"//("\\s+");
                for (int j = 2; j < h-1; j++) { //-1 goes because the array has an extra slot reserved for the biases for the network
                    //Log.d("WeightsParsing", "i: " + i + " j: " + j);
                    W[i][j] = Float.parseFloat(aLine[j]);;
                }
                i++;
            }
        }
    }
}
