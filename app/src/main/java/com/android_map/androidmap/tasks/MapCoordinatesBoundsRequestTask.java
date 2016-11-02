package com.android_map.androidmap.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.android_map.androidmap.models.CoordinatesBoundsResponse;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;

public class MapCoordinatesBoundsRequestTask extends AsyncTask<java.net.URL, Integer, CoordinatesBoundsResponse> {
    private static final String NAMESPACE = "http://stm.eti.gda.pl/stm";
    private static final String URL = "http://10.0.2.2:4321/MapService";
    private static final String METHOD_NAME = "GetCoordinatesBounds";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetCoordinatesBounds";

    private ProgressDialog dialog;

    public MapCoordinatesBoundsRequestTask(Activity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected CoordinatesBoundsResponse doInBackground(URL... params) {
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
    protected void onPostExecute(CoordinatesBoundsResponse result) {
        super.onPostExecute(result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private CoordinatesBoundsResponse parseResponse(SoapObject soapObject) {
        return new CoordinatesBoundsResponse(
                Double.parseDouble(soapObject.getProperty("LatitudeMin").toString()),
                Double.parseDouble(soapObject.getProperty("LatitudeMax").toString()),
                Double.parseDouble(soapObject.getProperty("LongitudeMin").toString()),
                Double.parseDouble(soapObject.getProperty("LongitudeMax").toString())
        );
    }
}
