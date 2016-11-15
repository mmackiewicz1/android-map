package com.android_map.androidmap.tasks;

import static com.android_map.androidmap.utils.SoapAPIParameters.NAMESPACE;
import static com.android_map.androidmap.utils.SoapAPIParameters.URL;
import static com.android_map.androidmap.utils.SoapRequestProperties.REQUEST_MAP_NAME;
import static com.android_map.androidmap.utils.SoapResponseProperties.LATITUDE_MAX;
import static com.android_map.androidmap.utils.SoapResponseProperties.LATITUDE_MIN;
import static com.android_map.androidmap.utils.SoapResponseProperties.LONGITUDE_MAX;
import static com.android_map.androidmap.utils.SoapResponseProperties.LONGITUDE_MIN;

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
    private static final String METHOD_NAME = "GetCoordinatesBounds";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetCoordinatesBounds";

    private ProgressDialog dialog;

    public MapCoordinatesBoundsRequestTask(Activity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected CoordinatesBoundsResponse doInBackground(URL... params) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty(REQUEST_MAP_NAME, "radom");

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
                Double.parseDouble(soapObject.getProperty(LATITUDE_MIN).toString()),
                Double.parseDouble(soapObject.getProperty(LATITUDE_MAX).toString()),
                Double.parseDouble(soapObject.getProperty(LONGITUDE_MIN).toString()),
                Double.parseDouble(soapObject.getProperty(LONGITUDE_MAX).toString())
        );
    }
}
