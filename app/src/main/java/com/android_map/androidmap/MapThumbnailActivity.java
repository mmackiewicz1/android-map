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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.android_map.androidmap.models.MapImageView;
import com.android_map.androidmap.tasks.MapThumbnailRequestTask;

import java.util.concurrent.ExecutionException;

public class MapThumbnailActivity extends AppCompatActivity {
    private static final String COORDINATES_PRESENT = "coordinatesPresent";

    private MapImageView mapImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_thumbnail);

        try {
            downloadMapThumbnail();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        mapImageView.setOnTouchListener(imgSourceOnTouchListener);
    }

    private void downloadMapThumbnail() throws ExecutionException, InterruptedException {
        mapImageView = (MapImageView) findViewById(R.id.thumbnailView);
        mapImageView.setAdjustViewBounds(true);
        mapImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        new MapThumbnailRequestTask(mapImageView, this).execute();
    }

    public void displayMapFragmentByPixelLocation(int latitude1, int longitude1, int latitude2, int longitude2) {
        Intent intent = new Intent(this, MapFragmentActivity.class);

        if (latitude1 < latitude2) {
            int temp = latitude1;
            latitude1 = latitude2;
            latitude2 = temp;
        }

        if (longitude1 > longitude2) {
            int temp = longitude1;
            longitude1 = longitude2;
            longitude2 = temp;
        }

        intent.putExtra(LATITUDE_ONE, latitude1);
        intent.putExtra(LONGITUDE_ONE, longitude1);
        intent.putExtra(LATITUDE_TWO, latitude2);
        intent.putExtra(LONGITUDE_TWO, longitude2);
        intent.putExtra(COORDINATES_PRESENT, false);
        intent.putExtra(MAP_IMAGE_WIDTH, mapImageView.getWidth());
        intent.putExtra(MAP_IMAGE_HEIGHT, mapImageView.getHeight());

        startActivity(intent);
    }

    private View.OnTouchListener imgSourceOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            MapImageView mapView = (MapImageView) view;
            mapView.right = (int)event.getX();
            mapView.bottom = (int)event.getY();
            mapView.invalidate();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mapView.left = (int)event.getX();
                mapView.top = (int)event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                displayMapFragmentByPixelLocation(mapView.left, mapView.top, mapView.right, mapView.bottom);
            }

            return true;
        }
    };
}
