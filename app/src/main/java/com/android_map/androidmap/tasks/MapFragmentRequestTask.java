package com.android_map.androidmap.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;
import org.ksoap2.serialization.Marshal;

import com.android_map.androidmap.helpers.MarshalDouble;
import com.android_map.androidmap.models.MapDetailResponse;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapFragmentRequestTask extends AsyncTask<java.net.URL, Integer, MapDetailResponse> {
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String METHOD_NAME = "GetDetailedMapByCoordinates";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByCoordinates";
    private static final String URL = "http://10.0.2.2:4321/MapService";

    private double latitude1;
    private double longitude1;
    private double latitude2;
    private double longitude2;
    private ImageView imageView;
    private ProgressDialog dialog;

    public MapFragmentRequestTask(ImageView imageView, double latitude1, double longitude1, double latitude2, double longitude2, Activity activity) {
        this.imageView = imageView;
        this.latitude1 = latitude1;
        this.longitude1 = longitude1;
        this.latitude2 = latitude2;
        this.longitude2 = longitude2;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected MapDetailResponse doInBackground(URL... params) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("mapName", "radom");
        request.addProperty("latitude1", latitude1);
        request.addProperty("longitude1", longitude1);
        request.addProperty("latitude2", latitude2);
        request.addProperty("longitude2", longitude2);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        new MarshalDouble().register(envelope);

        HttpTransportSE httpTransportSE =  new HttpTransportSE(URL);
        httpTransportSE.debug = true;

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);

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
