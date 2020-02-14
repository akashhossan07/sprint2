package com.example.sprint1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // static final int REQUEST_IMAGE_CAPTURE = 1;//SA
    //  String mCurrentPhotoPath;//SA
    RelativeLayout mainLayout;
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery = null;
    private FusedLocationProviderClient fusedLocationClient;
    String imageFileName;
    String currentLatitude;
    String currentLongiutde;
    EditText photoCaption;
    EditText timeIndication;
    TextView latIndication;
    TextView longIndication;

    String keywords = "";
    String fromLatitude = "";
    String toLatitude = "";
    String fromLongitude = "";
    String toLongitude = "";


    // todo this is the up to date file as 2:21PM Jan 31, 2020
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, 1);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mainLayout = findViewById(R.id.mainLayout);
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
        photoGallery = populateGallery(minDate, maxDate, keywords, fromLatitude, toLatitude, fromLongitude, toLongitude);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);

    }

    private ArrayList<String> populateGallery(Date minDate, Date maxDate,String keywords, String fromLatitude, String toLatitude, String fromLongitude, String toLongitude) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.sprint1/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                String filePathData[] = f.getPath().toString().split("_");
                double fromLatitudeNumber;
                double toLatitudeNumber;
                double fileLatitudeNumber;

                double fromLongitudeNumber;
                double toLongitudeNumber;
                double fileLongitudeNumber;

                try{
                    fromLatitudeNumber = Float.parseFloat(fromLatitude);
                } catch (Exception ex) {
                    fromLatitudeNumber = -180;
                }

                try{
                    toLatitudeNumber = Float.parseFloat(toLatitude);
                } catch (Exception ex) {
                    toLatitudeNumber = 180;
                }

                try{
                    fileLatitudeNumber = Float.parseFloat(filePathData[3]);
                } catch (Exception ex) {
                    fileLatitudeNumber = 0;
                }

                try{
                    fromLongitudeNumber = Float.parseFloat(fromLongitude);
                } catch (Exception ex) {
                    fromLongitudeNumber = -180;
                }

                try{
                    toLongitudeNumber = Float.parseFloat(toLongitude);
                } catch (Exception ex) {
                    toLongitudeNumber = 180;
                }

                try{
                    fileLongitudeNumber = Float.parseFloat(filePathData[4]);
                } catch (Exception ex) {
                    fileLongitudeNumber = 0;
                }

                Log.d("fuck", fromLongitudeNumber + "_" + toLongitudeNumber);
                Log.d("fuckstring", fromLongitude + "_" + fromLongitude);

                if (((minDate == null && maxDate == null) || (f.lastModified() >= minDate.getTime() && f.lastModified() <= maxDate.getTime()))
                        && (keywords == "" || f.getPath().contains(keywords))
                        && ((fromLatitude == null && toLatitude == null) || (fileLatitudeNumber >= fromLatitudeNumber && fileLatitudeNumber <= toLatitudeNumber))
                        && ((fromLongitude == null && toLongitude == null) || (fileLongitudeNumber >= fromLongitudeNumber && fileLongitudeNumber <= toLongitudeNumber)))
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
        photoGallery = populateGallery(new Date(Long.MIN_VALUE), new Date(), keywords, fromLatitude, toLatitude, fromLongitude, toLongitude);
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
        latIndication = (TextView) findViewById(R.id.lat);
        longIndication = (TextView) findViewById(R.id.lon);

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
            latIndication.setText(currentData[3]);
            longIndication.setText(currentData[4]);


            //try {
            //    final ExifInterface location = new ExifInterface(currentPhotoPath);
            //    Log.d("location", currentPhotoPath);
            //    Log.d("Latitude", location.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            //    Log.d("Longitude", (location.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)).toString());
            //    latIndication.setText(location.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) + " " + location.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            //    longIndication.setText(location.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) + " " + location.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            //} catch (IOException e) {
                //logger.info("Couldn't read exif info: " + e.getLocalizedMessage());
            //}

        }
    }

    private void updatePhoto(String path, String caption) {
        if (currentPhotoIndex < photoGallery.size()) {
            String[] attr = path.split("_");
            if (attr.length >= 3) {
                File to = new File(attr[0] + "_" + attr[1] + "_" + caption + "_" + attr[3] + "_" + attr[4]+ "_" + attr[5]);
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

                fromLatitude = (String) data.getStringExtra("STARTLATITUDE");
                toLatitude = (String) data.getStringExtra("ENDLATITUDE");
                fromLongitude = (String) data.getStringExtra("STARTLONGITUDE");
                toLongitude = (String) data.getStringExtra("ENDLONGITUDE");


                currentPhotoIndex = 0;
                photoGallery = populateGallery(startTimestamp, endTimestamp, keywords, fromLatitude, toLatitude, fromLongitude, toLongitude);
                if (photoGallery.size() == 0) {
                    photoGallery = populateGallery(new Date(Long.MIN_VALUE), new Date(), "", "", "", "", "");
                    displayPhoto(photoGallery.get(currentPhotoIndex));
                } else {
                    displayPhoto(photoGallery.get(currentPhotoIndex));
                }
            }
        }
        //todo it works but needs work
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            ImageView iv = (ImageView) findViewById(R.id.ivGallery);
            iv.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
           photoGallery = populateGallery(new Date(Long.MIN_VALUE), new Date(), "", "", "", "", "");
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

        fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLatitude = Double.toString(location.getLatitude());
                    currentLongiutde = Double.toString(location.getLongitude());
                }
            }
        });

        imageFileName = "JPEG_" + timeStamp + "_ _" + currentLatitude + "_" + currentLongiutde + "_";
        //text_1.setText( imageFileName,TextView.BufferType.EDITABLE);
      //  imageFileName = text_1.getText().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.d("CreateImageFile",currentPhotoPath);
        Log.d("Image File NAme",imageFileName);
        return image;
    }

    /** Upload is pressed */
    public void UploadButton(View v) {
        Upload Upload = new Upload();
        Upload.execute( currentPhotoPath );
    }


    /** Connect to google Drive Server and upload Picture **/
    private class Upload extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... Img) {

            final String Upload_URL = " https://www.googleapis.com/upload/drive/v3/files/\n";
            String token = "ya29.Il-9BzG7pNEagfMwEjvI-1mMOzBrwIY4_sCx2YzT8_r3UY0yQGCCbD2vxLrZn3uYQyFnMdwqNnTUqDUzgadj-5TRiQpm8eaay_je80uzjJOr205acZKrvG9bGF6vli7y8g";
            final File Image = new File(currentPhotoPath);
            final int BufferSize = 4096;

            HttpsURLConnection urlConnection;
            String serverResponse = "Connect Upload";

            try {

                URL urltrue = new URL(Upload_URL);

                urlConnection = (HttpsURLConnection) urltrue.openConnection();
                urlConnection.setRequestProperty("Authorization", "Bearer " + token);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);

                // Get image name
                urlConnection.setRequestProperty("Filename", Image.getName());

                //open output stream for writing data
                OutputStream out = urlConnection.getOutputStream();

                // Open input stream for reading data
                FileInputStream InputFile = new FileInputStream(Image);

                byte[] buffer = new byte[(int) BufferSize];

                int bytesRead = -1;

                while((bytesRead = InputFile.read(buffer)) != -1){
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                InputFile.close();

                // Check HTTP Response code (usefull for errors)
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){
                    //read server response
                    BufferedReader Reader = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()
                    ));

                    serverResponse = "Upload success, Code: " + responseCode;
                    Log.d("Server Response",serverResponse);
                } else {
                    Log.d("Server Response","Server Response's non-okay: " + responseCode);
                    serverResponse = "Upload failed, Code: " + responseCode;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView response = findViewById(R.id.UploadStatus);
            response.setText(result);
        }
    }



}


