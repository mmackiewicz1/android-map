package com.android_map.androidmap.tasks;

import static com.android_map.androidmap.utils.SoapParameters.NAMESPACE;
import static com.android_map.androidmap.utils.SoapParameters.URL;

import android.app.Activity;
import android.app.Dialog;
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
        request.addProperty("mapName", "radom");

        Log.i("aa", "Loading from URL: " + URL);

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
            Log.e("Error", "Error wile loading map thumbnail");
        }
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
