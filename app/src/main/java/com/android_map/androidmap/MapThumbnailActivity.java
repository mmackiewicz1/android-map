package com.android_map.androidmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android_map.androidmap.models.MapImageView;
import com.android_map.androidmap.tasks.MapThumbnailRequestTask;

import java.util.concurrent.ExecutionException;

public class MapThumbnailActivity extends AppCompatActivity {
    private static final String LATITUDE_1 = "latitude1";
    private static final String LONGITUDE_1 = "longitude1";
    private static final String LATITUDE_2 = "latitude2";
    private static final String LONGITUDE_2 = "longitude2";
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
        MapThumbnailRequestTask mapThumbnailRequestTask = new MapThumbnailRequestTask(mapImageView, this);
        mapThumbnailRequestTask.execute();
    }

    public void displayMapFragmentByPixelLocation(int latitude1, int longitude1, int latitude2, int longitude2) {
        Intent intent = new Intent(
                this,
                MapFragmentActivity.class
        );

        intent.putExtra(LATITUDE_1, latitude1);
        intent.putExtra(LONGITUDE_1, longitude1);
        intent.putExtra(LATITUDE_2, latitude2);
        intent.putExtra(LONGITUDE_2, longitude2);
        intent.putExtra(COORDINATES_PRESENT, false);

        startActivity(intent);
    }

    private View.OnTouchListener imgSourceOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            MapImageView drawView = (MapImageView) view;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                drawView.left = (int)event.getX();
                drawView.top = (int)event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                drawView.right = (int)event.getX();
                drawView.bottom = (int)event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                drawView.invalidate();
                drawView.drawRect = true;

                Log.i("tag", String.valueOf(drawView.left) + ", " +
                        String.valueOf(drawView.top) + ", " +
                        String.valueOf(drawView.right) + ", " +
                        String.valueOf((drawView.bottom)));

                displayMapFragmentByPixelLocation(drawView.left, drawView.top, drawView.right, drawView.bottom);
            }

            return true;
        }
    };
}
