package com.android_map.androidmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String LATITUDE_1 = "latitude1";
    private static final String LONGITUDE_1 = "longitude1";
    private static final String LATITUDE_2 = "latitude2";
    private static final String LONGITUDE_2 = "longitude2";
    private static final String COORDINATES_PRESENT = "coordinatesPresent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void displayMapFragmentByCoordinates(View view) {
        Intent intent = new Intent(
                this,
                MapFragmentActivity.class
        );

        intent.putExtra(LATITUDE_1, Double.parseDouble(((EditText) findViewById(R.id.latitude1)).getText().toString()));
        intent.putExtra(LATITUDE_2, Double.parseDouble(((EditText) findViewById(R.id.latitude2)).getText().toString()));
        intent.putExtra(LONGITUDE_1, Double.parseDouble(((EditText) findViewById(R.id.longitude1)).getText().toString()));
        intent.putExtra(LONGITUDE_2, Double.parseDouble(((EditText) findViewById(R.id.longitude2)).getText().toString()));
        intent.putExtra(COORDINATES_PRESENT, true);

        startActivity(intent);
    }

    public void displayMapThumbnail(View view) {
        Intent intent = new Intent(
                this,
                MapThumbnailActivity.class
        );

        startActivity(intent);
    }
}
