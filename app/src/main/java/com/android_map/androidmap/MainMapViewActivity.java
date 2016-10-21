package com.android_map.androidmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android_map.androidmap.tasks.MapThumbnailRequestTask;

import java.util.concurrent.ExecutionException;

public class MainMapViewActivity extends AppCompatActivity {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map_view);

        try {
            downloadMapThumbnail();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void downloadMapThumbnail() throws ExecutionException, InterruptedException {
        ImageView imageView = (ImageView) findViewById(R.id.thumbnailView);
        new MapThumbnailRequestTask(imageView, this).execute();
    }

    public void displayMapFragment(View view) {
        Intent intent = new Intent(
                this,
                MapFragmentActivity.class
        );

        intent.putExtra(LATITUDE, ((EditText) findViewById(R.id.latitude)).getText().toString());
        intent.putExtra(LONGITUDE, ((EditText) findViewById(R.id.longitude)).getText().toString());

        startActivity(intent);
    }
}
