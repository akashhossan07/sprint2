package com.example.sprint1;
import com.example.sprint1.SupportPackage.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import static org.junit.Assert.assertEquals;



public class Gallery_UtilityClassTest {

    private static Date minDate = new Date(Long.MIN_VALUE);
    private static Date maxDate = new Date(Long.MAX_VALUE);
    private static String keywords = "sajjad";
    private static String fromLatitude = "47";
    private static String toLatitude = "56";
    private static String fromLongitude = "-125";
    private static String toLongitude = "0";
    private static ArrayList<String> result;
    private static ArrayList<String> expected;


    @Test
    public void populateGallery() {

        expected = new ArrayList<String>();
        //hard coded paths
        expected.add("/storage/emulated/0/Android/data/com.example.sprint1/files/Pictures/JPEG_2020:02:18 16:32_sajjad_49.2510811_-123.002733_1017077667346231410.jpg");
        expected.add("/storage/emulated/0/Android/data/com.example.sprint1/files/Pictures/JPEG_2020:02:18 23:32_sajjad2_49.2285063_-122.9956083_39857451712266007.jpg");

        //call your method
        result = Gallery_UtilityClass.populateGallery(minDate, maxDate, keywords, fromLatitude, toLatitude,  fromLongitude, toLongitude);

        assertEquals(expected, result);
    }

}


