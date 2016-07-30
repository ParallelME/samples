package org.parallelme.FaceME;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.opencv.android.Utils;
import org.opencv.core.Scalar;

import java.io.IOException;


/**
 * Created by Leandro on 14/02/2016.
 */
public class RecognizerTask extends AsyncTask <Face, Void, Void> {
    //Fonte: http://dicasandroidmobile.blogspot.com.br/2013/09/desvendando-o-asynctask.html
    //Params: Tipo do Objeto que será passado por parâmetro, caso não precise mandar algum parâmetro, colocar colo "Void";
    //Progress: Tipo do Objeto usado para informar o progresso do processo, utilizados no método publishProgress(Progress...) e onProgressUpdate(Progress...);
    //Result: Tipo do Objeto de Retorno. Usado no método onInBackground(Params...) como sendo o seu retorno e o onPostExecute(Result).

    private FaceRecognizer faceReco;

    public RecognizerTask(FaceRecognizer faceReco) {
        this.faceReco = faceReco;
    }

    //É onde acontece o processamento. Este método é executado em uma thread a parte,
    // ou seja ele não pode atualizar a interface gráfica, por isso ele chama o método
    // onProgressUpdate, o qual é executado pela UI thread.
    @Override
    protected Void doInBackground(Face... params) {
        Face unknownFace = params[0];
        unknownFace.recognitionStatus = Parameters.rsRecognitionRunning;

        unknownFace.setNome("Searching");

        boolean recognitionResult = false;

        try {
            Bitmap bmpFace = Bitmap.createBitmap(unknownFace.faceImg.cols(), unknownFace.faceImg.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(unknownFace.faceImg, bmpFace);
            recognitionResult = faceReco.recognize(bmpFace);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (recognitionResult) {
            unknownFace.setColor(new Scalar(0, 255, 0, 255));
//            unknownFace.setNome("Known!");
            unknownFace.setNome("Is Alberto!");
            unknownFace.recognitionStatus = Parameters.rsRecognized;
        } else {
            unknownFace.setColor(new Scalar(0, 0, 255, 255));
//            unknownFace.setNome("Oh boy..");
            unknownFace.setNome("Is not Alberto");
            unknownFace.recognitionStatus = Parameters.rsNotRecognized;
        }

        return null;
    }

    //É onde acontece o processamento. Este método é executado em uma thread a parte,
    // ou seja ele não pode atualizar a interface gráfica, por isso ele chama o método
    // onProgressUpdate, o qual é executado pela UI thread.
//    @Override
//    protected Void doInBackground(Face... params) {
//        Face unknownFace = params[0];
//        unknownFace.recognitionStatus = Parameters.rsRecognitionRunning;
//
//        unknownFace.setNome("Searching");
//        int i = 0;
//        while(i < 3){
//            try{
//                Thread.sleep(1000);
//            } catch(InterruptedException e){
//                e.printStackTrace();
//            }
//            i++;
//        }
//
//        if (false) {
//            unknownFace.setNome("Known!");
//            unknownFace.recognitionStatus = Parameters.rsRecognized;
//        } else {
//            unknownFace.setNome("Oh boy..");
//            unknownFace.recognitionStatus = Parameters.rsRecognitionFailed;
//        }
//
//        return null;
//    }
}
