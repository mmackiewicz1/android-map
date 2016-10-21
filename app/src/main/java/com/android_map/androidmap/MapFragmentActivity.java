package com.android_map.androidmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MapFragmentActivity extends AppCompatActivity {
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
