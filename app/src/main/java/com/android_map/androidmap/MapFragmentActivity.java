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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_fragment);

        Intent intent = getIntent();

        try {
            downloadMapDetailedView(
                    intent.getDoubleExtra(LATITUDE_1, 0f),
                    intent.getDoubleExtra(LONGITUDE_1, 0f),
                    intent.getDoubleExtra(LATITUDE_2, 0f),
                    intent.getDoubleExtra(LONGITUDE_2, 0f));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void downloadMapDetailedView(double latitude1, double longitude1, double latitude2, double longitude2) throws ExecutionException, InterruptedException {
        new MapFragmentRequestTask((ImageView) findViewById(R.id.mapFragment), latitude1, longitude1, latitude2, longitude2, this).execute();
    }
}
