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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private ArrayList<String> photoGallery;
    String imageFileName;
    EditText helloTextView;
    EditText timeIndication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLeft = (Button) findViewById(R.id.btnLeft);
        Button btnRight = (Button) findViewById(R.id.btnRight);
        // Button btnFilter = (Button)findViewById(R.id.btnFilter);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        //  btnFilter.setOnClickListener(filterListener);

        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        Log.d("min date is ",minDate.toString());
        Log.d("max date is",maxDate.toString());

        photoGallery = populateGallery(minDate, maxDate);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);

    }

    private ArrayList<String> populateGallery(Date minDate, Date maxDate) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.sprint1/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : file.listFiles()) {
                photoGallery.add(f.getPath());
            }
        }
        return photoGallery;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClick( View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                if(photoGallery.size() > 0) //SA
                --currentPhotoIndex;
                break;
            case R.id.btnRight:
                if(photoGallery.size() > 0) //SA
                ++currentPhotoIndex;
                break;
            default:
                break;
        }
        if (currentPhotoIndex < 0)
           currentPhotoIndex = 0;
        if (currentPhotoIndex > photoGallery.size())//SA
            currentPhotoIndex = photoGallery.size() - 1;
        if(currentPhotoIndex < photoGallery.size()){ //SA
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
        helloTextView = (EditText) findViewById(R.id.editText3);
        timeIndication = (EditText) findViewById(R.id.editText4);
        if(photoGallery.size()== 0){
            helloTextView.setEnabled(false);
            timeIndication.setEnabled(false);
        }
        if (photoGallery.size() > 0) {
            helloTextView.setEnabled(true);
            timeIndication.setEnabled(true);
            // create object of Path
            final Path myPath = Paths.get(currentPhotoPath);
            // call getFileName() and get FileName path object
            final Path fileName = myPath.getFileName();
            //helloTextView.setText(fileName.toString());
            String []currentData = fileName.toString().split("_");
            timeIndication.setText(currentData[1].toString());



            helloTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //NO CODE FOR THIS SECTION
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //NO CODE FOR THIS SECTION
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //MUST RENANME FILE AT THIS POST, S is our variable


                    //File newFile = new File(fileName.toString());
                    //File renFile = new File(s.toString()+".jpg");
                    //newFile.renameTo(renFile);
                    //String n = "Image_ _picture_.jpg";
                    //Log.d("MyDebugTab" , n.toString());
                    //String []newString = n.split("_");
                    //Log.d("MyDebugTabMod" , newString[1].toString());
                }
            });
            // text_1 = (EditText) findViewById(R.id.caption);
            //    text_1.setText( imageFileName,TextView.BufferType.EDITABLE);
        }
    }


    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            //Intent i = new Intent(MainActivity.this, SearchActivity.class);
           //  startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
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
/*
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("ENDDATE"));

                photoGallery = populateGallery(new Date(), new Date());
                Log.d("onCreate, size", Integer.toString(photoGallery.size()));
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                displayPhoto(currentPhotoPath);
            }
        }
        */
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", "Picture Taken");
                photoGallery = populateGallery(new Date(), new Date());
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                displayPhoto(currentPhotoPath);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy MMMM dd").format(new Date());
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

