package com.example.sprint1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // static final int REQUEST_IMAGE_CAPTURE = 1;//SA
    //  String mCurrentPhotoPath;//SA
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery = null;
    String imageFileName;
    EditText photoCaption;
    EditText timeIndication;
    String keywords = "";
    // todo Neeed to fix the search command S.A
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLeft = (Button) findViewById(R.id.btnLeft);
        Button btnRight = (Button) findViewById(R.id.btnRight);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnSearch.setOnClickListener(filterListener);

        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        Log.d("min date is ", minDate.toString());
        Log.d("max date is", maxDate.toString());
        photoGallery = populateGallery(minDate, maxDate, keywords);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);

    }

    private ArrayList<String> populateGallery(Date minDate, Date maxDate,String keywords) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.sprint1/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((minDate == null && maxDate == null) || (f.lastModified() >= minDate.getTime() && f.lastModified() <= maxDate.getTime())) && (keywords == "" || f.getPath().contains(keywords)))
                    photoGallery.add(f.getPath());
            }

        }
        return photoGallery;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    public void onClick(View v) {
        updatePhoto(currentPhotoPath, photoCaption.getText().toString());
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        photoGallery = populateGallery(new Date(Long.MIN_VALUE), new Date(), keywords);
        switch (v.getId()) {
            case R.id.btnLeft:
                if (photoGallery.size() > 0) //SA
                    --currentPhotoIndex;
                break;
            case R.id.btnRight:
                if (photoGallery.size() > 0) //SA
                    ++currentPhotoIndex;
                break;
            default:
                break;
        }
        if (currentPhotoIndex < 0)
            currentPhotoIndex = 0;
        if (currentPhotoIndex > photoGallery.size())//SA
            currentPhotoIndex = photoGallery.size() - 1;
        if (currentPhotoIndex < photoGallery.size()) { //SA
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            Log.d("phpotoleft, size", Integer.toString(photoGallery.size()));
            Log.d("photoleft, index", Integer.toString(currentPhotoIndex));

            displayPhoto(currentPhotoPath);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
        photoCaption = (EditText) findViewById(R.id.editText3);
        timeIndication = (EditText) findViewById(R.id.editText4);
        if (photoGallery.size() == 0) {
            photoCaption.setEnabled(false);
            timeIndication.setEnabled(false);
        }
        if (photoGallery.size() > 0) {
            photoCaption.setEnabled(true);
            timeIndication.setEnabled(true);
            // create object of Path
            final Path myPath = Paths.get(currentPhotoPath);
            // call getFileName() and get FileName path object
            final Path filePath = myPath.getFileName();
            //photoCaption.setText(fileName.toString());
            final String[] currentData = myPath.toString().split("_");
            timeIndication.setText(currentData[1].toString());

            photoCaption.setText(currentData[2]);

        }
    }

    private void updatePhoto(String path, String caption) {
        if (currentPhotoIndex < photoGallery.size()) {
            String[] attr = path.split("_");
            if (attr.length >= 3) {
                File to = new File(attr[0] + "_" + attr[1] + "_" + caption + "_" + attr[3]);
                File from = new File(path);
                from.renameTo(to);

            }
        }
    }


    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    public void takePicture(View v) { //view is a parameter
        //you create an intent to till the environment to take a photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //Log.d("FileCreation","Failed");
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.sprint1.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

                //since you don't create a new activity so you just create a intent object of a class
                //
            }
        }
    }

    @Override
    //requestcode is request image capture..
    //resultcode is when you switch activities and want to come back to the main activity.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Date startTimestamp, endTimestamp;
                DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm");

                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    startTimestamp = format.parse(from);
                    endTimestamp = format.parse(to);
                } catch (Exception ex) {
                    startTimestamp = null;
                    endTimestamp = null;
                    System.out.println("Error ");
                }
               keywords = (String) data.getStringExtra("KEYWORDS");
                currentPhotoIndex = 0;
                photoGallery = populateGallery(startTimestamp, endTimestamp, keywords);
                if (photoGallery.size() == 0) {
                    displayPhoto(null);
                } else {
                    displayPhoto(photoGallery.get(currentPhotoIndex));
                }
            }
        }
        //todo it works but needs work
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            ImageView iv = (ImageView) findViewById(R.id.ivGallery);
            iv.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
           photoGallery = populateGallery(new Date(Long.MIN_VALUE), new Date(), "");
           photoCaption.setText("");
            final Path myPath = Paths.get(currentPhotoPath);
            // call getFileName() and get FileName path object
            final Path filePath = myPath.getFileName();
            //photoCaption.setText(fileName.toString());
            final String[] currentData = myPath.toString().split("_");
            timeIndication.setText(currentData[1].toString());
            photoCaption.setEnabled(true);
            timeIndication.setEnabled(true);
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy:MM:dd HH:mm").format(new Date());
        //Get input from user
        imageFileName = "JPEG_" + timeStamp + "_ _";
        //text_1.setText( imageFileName,TextView.BufferType.EDITABLE);
      //  imageFileName = text_1.getText().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.d("CreateImageFile",currentPhotoPath);
        Log.d("Image File NAme",imageFileName);
        return image;
    }
}

