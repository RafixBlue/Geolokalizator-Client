package com.magisterka.geolokalizator_client;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.text.BreakIterator;

import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IDEN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_NR;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;

public class DataCollectorLocation extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    FusedLocationProviderClient fusedLocationClient;

    LocationRequest locationRequest;

    private Context context;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;

    public DataCollectorLocation(Context newContext) {
        this.context=newContext;

        locationRequest = LocationRequest.create()
                .setInterval(DEFAULT_UPDATE_INTERVAL * 1000)
                .setFastestInterval(FAST_UPDATE_INTERVAL * 1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGPS();
                } else {
                    Toast.makeText(context, "This app requires permision to be granted to work properly", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getGPS()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

    }

}
