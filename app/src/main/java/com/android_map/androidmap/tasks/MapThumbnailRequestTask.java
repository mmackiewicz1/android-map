package com.android_map.androidmap.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapThumbnailRequestTask extends AsyncTask<URL, Integer, Bitmap> {
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String METHOD_NAME = "GetMapThumbnail";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetMapThumbnail";
    private static final String URL = "http://10.0.2.2:4321/mapservice";

    private ImageView imageView;
    private ProgressDialog dialog;

    public MapThumbnailRequestTask(ImageView imageView, Activity activity) {
        this.imageView = imageView;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected Bitmap doInBackground(URL... params) {
        Log.i("Tag", "Starting background task");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        // Set all input params
        request.addProperty("mapName", "radom");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        // Enable the below property if consuming .Net service
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE =  new HttpTransportSE(URL);
        httpTransportSE.debug = true;

        HttpTransportSE androidHttpTransport = httpTransportSE;

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject)envelope.getResponse();

            byte[] decodedString = Base64.decode(response.getProperty("Thumbnail").toString(), Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        imageView.setImageBitmap(result);
    }
}
