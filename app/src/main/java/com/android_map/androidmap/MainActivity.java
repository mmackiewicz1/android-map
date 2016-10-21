package com.android_map.androidmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android_map.androidmap.tasks.MapThumbnailRequestTask;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            downloadMapThumbnail();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        imageView.setOnTouchListener(imgSourceOnTouchListener);
    }

    private void downloadMapThumbnail() throws ExecutionException, InterruptedException {
        imageView = (ImageView) findViewById(R.id.thumbnailView);
        new MapThumbnailRequestTask(imageView, this).execute();
    }

    public void displayMapFragment(View view) {
        Intent intent = new Intent(
                this,
                MapFragmentActivity.class
        );

        intent.putExtra(LATITUDE, Integer.parseInt(((EditText) findViewById(R.id.latitude)).getText().toString()));
        intent.putExtra(LONGITUDE, Integer.parseInt(((EditText) findViewById(R.id.longitude)).getText().toString()));

        startActivity(intent);
    }

    private View.OnTouchListener imgSourceOnTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            float[] eventXY = new float[] {eventX, eventY};

            Matrix invertMatrix = new Matrix();
            ((ImageView)view).getImageMatrix().invert(invertMatrix);

            invertMatrix.mapPoints(eventXY);
            int x = Integer.valueOf((int)eventXY[0]);
            int y = Integer.valueOf((int)eventXY[1]);

            Log.i("touchedXY", "touched position: "
                    + String.valueOf(eventX) + " / "
                    + String.valueOf(eventY));

            Log.i("invertedXY", "touched position: "
                    + String.valueOf(x) + " / "
                    + String.valueOf(y));

            Drawable imgDrawable = ((ImageView)view).getDrawable();
            Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

            Log.i("imgSize", "drawable size: "
                    + String.valueOf(bitmap.getWidth()) + " / "
                    + String.valueOf(bitmap.getHeight()));

            return true;
        }};
}
