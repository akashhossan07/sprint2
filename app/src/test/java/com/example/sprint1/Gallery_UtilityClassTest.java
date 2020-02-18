package com.example.sprint1;
import android.os.Environment;

import com.example.sprint1.SupportPackage.Gallery_UtilityClass;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class Gallery_UtilityClassTest {
    ArrayList<String> photoGallery = null;
    @Test
    public void populateGallery() {


        Date minDate = new Date("Sun Dec 02 08:47:04 PST 292269055");
        Date maxDate = new Date("Sat Aug 16 23:12:55 PST 292278994");
        String keywords = "";
        String fromLatitude = "";
        String toLatitude = "";
        String fromLongitude = "";
        String toLongitude = "";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Android/data/com.example.sprint1/files/Pictures");

        String photoGallery_Expected_Result = "/storage/emulated/0/Android/data/com.example.sprint1/files/Pictures/JPEG_2020:02:17 16:36_1_null_null_4763945566309605507.jpg";

        Gallery_UtilityClass populateGallery = new Gallery_UtilityClass();
        photoGallery = populateGallery.populateGallery(minDate, maxDate, keywords, fromLatitude, toLatitude,  fromLongitude, toLongitude);
        assertEquals(photoGallery_Expected_Result,photoGallery);
    }



}