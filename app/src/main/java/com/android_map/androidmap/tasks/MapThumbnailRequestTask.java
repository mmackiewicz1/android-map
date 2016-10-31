package com.android_map.androidmap.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android_map.androidmap.models.MapThumbnailResponse;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapThumbnailRequestTask extends AsyncTask<URL, Integer, MapThumbnailResponse> {
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String METHOD_NAME = "GetMapThumbnail";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetMapThumbnail";
    private static final String URL = "http://10.0.2.2:4321/MapService";

    private ImageView imageView;
    private ProgressDialog dialog;

    public MapThumbnailRequestTask(ImageView imageView, Activity activity) {
        this.imageView = imageView;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected MapThumbnailResponse doInBackground(URL... params) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("mapName", "radom");

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
    protected void onPostExecute(MapThumbnailResponse result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        byte[] decodedString = Base64.decode(result.getThumbnail(), Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }

    private MapThumbnailResponse parseResponse(SoapObject soapObject) {
        return new MapThumbnailResponse(
                soapObject.getProperty("Thumbnail").toString(),
                soapObject.getProperty("MapName").toString(),
                Double.parseDouble(soapObject.getProperty("LatitudeMin").toString()),
                Double.parseDouble(soapObject.getProperty("LatitudeMax").toString()),
                Double.parseDouble(soapObject.getProperty("LongitudeMin").toString()),
                Double.parseDouble(soapObject.getProperty("LongitudeMax").toString())
        );
    }
}
