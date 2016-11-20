package com.android_map.androidmap;

import static com.android_map.androidmap.utils.SoapRequestProperties.LATITUDE_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.LATITUDE_TWO;
import static com.android_map.androidmap.utils.SoapRequestProperties.LONGITUDE_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.LONGITUDE_TWO;
import static com.android_map.androidmap.utils.SoapRequestProperties.MAP_IMAGE_HEIGHT;
import static com.android_map.androidmap.utils.SoapRequestProperties.MAP_IMAGE_WIDTH;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android_map.androidmap.tasks.MapFragmentRequestTask;

import java.util.concurrent.ExecutionException;

public class MapFragmentActivity extends AppCompatActivity {
    private static final String COORDINATES_PRESENT = "coordinatesPresent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_fragment);

        Intent intent = getIntent();

        if (intent.getBooleanExtra(COORDINATES_PRESENT, true)) {
            try {
                downloadMapDetailedViewByCoordinates(
                        intent.getDoubleExtra(LATITUDE_ONE, 0f),
                        intent.getDoubleExtra(LONGITUDE_ONE, 0f),
                        intent.getDoubleExtra(LATITUDE_TWO, 0f),
                        intent.getDoubleExtra(LONGITUDE_TWO, 0f));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        } else {
            try {
                downloadMapDetailedViewByPixelLocation(
                        (int) (intent.getIntExtra(LATITUDE_ONE, 0) * (500f / intent.getIntExtra(MAP_IMAGE_WIDTH, 0))),
                        (int) (intent.getIntExtra(LONGITUDE_ONE, 0) * (500f / intent.getIntExtra(MAP_IMAGE_HEIGHT, 0))),
                        (int) (intent.getIntExtra(LATITUDE_TWO, 0) * (500f / intent.getIntExtra(MAP_IMAGE_WIDTH, 0))),
                        (int) (intent.getIntExtra(LONGITUDE_TWO, 0) * (500f / intent.getIntExtra(MAP_IMAGE_HEIGHT, 0))));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    private void downloadMapDetailedViewByCoordinates(double latitude1, double longitude1, double latitude2, double longitude2) throws ExecutionException, InterruptedException {
        new MapFragmentRequestTask((ImageView) findViewById(R.id.mapFragment), latitude1, longitude1, latitude2, longitude2, this).execute();
    }

    private void downloadMapDetailedViewByPixelLocation(int latitude1, int longitude1, int latitude2, int longitude2) {
        new MapFragmentRequestTask((ImageView) findViewById(R.id.mapFragment), latitude1, longitude1, latitude2, longitude2, this).execute();
    }
}
