package com.android_map.androidmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapThumbnailRequestTask extends AsyncTask<URL, Integer, String> {
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String METHOD_NAME = "GetMapThumbnail";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetMapThumbnail";
    private static final String URL = "http://10.0.2.2:4321/mapservice";

    private ProgressDialog dialog;

    public MapThumbnailRequestTask(Activity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected String doInBackground(URL... params) {
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

            return response.getProperty("Thumbnail").toString();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onPreExecute() {
        this.dialog.setMessage("Proszę czekać.");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(String mapData) {
        super.onPostExecute(mapData);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
