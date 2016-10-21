package com.android_map.androidmap.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import com.android_map.androidmap.models.MapDetailResponse;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapFragmentRequestTask extends AsyncTask<java.net.URL, Integer, MapDetailResponse> {
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String METHOD_NAME = "GetDetailedMapByPixelLocation";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetDetailedMapByPixelLocation";
    private static final String URL = "http://10.0.2.2:4321/mapservice";

    private int latitude;
    private int longitude;
    private ImageView imageView;
    private ProgressDialog dialog;

    public MapFragmentRequestTask(ImageView imageView, int latitude, int longitude, Activity activity) {
        this.imageView = imageView;
        this.latitude = latitude;
        this.longitude = longitude;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected MapDetailResponse doInBackground(URL... params) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("mapName", "radom");
        request.addProperty("x1", latitude);
        request.addProperty("y1", longitude);
        request.addProperty("x2", latitude + 100);
        request.addProperty("y2", longitude + 100);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE =  new HttpTransportSE(URL);
        httpTransportSE.debug = true;

        HttpTransportSE androidHttpTransport = httpTransportSE;

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            return parseResponse((SoapObject)envelope.getResponse());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPreExecute() {
        dialog.setMessage("Ładowanie mapy.");
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
