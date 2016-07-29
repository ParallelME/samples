package org.parallelme.FaceME;

import org.opencv.imgproc.Imgproc;

/**
 * Created by Leandro on 07/12/2015.
 */
public class Parameters {
    public static int typeOfTemplateMatching = Imgproc.TM_CCOEFF_NORMED;
    //TO DO: PARAMETERIZE TEMPLATE MATCHING METHOD:
    // TM_SQDIFF, TM_SQDIFF_NORMED, TM_CCORR, TM_CCORR_NORMED, TM_CCOEFF, TM_CCOEFF_NORMED

    public static int camResolutionWidth = 480;
    public static int camResolutionHeigth = 320;
    //      HD: 1280, 720
    //      HVGA: 480, 320;
    //      QVGA: 320, 240;
    //      VGA: 640, 480;

    public static int camBackOrFrontal = 1;

    public static long waitTimeForNewFaces = 800; // em milisegundos: 1000 = 1 segundo

    public static String cascadeFileName = "haarcascade_frontalface_default.xml";
    //TO DO: PARAMETERIZE CASCADE FILES

    public static double templateMatchingThreshold = 0.7;

    public static int templateNotFoundTolerance = 4;

    // Minimum face size of screen height (percentage)
    public static double minScreenFaceSize = 0.2d;
    // Maximum face size of screen height (percentage)
    public static double maxScreenFaceSize = 0.9d;

    public static int minHaarNeighbours = 6;

    public static double ROIlength = 4.0;   //TO DO: PARAMETERIZE ROI LENGTH


    public static int rsNotRecognized = 0;
    public static int rsRecognized = 1;
    public static int rsRecognitionRunning = 2;
    public static int rsRecognitionFailed = 3;

}
