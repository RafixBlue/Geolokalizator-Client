package com.magisterka.geolokalizator_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginRegisterActivity extends AppCompatActivity {


    private static final int READ_PHONE_STATE = 11;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    DataCollectorLocation dataCollector;
    DataCollectorSignal DCS;
    boolean gps_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        dataCollector = new DataCollectorLocation(this);
        DCS =new DataCollectorSignal(this);

    }

    public void testClick(View view) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            dataCollector.updateLocationList(false);
            boolean stop = true;
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }

    }
    public void testClickTwo(View view) {
        //ArrayList<ArrayList<String>> locationData;
        //locationData = dataCollector.getLocationData();
        //if(locationData != null)
        //{
            //TextView text = findViewById(R.id.textView);
            //text.setText(locationData.get(0).get(0));
        //}

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},READ_PHONE_STATE);
            return;
        }

        DataCollectorSignal DCS =new DataCollectorSignal(this);
        String check = DCS.checkDataSignal();
        String check2 = check;
    }

    ////////////////////////////////////////////////Permisions////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dataCollector.updateLocationList(false);
                } else {
                    Toast.makeText(this, "This app requires permision to be granted to work properly", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}