package com.example.sprint1.SupportPackage;

import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprint1.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Gallery_UtilityClass extends AppCompatActivity {
    public static ArrayList<String> photoGallery = null;
    public static ArrayList<String> populateGallery(Date minDate, Date maxDate, String keywords, String fromLatitude, String toLatitude, String fromLongitude, String toLongitude) {
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

}
