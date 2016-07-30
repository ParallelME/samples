package org.parallelme.FaceME;

import org.parallelme.userlibrary.Array;
import org.parallelme.userlibrary.datatypes.Float32;
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
    private static void mult(final Array<LayerNode> Input, final WeightsTable Weights,
                             final Array<LayerNode> Layer1, final WeightsTable Bias) {
        Layer1.foreach(new ParallelIterator<LayerNode>(){    //(new UserFunctionWithIndex2<LayerNode>() {
            @Override
            public void function(final LayerNode eixLayer1) { //TODO: add par to this foreach?
                Input.foreach(new ParallelIterator<LayerNode>() {
                    @Override
                    public void function(final LayerNode eixInput) { //TODO: add par to this foreach?
                        eixLayer1.setWeight(eixLayer1.getWeight() + ((eixInput.getWeight() * Weights.getField(eixInput.x, eixLayer1.y)))); //TODO: must get these coordinates through the new library
                    }
                });
                eixLayer1.setWeight(eixLayer1.getWeight() + Bias.getField(0, eixLayer1.y));
                eixLayer1.setWeight((float) (1 / (1 + (Math.exp(-(float)(eixLayer1.getWeight())))))); //Sigmoid activation function over the node's final weight plus the bias
                //TODO: need to check this '-' operator for the new float
            }
        });
    }

    public boolean recognize(Bitmap thePic) throws IOException {

        face = thePic;
        Array<LayerNode> In = null; //Input
        Array<LayerNode> L1 = null; //Layer 1
        Array<LayerNode> Out = null; //Layer 2
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
        In = new Array<LayerNode>(LayerNode.class, false, 1, sizeIn);
        L1 = new Array<LayerNode>(LayerNode.class, false, 1, sizeL1);
        Out = new Array<LayerNode>(LayerNode.class, false, 1, sizeOut);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                In.set(new LayerNode(pixelsNormalized[i + (j * w)]), 0, i + (j * w));
            }
        }
        In.set(new LayerNode(1), 0, (w-1) + ((h-1) * w) + 1);

        for (int j = 0; j < sizeL1; j++) {
            L1.set(new LayerNode(0), 0, j);
        }
        L1.set(new LayerNode(1), 0, sizeL1-1);

        for (int j = 0; j < sizeOut; j++) {
            Out.set(new LayerNode(0), 0, j);
        }

        //Network processing
        mult(In, W1, L1, B1); //In -> L1
        Log.d("NetworkProcChecking", "L1 random samples " + L1.get(0, sizeL1 / 4).getWeight() + " and " + L1.get(0, sizeL1 - 1).getWeight());

        mult(L1, W2, Out, B2); //In -> L1

        float[] final_classification = new float[sizeOut];
        float biggest_value = -10;
        int result = -1;
        for(int k = 0; k < sizeOut; k++) {
            final_classification[k] = Out.get(0, k).getWeight();
            Log.d("FinalResult",  "k" + k + ": was " + final_classification[k] + ".");
            if (final_classification[k] > biggest_value)
            {
                biggest_value = final_classification[k]; //TODO: define minimum acceptable score for a classification to be presented.
                result = k;
            }
        }
        Log.d("FinalResult",  "Biggest value was " + result + ".");


        if(result == 0)
            return true;
        else
            return false;
    }
}
