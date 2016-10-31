package com.android_map.androidmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android_map.androidmap.tasks.MapFragmentRequestTask;

import java.util.concurrent.ExecutionException;

public class MapFragmentActivity extends AppCompatActivity {
    private static final String LATITUDE_1 = "latitude1";
    private static final String LONGITUDE_1 = "longitude1";
    private static final String LATITUDE_2 = "latitude2";
    private static final String LONGITUDE_2 = "longitude2";
    private static final String COORDINATES_PRESENT = "coordinatesPresent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_fragment);

        Intent intent = getIntent();

        if (intent.getBooleanExtra(COORDINATES_PRESENT, true)) {
            try {
                downloadMapDetailedViewByCoordinates(
                        intent.getDoubleExtra(LATITUDE_1, 0f),
                        intent.getDoubleExtra(LONGITUDE_1, 0f),
                        intent.getDoubleExtra(LATITUDE_2, 0f),
                        intent.getDoubleExtra(LONGITUDE_2, 0f));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        } else {
            try {
                downloadMapDetailedViewByPixelLocation(
                        intent.getIntExtra(LATITUDE_1, 0),
                        intent.getIntExtra(LONGITUDE_1, 0),
                        intent.getIntExtra(LATITUDE_2, 0),
                        intent.getIntExtra(LONGITUDE_2, 0));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    private void downloadMapDetailedViewByCoordinates(double latitude1, double longitude1, double latitude2, double longitude2) throws ExecutionException, InterruptedException {
        new MapFragmentRequestTask((ImageView) findViewById(R.id.mapFragment), latitude1, longitude1, latitude2, longitude2, this).execute();
    }

    private void downloadMapDetailedViewByPixelLocation(int latitude1, int longitude1, int latitude2, int longitude2) throws ExecutionException, InterruptedException {
        new MapFragmentRequestTask((ImageView) findViewById(R.id.mapFragment), latitude1, longitude1, latitude2, longitude2, this).execute();
    }
}
