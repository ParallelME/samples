package org.parallelme.FaceME;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
//import dcc.ufmg.br.face_recognizer.R;


public class TakePicture extends ActionBarActivity implements OnClickListener {
    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;
    final int PIC_CROP = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_take_picture);
        setContentView(R.layout.content_take_picture);
        //retrieve a reference to the UI button
        Button captureBtn = (Button)findViewById(R.id.capture_btn);
        Button sendBtn = (Button)findViewById(R.id.send_btn);
        //handle button clicks
        captureBtn.setOnClickListener(this);

        sendBtn.setOnClickListener(this);
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

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
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

    //This function takes a batch of images from a specific subfolder of "assets", treats them like the app treats new pictures, and saves them in a specific
    //folder. The idea is treat the images that'll train the network the same way new app images are treated.
    private void treatsBatchOfImages() throws IOException {
        //Iterates over the images in an assets subfolder
        Log.d("ImageTreating", "Begun image treatment");
        for(int fn = 0; fn < 12 ; fn++) {
            String[] fileNames = getResources().getAssets().list("Dataset_LabeledTrainingImages/" + fn); //Gets filenames in subfolder
            Log.d("ImageTreating", "Treating Dataset_LabeledTrainingImages/" + fn );
            AssetManager am = getResources().getAssets();
            Bitmap bmp = null;
            InputStream inputStream = null;
            ImageView tImageView = null;
            //Gets each image and crops it in a square
            for (String name : fileNames) {
                isExternalStorageWritable();
                bmp = null;
                tImageView = new ImageView(this);
                Log.d("ImageTreating", "Treating Dataset_LabeledTrainingImages/" + fn + "/" + name);
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    inputStream = am.open("Dataset_LabeledTrainingImages/" + fn + "/" + name);
                    bmp = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                    tImageView.setImageBitmap(bmp);
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                //Treats each image and saves the result in the folder created
                this.resizeImage(bmp, 56);
                int w = face.getWidth();
                int h = face.getHeight();
                //Gets the image in a 1d array with pixels treated to work well in the network (avg of R, G and B and normalized)
                float[] pixelsNormalized = getTreatedPixels(w, h);
                //Saves the treated images in a folder.
                final File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fn + "/" + name + ".txt");
                if (!image.exists()) {
                    try {
                        image.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    BufferedWriter buf = new BufferedWriter(new FileWriter(image, true));
                    for (Float f : pixelsNormalized) {
                        buf.append(f.toString());
                        buf.append(" ");
                    }
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.d("ImageTreating", "Wrote .txt file named " + getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fn + "/" + name + ".txt. Sizes: " + );
            }
            final File flagFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/filesReady" + ".flag");
            BufferedWriter buf = new BufferedWriter(new FileWriter(flagFile, true));
            buf.close();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.capture_btn) {
            try {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            } catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }else if (v.getId() == R.id.send_btn) {
            try {
                treatsBatchOfImages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    Bitmap face;

    /**
     * @param pic image to be saved
     * @throws IOException
     */
    private void saveImage(Bitmap pic) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date()).replace(" ","");
        String name = "faceImage_" + currentDateandTime;
        FileOutputStream out = new FileOutputStream(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Dataset_LabeledTrainingImages/" + "0" + "/" + name + ".png"));
        pic.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
                picUri = data.getData();
                performCrop();
            } //user is returning from cropping the image
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                thePic = thePic.copy(Bitmap.Config.ARGB_8888, false);
                //retrieve a reference to the ImageView
                ImageView picView = (ImageView)findViewById(R.id.picture);
                //display the returned cropped image
                picView.setImageBitmap(thePic);
                try {
                    saveImage(thePic);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.recreate();
            }
        }
    }
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_picture, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}