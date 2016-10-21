package com.android_map.androidmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class DisplayMapFragmentActivity extends AppCompatActivity {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_fragment);

        Intent intent = getIntent();
        Log.i("Sent latitude ", intent.getStringExtra(LATITUDE));
        Log.i("Sent longitude ", intent.getStringExtra(LONGITUDE));
    }
}
