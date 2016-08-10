package org.parallelme.FaceME;

import org.parallelme.userlibrary.Array;
import org.parallelme.userlibrary.datatypes.Float32;
import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Reduce;
import org.parallelme.userlibrary.parallel.ParallelIterator;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
/**
 * Given a squared image, this class classifies it accordingly to the network parameters. These (number of layers, number of nodes per layer, layer's weights) are hardcoded, but can be easily changed for code adaptation.
 *
 * The network programmed here is a simple Stacked-Denoising-Autoencoder. Note that this class doesn't configure it, only takes fixed parameters and applies them to a image.
 *
 * @author AlbertoSCA
 * @date 25/11/2015.
 */
public class FaceRecognizer extends ContextWrapper {

    Bitmap face;

    public FaceRecognizer(Context base) {
        super(base);
    }

    /**
     * Resizes a given image to a squared format.
     * @param pic image to be resized
     * @param size size of the squared image's side, in pixels
     */
    public void resizeImage(Bitmap pic, int size){ //Image should be always square, for recognition purposes
        face = pic;
		/* Associate the Bitmap to the ImageView */
        face = Bitmap.createScaledBitmap(face, size, size, false);
    }

    /**
     * The pixels of the class image are, here, normalized and unified in a single value, averaged from red, green and blue.
     *
     * This is done because the format is necessary for the autoencoder to work properly.
     *
     * @param _w width of the image
     * @param _h height of the image
     * @return the array of treated pixels
     */
    private float[] getTreatedPixels(int _w, int _h){
        //Gets image pixels
        int[] pixels = new int[face.getWidth() * face.getHeight()];
        face.getPixels(pixels, 0, _w, 0, 0, _w, _h);

        //Decode the pixels from standard format and treats them
        float[] pixelsNormalized = new float[(_w*_h)];
        float R, G, B, T, M;
        int index;
        for (int i = 0; i < _h; i++){
            for (int j = 0; j < _w; j++)
            {
                index = i * _w + j;
                R = (float) (((pixels[index] >> 24) & 0xff)/255.0);     //bitwise shifting
                G = (float) (((pixels[index] >> 16) & 0xff)/255.0);
                B = (float) (((pixels[index] >> 8) & 0xff)/255.0);
                T = (float) ((pixels[index] & 0xff)/255.0);
                //Transforming 3 color values into 1 normalized one
                M = (float) ((R+G+B+T)/4.0);
                pixelsNormalized[index] = M;
            }
        }
        face.recycle(); //Freeing up memory
        return pixelsNormalized;
    }

    //Processes a layer operation in the neural network
    private static void mult(final Array<Float32> Input, final WeightsTable Weights,
                             final Array<Float32> Layer1, final WeightsTable Bias) {
        Layer1.par().foreach(new Foreach<Float32>() {
            @Override
            public void function(final Float32 eixLayer1) {
                Input.par().foreach(new Foreach<Float32>() {
                    @Override
                    public void function(final Float32 eixInput) {
                        //eixLayer1.value = eixLayer1.value + ((eixInput.value * Weights.getField(eixInput.x, eixLayer1.x))); //TODO: revive this and erase below when ParallelME support element index location and matrix multiplication
                        //TODO: must get these coordinates through the new library
                    }
                });
                //eixLayer1.value = eixLayer1.value + Bias.getField(0, eixLayer1.x);
                eixLayer1.value = (float) (1 / (1 + (Math.exp(-(float) (eixLayer1.value))))); //Sigmoid activation function over the node's final weight plus the bias //TODO: revive this and erase below when ParallelME support element index location and matrix multiplication
            }
        });
    }

    private float node_mult(final float v, final float[] column) {
        Array<Float32> W = new Array<Float32>(column, Float32.class);
        final Float32 out = new Float32();
        out.value = (float) 0;
        W.par().foreach(new Foreach<Float32>() {
            @Override
            public void function(Float32 element) {
                out.value = out.value + v * (float) element.value;
            }
        });
        return out.value;
    }

    public boolean recognize(Bitmap thePic) throws IOException {

        face = thePic;
        Array<Float32> In = null; //Input
        Array<Float32> L1 = null; //Layer 1
        Array<Float32> Out = null; //Layer 2
        //Configure and get image sizes
        this.resizeImage(face, 56);
        int w = face.getWidth();
        int h = face.getHeight();
        Log.d("Image sizes", "" + w + " " + h);
        //Layer sizes. The parameters are fixed, based of previous network training.
        int sizeIn = 3136+3;
        int sizeL1 = 1024+3;
        int sizeOut = 11;
        //Gets the image in a 1d array with pixels treated to work well in the network (avg of R, G and B and normalized)
        float[] pixelsNormalized = getTreatedPixels(w, h);
        //Weight matrices
        WeightsTable W1 = new WeightsTable(sizeIn, sizeL1); //Weights from input to layer 1
        WeightsTable W2 = new WeightsTable(sizeL1, sizeOut); //Weights from layer 1 to layer 2
        WeightsTable B1 = new WeightsTable(1, sizeL1); //Weights from input to layer 1
        WeightsTable B2 = new WeightsTable(1, sizeOut); //Weights from layer 1 to layer 2
        //Original network weights reading and setting. These files are matrices that follow the format 'number number ... number' on each line.
        W1.readAndInit("experiment-01-none-sigmoid-parameters-01-L1.txt", getResources().getAssets());
        W2.readAndInit("experiment-01-none-sigmoid-parameters-01-L2.txt", getResources().getAssets());
        B1.readAndInit("experiment-01-none-sigmoid-parameters-01-B1.txt", getResources().getAssets());
        B2.readAndInit("experiment-01-none-sigmoid-parameters-01-B2.txt", getResources().getAssets());
        //Allocating and initializing memory spaces to be operated on
        float[] arrIn = new float[sizeIn];
        float[] arrL1 = new float[sizeL1];
        float[] arrOut = new float[sizeOut];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                arrIn[i + (j * w)] = pixelsNormalized[i + (j * w)];
            }
        }
        arrIn[(w-1) + ((h-1) * w) + 1] = 1;

        for (int j = 0; j < sizeL1; j++) {
            arrL1[j] = 0;
        }
        arrL1[sizeL1-1] = 1;

        for (int j = 0; j < sizeOut; j++) {
            arrOut[j] = 0;
        }

        In= new Array<Float32>(arrIn, Float32.class);
        L1 = new Array<Float32>(arrL1, Float32.class);
        Out = new Array<Float32>(arrOut, Float32.class);

        //Network processing
        //mult(In, W1, L1, B1); //In -> L1 //TODO: revive this and erase below when ParallelME support element index location and matrix multiplication
        for(int i = 0; i < sizeL1; i++){
            arrL1[i] = node_mult(arrIn[i], W1.getColumn(i));
        }

        //mult(L1, W2, Out, B2); //In -> L1 //TODO: revive this and erase below when ParallelME support element index location and matrix multiplication
        for(int i = 0; i < sizeOut; i++){
            arrOut[i] = node_mult(arrL1[i], W2.getColumn(i));
        }


        float[] final_classification = new float[sizeOut];
        float biggest_value = -10;
        int result = -1;
        float[] out = new float[sizeOut];
        //Out.toJavaArray(out); //TODO: revive this and erase below when ParallelME support element index location and matrix multiplication
        for(int i = 0; i < sizeOut; i++)
            out[i] = arrOut[i];
        for(int k = 0; k < sizeOut; k++) {
            final_classification[k] = out[k];
            Log.d("FinalResult",  "k" + k + ": was " + final_classification[k] + ".");
            if (final_classification[k] > biggest_value)
            {
                biggest_value = final_classification[k]; //TODO: define minimum acceptable score for a classification to be presented.
                result = k;
            }
        }
        Log.d("FinalResult",  "Biggest value was " + result + ".");


        if(result == 0) //TODO: this is trying to detect only the face whose's class is zero. This should change.
            return true;
        else
            return false;
    }
}
