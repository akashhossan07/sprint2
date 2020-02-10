package com.example.sprint1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm");
            Date now = calendar.getTime();
            String todayStr = new SimpleDateFormat("yyyy:MM:dd HH:mm", Locale.getDefault()).format(now);
            Date today = format.parse((String) todayStr);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrowStr = new SimpleDateFormat("yyyy:MM:dd HH:mm", Locale.getDefault()).format( calendar.getTime());
            Date tomorrow = format.parse((String) tomorrowStr);
            ((EditText) findViewById(R.id.etFromDateTime)).setText(new SimpleDateFormat(
                    "yyyy:MM:dd HH:mm", Locale.getDefault()).format(today));
            ((EditText) findViewById(R.id.etToDateTime)).setText(new SimpleDateFormat(
                    "yyyy:MM:dd HH:mm", Locale.getDefault()).format(tomorrow));
        } catch (Exception ex) { }
    }
    public void cancel(final View v) {
        finish();
    }
    public void go(final View v) {
        Intent i = new Intent();
        EditText from = (EditText) findViewById(R.id.etFromDateTime);
        EditText to = (EditText) findViewById(R.id.etToDateTime);
        EditText keywords = (EditText) findViewById(R.id.etKeywords);
        EditText fromLatitude = (EditText) findViewById(R.id.fromLat);
        EditText toLatitude = (EditText) findViewById(R.id.toLat);
        EditText fromLongitude = (EditText) findViewById(R.id.fromLon);
        EditText toLongitude = (EditText) findViewById(R.id.toLon);
        i.putExtra("STARTTIMESTAMP", from.getText() != null ? from.getText().toString() : "");
        i.putExtra("ENDTIMESTAMP", to.getText() != null ? to.getText().toString() : "");
        i.putExtra("KEYWORDS", keywords.getText() != null ? keywords.getText().toString() : "");
        i.putExtra("STARTLATITUDE", fromLatitude.getText() != null ? fromLatitude.getText().toString() : "");
        i.putExtra("ENDLATITUDE", toLatitude.getText() !=null ? toLatitude.getText().toString() : "");
        i.putExtra("STARTLONGITUDE", fromLongitude.getText() != null ? fromLongitude.getText().toString() : "");
        i.putExtra("ENDLONGITUDE", toLongitude.getText() !=null ? toLongitude.getText().toString() : "");
        setResult(RESULT_OK, i);
        finish();
    }
}