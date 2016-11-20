package com.android_map.androidmap.tasks;

import static com.android_map.androidmap.utils.SoapAPIParameters.NAMESPACE;
import static com.android_map.androidmap.utils.SoapAPIParameters.URL;
import static com.android_map.androidmap.utils.SoapRequestProperties.LATITUDE_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.LATITUDE_TWO;
import static com.android_map.androidmap.utils.SoapRequestProperties.LONGITUDE_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.LONGITUDE_TWO;
import static com.android_map.androidmap.utils.SoapRequestProperties.REQUEST_MAP_NAME;
import static com.android_map.androidmap.utils.SoapRequestProperties.X_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.X_TWO;
import static com.android_map.androidmap.utils.SoapRequestProperties.Y_ONE;
import static com.android_map.androidmap.utils.SoapRequestProperties.Y_TWO;
import static com.android_map.androidmap.utils.SoapResponseProperties.DETAILED_IMAGE;
import static com.android_map.androidmap.utils.SoapResponseProperties.RESPONSE_MAP_NAME;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import com.android_map.androidmap.helpers.MarshalDouble;
import com.android_map.androidmap.models.MapDetailResponse;
import com.android_map.androidmap.models.PixelCoordinates;
import com.android_map.androidmap.models.RegularCoordinates;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapFragmentRequestTask extends AsyncTask<java.net.URL, Integer, MapDetailResponse> {
    private String methodName = "GetDetailedMapByCoordinates";
    private String soapAction = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByCoordinates";
    private RegularCoordinates regularCoordinates;
    private PixelCoordinates pixelCoordinates;
    private ImageView imageView;
    private ProgressDialog dialog;
    private boolean isRegularCoordinateFragment;

    public MapFragmentRequestTask(ImageView imageView, double latitude1, double longitude1, double latitude2, double longitude2, Activity activity) {
        this.imageView = imageView;
        regularCoordinates = new RegularCoordinates(latitude1, longitude1, latitude2, longitude2);
        dialog = new ProgressDialog(activity);

        methodName = "GetDetailedMapByCoordinates";
        soapAction = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByCoordinates";

        isRegularCoordinateFragment = true;
    }

    public MapFragmentRequestTask(ImageView imageView, int latitude1, int longitude1, int latitude2, int longitude2, Activity activity) {
        this.imageView = imageView;
        pixelCoordinates = new PixelCoordinates(latitude1, longitude1, latitude2, longitude2);
        dialog = new ProgressDialog(activity);

        methodName = "GetDetailedMapByPixelLocation";
        soapAction = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByPixelLocation";

        isRegularCoordinateFragment = false;
    }

    @Override
    protected MapDetailResponse doInBackground(URL... params) {
        SoapObject request = new SoapObject(NAMESPACE, methodName);

        assignProperties(request);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        new MarshalDouble().register(envelope);

        HttpTransportSE httpTransportSE =  new HttpTransportSE(URL);
        httpTransportSE.debug = true;

        try {
            httpTransportSE.call(soapAction, envelope);

            return parseResponse((SoapObject)envelope.getResponse());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPreExecute() {
        dialog.setMessage("Ładowanie fragmentu mapy.");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected void onPostExecute(MapDetailResponse result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (result != null) {
            byte[] decodedString = Base64.decode(result.getDetailedImage(), Base64.DEFAULT);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void assignProperties(SoapObject request) {
        request.addProperty(REQUEST_MAP_NAME, "radom");

        if (isRegularCoordinateFragment) {
            request.addProperty(LATITUDE_ONE, regularCoordinates.getLatitude1());
            request.addProperty(LONGITUDE_ONE, regularCoordinates.getLongitude1());
            request.addProperty(LATITUDE_TWO, regularCoordinates.getLatitude2());
            request.addProperty(LONGITUDE_TWO, regularCoordinates.getLongitude2());
        } else {
            request.addProperty(X_ONE, pixelCoordinates.getLatitude2());
            request.addProperty(Y_ONE, pixelCoordinates.getLongitude1());
            request.addProperty(X_TWO, pixelCoordinates.getLatitude1());
            request.addProperty(Y_TWO, pixelCoordinates.getLongitude2());
        }
    }

    private MapDetailResponse parseResponse(SoapObject soapObject) {
        return new MapDetailResponse(
                soapObject.getProperty(RESPONSE_MAP_NAME).toString(),
                soapObject.getProperty(DETAILED_IMAGE).toString()
        );
    }
}
