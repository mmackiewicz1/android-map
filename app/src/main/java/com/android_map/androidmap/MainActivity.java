package com.android_map.androidmap;

import static com.android_map.androidmap.utils.SoapRequestProperties.LATITUDE_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.LATITUDE_TWO;
import static com.android_map.androidmap.utils.SoapRequestProperties.LONGITUDE_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.LONGITUDE_TWO;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android_map.androidmap.models.CoordinatesBoundsResponse;
import com.android_map.androidmap.tasks.MapCoordinatesBoundsRequestTask;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String COORDINATES_PRESENT = "coordinatesPresent";

    private CoordinatesBoundsResponse coordinatesBoundsResponse;

    public void displayMapFragmentByCoordinates(View view) {
        if (validateCoordinatesData(
                (EditText) findViewById(R.id.latitude1),
                (EditText) findViewById(R.id.longitude1),
                (EditText) findViewById(R.id.latitude2),
                (EditText) findViewById(R.id.longitude2))) {

            Intent intent = new Intent(this, MapFragmentActivity.class);

            assignExtras(intent);
            startActivity(intent);
        }
    }

    public void displayMapThumbnail(View view) {
        Intent intent = new Intent(this, MapThumbnailActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            coordinatesBoundsResponse = downloadMapCoordinatesBounds();
            ((TextView)findViewById(R.id.latitudeRange))
                    .setText(String.format("%.4f", coordinatesBoundsResponse.getLatitudeMax())+ " - " +
                            String.format("%.4f", coordinatesBoundsResponse.getLatitudeMin()));
            ((TextView)findViewById(R.id.longitudeRange))
                    .setText(String.format("%.4f", coordinatesBoundsResponse.getLongitudeMin())+ " - " +
                            String.format("%.4f", coordinatesBoundsResponse.getLongitudeMax()));
        } catch (Exception e) {
        }
    }

    private CoordinatesBoundsResponse downloadMapCoordinatesBounds() throws InterruptedException, ExecutionException {
        return new MapCoordinatesBoundsRequestTask(this).execute().get();
    }

    private boolean validateCoordinatesData(EditText latitude1, EditText longitude1, EditText latitude2, EditText longitude2) {
        if (latitude1.getText().toString().isEmpty()
                || longitude1.getText().toString().isEmpty()
                || latitude2.getText().toString().isEmpty()
                || longitude2.getText().toString().isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Proszę, uzupełnij wszystkie pola");
            alert.setPositiveButton("OK", null);
            alert.show();

            return false;
        }

        return validateInputData(latitude1, longitude1, latitude2, longitude2);
    }

    private boolean validateInputData(EditText latitude1, EditText longitude1, EditText latitude2, EditText longitude2) {
        boolean isValidated = true;

        if (Double.parseDouble(latitude1.getText().toString()) > coordinatesBoundsResponse.getLatitudeMax()
                || Double.parseDouble(latitude1.getText().toString()) < coordinatesBoundsResponse.getLatitudeMin()) {
            isValidated = false;
            latitude1.setTextColor(Color.RED);
        } else {
            latitude1.setTextColor(Color.BLACK);
        }

        if (Double.parseDouble(longitude1.getText().toString()) < coordinatesBoundsResponse.getLongitudeMin()
                || Double.parseDouble(longitude1.getText().toString()) > coordinatesBoundsResponse.getLongitudeMax()) {
            isValidated = false;
            longitude1.setTextColor(Color.RED);
        } else {
            longitude1.setTextColor(Color.BLACK);
        }

        if (Double.parseDouble(latitude2.getText().toString()) < coordinatesBoundsResponse.getLatitudeMin()
                || Double.parseDouble(latitude2.getText().toString()) > coordinatesBoundsResponse.getLatitudeMax()) {
            isValidated = false;
            latitude2.setTextColor(Color.RED);
        } else {
            latitude2.setTextColor(Color.BLACK);
        }

        if (Double.parseDouble(longitude2.getText().toString()) > coordinatesBoundsResponse.getLongitudeMax()
                || Double.parseDouble(longitude2.getText().toString()) < coordinatesBoundsResponse.getLongitudeMin()) {
            isValidated = false;
            longitude2.setTextColor(Color.RED);
        } else {
            longitude2.setTextColor(Color.BLACK);
        }

        return isValidated;
    }

    private void assignExtras(Intent intent) {
        double latitude1 = Double.parseDouble(((EditText) findViewById(R.id.latitude1)).getText().toString());
        double latitude2 = Double.parseDouble(((EditText) findViewById(R.id.latitude2)).getText().toString());

        if (latitude1 < latitude2) {
            double temp = latitude1;
            latitude1 = latitude2;
            latitude2 = temp;
        }

        double longitude1 = Double.parseDouble(((EditText) findViewById(R.id.longitude1)).getText().toString());
        double longitude2 = Double.parseDouble(((EditText) findViewById(R.id.longitude2)).getText().toString());

        if (longitude1 > longitude2) {
            double temp = longitude1;
            longitude1 = longitude2;
            longitude2 = temp;
        }

        intent.putExtra(LATITUDE_ONE, latitude1);
        intent.putExtra(LATITUDE_TWO, latitude2);
        intent.putExtra(LONGITUDE_ONE, longitude1);
        intent.putExtra(LONGITUDE_TWO, longitude2);
        intent.putExtra(COORDINATES_PRESENT, true);
    }
}
