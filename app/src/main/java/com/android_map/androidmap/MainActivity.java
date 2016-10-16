package com.android_map.androidmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String NAMESPACE = "http://vladozver.org/";
        String METHOD_NAME = "GetCategoryById";
        String SOAP_ACTION = "http://vladozver.org/GetCategoryById";
        String URL = "http://192.168.1.3/VipEvents/Services/CategoryServices.asmx";

        SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
    }
}
