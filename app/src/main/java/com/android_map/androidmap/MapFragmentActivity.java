package com.android_map.androidmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android_map.androidmap.tasks.MapFragmentRequestTask;

import java.util.concurrent.ExecutionException;

public class MapFragmentActivity extends AppCompatActivity {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_fragment);

        Intent intent = getIntent();

        try {
            downloadMapDetailedView(intent.getIntExtra(LATITUDE, 0), intent.getIntExtra(LONGITUDE, 0));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void downloadMapDetailedView(int x, int y) throws ExecutionException, InterruptedException {
        imageView = (ImageView) findViewById(R.id.mapFragment);
        new MapFragmentRequestTask(imageView, x, y, this).execute();
    }
}
