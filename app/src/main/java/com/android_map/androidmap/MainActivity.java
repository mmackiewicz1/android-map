package com.android_map.androidmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
