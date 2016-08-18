package org.parallelme.FaceME;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.opencv.android.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class MainActivity extends AppCompatActivity implements CvCameraViewListener {


    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    ArrayList<Face> facesFound;
    private long faceNumber = 0;
    private long lastCascadeSearchTime;
    private int cameraNumber = Parameters.camBackOrFrontal;
    private final int  MY_PERMISSIONS_REQUEST_CAMERA = 10;
    private final int  MY_PERMISSIONS_REQUEST_STORAGE = 11;
    private static final int MY_PERMISSIONS_REQUEST_READ = 12;
    private boolean opencvReady = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    if(askPermissions()) initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private boolean askPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initializeOpenCVDependencies(){
        try{
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            File cascadeDir = getDir("cascade", Context.MODE_APPEND);
            File mCascadeFile = new File(cascadeDir, Parameters.cascadeFileName);
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
        if(opencvReady) openCvCameraView.enableView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_STORAGE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d("onCreate", "Permission to camera granted");
            openCvCameraView = (CameraBridgeViewBase) findViewById(R.id.faceCamera);
            openCvCameraView.setVisibility(SurfaceView.VISIBLE);
            openCvCameraView.setCvCameraViewListener(this);
            openCvCameraView.setCameraIndex(cameraNumber);
            openCvCameraView.setMaxFrameSize(Parameters.camResolutionWidth, Parameters.camResolutionHeigth);
            opencvReady = true;
            this.getApplicationContext();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(this.getIntent());

                } else {

                    Toast.makeText(MainActivity.this, "CAMERA PERMISSION Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_camera) {
            openCvCameraView.disableView();
            cameraNumber = (cameraNumber == 0 ? 1 : 0);
            openCvCameraView.setCameraIndex(cameraNumber);
            openCvCameraView.enableView();
            return true;
        }

        if (id == R.id.action_configure_facereco) {
            Intent intent = new Intent(this, TakePicture.class);
            File mainImagesDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Dataset_LabeledTrainingImages/0/");
            boolean deleted = deleteDir(mainImagesDirectory);
            mainImagesDirectory.mkdirs();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        facesFound = new ArrayList<Face>();
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat aInputFrame) {
        if (facesFound.isEmpty()) {
            detectFaceAllSizes(aInputFrame);
        } else {
            detectFacesTemplateMatching(aInputFrame);
            long duration = System.currentTimeMillis() - lastCascadeSearchTime;
            if (duration > Parameters.waitTimeForNewFaces) {
                detectFaceAllSizes(aInputFrame);
            }
        }

        // If there are any faces found, draw a rectangle around it
        for (int i = 0; i < facesFound.size(); i++) {
            Face face =  facesFound.get(i);
            Imgproc.rectangle(aInputFrame, face.trackedFace.tl(), face.trackedFace.br(), face.getColor(), 3);
            Imgproc.circle(aInputFrame, face.facePosition, 10, face.getColor());

            Imgproc.putText(aInputFrame, face.getNome(),
                    new Point(face.trackedFace.x, face.trackedFace.y + face.trackedFace.height + 15),
                    0, 0.4d, face.getColor());

        }
        return aInputFrame;
    }

    private void detectFaceAllSizes(Mat frame) {
        Imgproc.cvtColor(frame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        // Minimum face size is proportional to screen height
        int minFaceSize = (int) (frame.rows() * Parameters.minScreenFaceSize);
        // Maximum face size is proportional to screen height
        int maxFaceSize = (int) (frame.rows() * Parameters.maxScreenFaceSize);

        // Use the classifier to detect faces
        MatOfRect facesTemp = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, facesTemp, 1.1,
                    Parameters.minHaarNeighbours, Parameters.minHaarNeighbours,
                    new Size(minFaceSize, minFaceSize),
                    new Size(maxFaceSize, maxFaceSize));
        }

        //penalidade para o caso de não encontrar a face com o haar cascade
//        for (int j = 0; j < facesFound.size(); j++) {
//            facesFound.get(j).faceHaarNotFoundCount++;
//            if (facesFound.get(j).faceHaarNotFoundCount > 15) facesFound.remove(j);
//        }

        Rect[] facesArray = facesTemp.toArray();
        for (int i = 0; i <facesArray.length; i++) {

            Face newFaceFound = new Face();
            boolean repeatedFace = false;
            for (int j = 0; j < facesFound.size(); j++) {
                Face face = facesFound.get(j);

                if (searchForIntersection(facesArray[i], face.trackedFace)) {
                    repeatedFace = true;
                    newFaceFound = face;
                    break;
                }
            }
            // Copy Rect of the face;
            newFaceFound.trackedFace = facesArray[i];
            // Copy face template
            newFaceFound.faceTemplate = getFaceTemplate(frame, facesArray[i]);
            // Get new face image
            newFaceFound.faceImg = getFaceImg(frame, newFaceFound.trackedFace);
            // Calculate roi
            newFaceFound.faceRoi = getROISize(facesArray[i], new Rect(0, 0, frame.cols(), frame.rows()));
            // Update face position
            newFaceFound.facePosition = getCenterOfRect(facesArray[i]);

//            newFaceFound.faceHaarNotFoundCount = 0;

            if (!repeatedFace) {
                // Identification para a nova face
                newFaceFound.id = ++faceNumber;
                facesFound.add(newFaceFound);
//                newFaceFound.doRecognition();

                FaceRecognizer faceReco = new FaceRecognizer(this.getApplicationContext());
                RecognizerTask faceRecognitionBridge;
                faceRecognitionBridge = new RecognizerTask(faceReco);
                faceRecognitionBridge.execute(newFaceFound);

            } else {
                newFaceFound.faceTemplateNotFoundCount--;
                if (newFaceFound.faceTemplateNotFoundCount < 0) newFaceFound.faceTemplateNotFoundCount = 0;
            }

        }
        lastCascadeSearchTime = System.currentTimeMillis();
    }

    private void detectFacesTemplateMatching(Mat frame) {

        Imgproc.cvtColor(frame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        // Template matching with last knowns faces
        for (int i = 0; i < facesFound.size(); i++) {
            Face face =  facesFound.get(i);

            Mat m_matchingResult = new Mat(face.faceRoi.x, face.faceRoi.y, CvType.CV_8U);

            Imgproc.matchTemplate(frame.submat(face.faceRoi), face.faceTemplate, m_matchingResult, Parameters.typeOfTemplateMatching);
            Point matchLoc;
            Core.MinMaxLocResult mmres = Core.minMaxLoc(m_matchingResult);

            matchLoc = mmres.maxLoc;
            if (mmres.maxVal >= Parameters.templateMatchingThreshold) {

                // Add roi offset to face position
                matchLoc.x += face.faceRoi.x;
                matchLoc.y += face.faceRoi.y;

                // Update face tracked
                face.trackedFace.x = (int) matchLoc.x - face.trackedFace.width / 4;
                face.trackedFace.y = (int) matchLoc.y - face.trackedFace.height / 4;
                // Get new face template
                face.faceTemplate = getFaceTemplate(frame, face.trackedFace);
                // Get new face image
                face.faceImg = getFaceImg(frame, face.trackedFace);
                // Calculate face roi
                face.faceRoi = getROISize(face.trackedFace, new Rect(0, 0, frame.cols(), frame.rows()));
                // Update face position
                face.facePosition = getCenterOfRect(face.trackedFace);

                // Try recognition again, only if it fails
                if (face.recognitionStatus == Parameters.rsRecognitionFailed) {
                    FaceRecognizer faceReco = new FaceRecognizer(this.getApplicationContext());
                    RecognizerTask faceRecognitionBridge;
                    faceRecognitionBridge = new RecognizerTask(faceReco);
                    faceRecognitionBridge.execute(face);
                }

            } else {
                face.faceTemplateNotFoundCount++;
                if (face.faceTemplateNotFoundCount > Parameters.templateNotFoundTolerance) facesFound.remove(i);
            }
        }
    }

    /* Face template is small patch in the middle of detected face. */
    private Mat getFaceTemplate(Mat frame, Rect face) {
        Rect templateRect = face.clone();
        templateRect.x += templateRect.width / 4;
        templateRect.y += templateRect.height / 4;
        templateRect.width /= 2;
        templateRect.height /= 2;

        Mat faceTemplate = frame.submat(templateRect);
        return faceTemplate;
    }


    /* Face template is small patch in the middle of detected face. */
    private Mat getFaceImg(Mat frame, Rect face) {
        Rect imgRect = face.clone();

        // 0 <= imgRect.width &&
        // (0 <= imgRect.x &&
        imgRect.x = (imgRect.x < 0 ? 0 : imgRect.x);
        // imgRect.x + imgRect.width <= frame.cols &&
        imgRect.width = (imgRect.x + imgRect.width > frame.cols() ? frame.cols() - imgRect.x: imgRect.width );

        // 0 <= imgRect.y &&
        imgRect.y = (imgRect.y < 0 ? 0 : imgRect.y);

        // 0 <= imgRect.height &&
        // imgRect.y + imgRect.height <= frame.rows)
        imgRect.height = (imgRect.y + imgRect.height > frame.rows() ? frame.rows() - imgRect.y: imgRect.height);

        Mat faceTemplate = frame.submat(imgRect);
        return faceTemplate;
    }




    private Rect getROISize(Rect inputRect, Rect frameSize) {
        Rect outputRect = new Rect();
        // Double rect size
        outputRect.width = inputRect.width * 2;
        outputRect.height = inputRect.height * 2;

        // Center rect around original center
        outputRect.x = inputRect.x - inputRect.width / 2;
        outputRect.y = inputRect.y - inputRect.height / 2;

        // Handle edge cases
        if (outputRect.x < frameSize.x) {
            outputRect.width += outputRect.x;
            outputRect.x = frameSize.x;
        }
        if (outputRect.y < frameSize.y) {
            outputRect.height += outputRect.y;
            outputRect.y = frameSize.y;
        }

        if (outputRect.x + outputRect.width > frameSize.width) {
            outputRect.width = frameSize.width - outputRect.x;
        }
        if (outputRect.y + outputRect.height > frameSize.height) {
            outputRect.height = frameSize.height - outputRect.y;
        }

        return outputRect;
    }

    private Point getCenterOfRect(Rect rect) {
        return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    private boolean searchForIntersection(Rect origem, Rect destino) {
        /*This function leaves less rigid the calculation of intersection between two Rect,
        considering that there is an intersection only if the center of source Rect is to a distance
        radius from the center of the other Rect.          */

        Point center = getCenterOfRect(destino);
        double radius = 0.25; // radius proportion do rect de destino
        boolean contains = false;
        if (origem.contains(center)) {
            return true;
        } else {
            center.x += destino.width * radius;
            if (origem.contains(center)) {
                return true;
            } else {
                center.x -= destino.width * (2*radius);
                if (origem.contains(center)) {
                    return true;
                } else {
                    center.x += destino.width / radius;
                    center.y += destino.height / radius;
                    if (origem.contains(center)) {
                        return true;
                    } else {
                        center.y -= destino.height / (2*radius);
                        if (origem.contains(center)) {
                            return true;
                        }
                    }
                }
            }
        }

        return contains;
    }

}
