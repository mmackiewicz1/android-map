package com.android_map.androidmap.tasks;

import static com.android_map.androidmap.utils.SoapAPIParameters.NAMESPACE;
import static com.android_map.androidmap.utils.SoapAPIParameters.URL;
import static com.android_map.androidmap.utils.SoapRequestProperties.REQUEST_MAP_NAME;
import static com.android_map.androidmap.utils.SoapResponseProperties.LATITUDE_MAX;
import static com.android_map.androidmap.utils.SoapResponseProperties.LATITUDE_MIN;
import static com.android_map.androidmap.utils.SoapResponseProperties.LONGITUDE_MAX;
import static com.android_map.androidmap.utils.SoapResponseProperties.LONGITUDE_MIN;
import static com.android_map.androidmap.utils.SoapResponseProperties.RESPONSE_MAP_NAME;
import static com.android_map.androidmap.utils.SoapResponseProperties.THUMBNAIL;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private static final String METHOD_NAME = "GetMapThumbnail";
    private static final String SOAP_ACTION = "http://stm.eti.gda.pl/stm/IMapService/GetMapThumbnail";


    private ImageView imageView;
    private ProgressDialog progressDialog;

    public MapThumbnailRequestTask(ImageView imageView, Activity activity) {
        this.imageView = imageView;
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected MapThumbnailResponse doInBackground(URL... params) {
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
        progressDialog.setMessage("Ładowanie mapy.");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(MapThumbnailResponse result) {
        super.onPostExecute(result);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (result != null) {
            byte[] decodedString = Base64.decode(result.getThumbnail(), Base64.DEFAULT);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        } else {
            Log.e("Error", "Wystąpił błąd w czasie ładowania mapy");
        }
    }

    private MapThumbnailResponse parseResponse(SoapObject soapObject) {
        return new MapThumbnailResponse(
                soapObject.getProperty(THUMBNAIL).toString(),
                soapObject.getProperty(RESPONSE_MAP_NAME).toString(),
                Double.parseDouble(soapObject.getProperty(LATITUDE_MIN).toString()),
                Double.parseDouble(soapObject.getProperty(LATITUDE_MAX).toString()),
                Double.parseDouble(soapObject.getProperty(LONGITUDE_MIN).toString()),
                Double.parseDouble(soapObject.getProperty(LONGITUDE_MAX).toString())
        );
    }
}
