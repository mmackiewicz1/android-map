package com.android_map.androidmap.tasks;

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
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String URL = "http://10.0.2.2:4321/MapService";

    private String methodName = "GetDetailedMapByCoordinates";
    private String soapAction = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByCoordinates";
    private RegularCoordinates regularCoordinates;
    private PixelCoordinates pixelCoordinates;
    private ImageView imageView;
    private ProgressDialog dialog;
    private boolean isRegualarCoordinateFragment;

    public MapFragmentRequestTask(ImageView imageView, double latitude1, double longitude1, double latitude2, double longitude2, Activity activity) {
        this.imageView = imageView;
        regularCoordinates = new RegularCoordinates(latitude1, longitude1, latitude2, longitude2);
        dialog = new ProgressDialog(activity);

        methodName = "GetDetailedMapByCoordinates";
        soapAction = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByCoordinates";

        isRegualarCoordinateFragment = true;
    }

    public MapFragmentRequestTask(ImageView imageView, int latitude1, int longitude1, int latitude2, int longitude2, Activity activity) {
        this.imageView = imageView;
        pixelCoordinates = new PixelCoordinates(latitude1, longitude1, latitude2, longitude2);
        dialog = new ProgressDialog(activity);

        methodName = "GetDetailedMapByPixelLocation";
        soapAction = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByPixelLocation";

        isRegualarCoordinateFragment = false;
    }

    @Override
    protected MapDetailResponse doInBackground(URL... params) {
        SoapObject request = new SoapObject(NAMESPACE, methodName);

        request.addProperty("mapName", "radom");
        if (isRegualarCoordinateFragment) {
            request.addProperty("latitude1", regularCoordinates.getLatitude1());
            request.addProperty("longitude1", regularCoordinates.getLongitude1());
            request.addProperty("latitude2", regularCoordinates.getLatitude2());
            request.addProperty("longitude2", regularCoordinates.getLongitude2());
        } else {
            request.addProperty("x1", pixelCoordinates.getLatitude1());
            request.addProperty("y1", pixelCoordinates.getLongitude1());
            request.addProperty("x2", pixelCoordinates.getLatitude2());
            request.addProperty("y2", pixelCoordinates.getLongitude2());
        }

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

        byte[] decodedString = Base64.decode(result.getDetailedImage(), Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }

    private MapDetailResponse parseResponse(SoapObject soapObject) {
        return new MapDetailResponse(
                soapObject.getProperty("MapName").toString(),
                soapObject.getProperty("DetailedImage").toString()
        );
    }
}
