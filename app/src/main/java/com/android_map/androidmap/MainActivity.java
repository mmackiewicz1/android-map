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
import android.widget.TextView;

import com.android_map.androidmap.models.MapThumbnailResponse;
import com.android_map.androidmap.tasks.MapThumbnailRequestTask;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String LATITUDE_1 = "latitude1";
    private static final String LONGITUDE_1 = "longitude1";
    private static final String LATITUDE_2 = "latitude2";
    private static final String LONGITUDE_2 = "longitude2";
    private static final String LATITUDE_RANGE = "Przedział:\n%.3f - %.3f";
    private static final String LONGITUDE_RANGE = "Przedział:\n%.3f - %.3f";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((EditText) findViewById(R.id.latitude1)).setText("0");
        ((EditText) findViewById(R.id.longitude1)).setText("0");
        ((EditText) findViewById(R.id.latitude2)).setText("0");
        ((EditText) findViewById(R.id.longitude2)).setText("0");

        try {
            downloadMapThumbnail();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        imageView.setOnTouchListener(imgSourceOnTouchListener);
    }

    private void downloadMapThumbnail() throws ExecutionException, InterruptedException {
        imageView = (ImageView) findViewById(R.id.thumbnailView);
        MapThumbnailRequestTask mapThumbnailRequestTask = new MapThumbnailRequestTask(imageView, this);
        mapThumbnailRequestTask.execute();
        MapThumbnailResponse mapThumbnailResponse = mapThumbnailRequestTask.get();
        ((TextView)findViewById(R.id.latitudeRange)).setText(String.format(LATITUDE_RANGE, mapThumbnailResponse.getLatitudeMin(), mapThumbnailResponse.getLatitudeMax()));
        ((TextView)findViewById(R.id.longitudeRange)).setText(String.format(LONGITUDE_RANGE, mapThumbnailResponse.getLongitudeMin(), mapThumbnailResponse.getLongitudeMax()));
    }

    public void displayMapFragment(View view) {
        Intent intent = new Intent(
                this,
                MapFragmentActivity.class
        );

        intent.putExtra(LATITUDE_1, Double.parseDouble(((EditText) findViewById(R.id.latitude1)).getText().toString()));
        intent.putExtra(LATITUDE_2, Double.parseDouble(((EditText) findViewById(R.id.latitude2)).getText().toString()));
        intent.putExtra(LONGITUDE_1, Double.parseDouble(((EditText) findViewById(R.id.longitude1)).getText().toString()));
        intent.putExtra(LONGITUDE_2, Double.parseDouble(((EditText) findViewById(R.id.longitude2)).getText().toString()));

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
