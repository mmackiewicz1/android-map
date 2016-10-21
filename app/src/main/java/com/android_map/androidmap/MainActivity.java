package com.android_map.androidmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            downloadMapThumbnail();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void displayMapFragment(View view) {
        Intent intent = new Intent(
                this,
                DisplayMapFragmentActivity.class
        );

        intent.putExtra(LATITUDE, ((EditText) findViewById(R.id.latitude)).getText().toString());
        intent.putExtra(LONGITUDE, ((EditText) findViewById(R.id.longitude)).getText().toString());

        startActivity(intent);
    }

    private void downloadMapThumbnail() throws ExecutionException, InterruptedException {
        MapThumbnailRequestTask mapRequestTask = new MapThumbnailRequestTask(this);
        mapRequestTask.execute();

        byte[] decodedString = Base64.decode(mapRequestTask.get(), Base64.DEFAULT);
        ((ImageView) findViewById(R.id.thumbnailView))
                .setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }
}
