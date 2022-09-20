package com.magisterka.geolokalizator_client.datacollection;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.magisterka.geolokalizator_client.TimeCalculator;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class DataCollectorLocation{

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;

    private ContentValues locationData;

    private Context context;

    private static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int FAST_UPDATE_INTERVAL = 5;

    private LocationManager locationManager;

    private TimeCalculator timeCalculator;

    public DataCollectorLocation(Context newContext) {
        this.context=newContext;
        locationData = new ContentValues();
        locationRequest = LocationRequest.create()
                .setInterval(DEFAULT_UPDATE_INTERVAL * 1000)
                .setFastestInterval(FAST_UPDATE_INTERVAL * 1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        timeCalculator = new TimeCalculator();
    }

    public ContentValues getLocationData()
    {
        return locationData;
    }

    public void checkGPSSignal()
    {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //TODO add notification about gps being turned off
        }

    }

    public void updateLocationList(boolean alreadyUpdated)
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(!alreadyUpdated){
                        updateLocation();
                    }
                    else if(location != null) {
                        fillLocationList(location);
                    }
                    //TODO add notification about lack of gps signal
                }

            });
        }
    }

    private void fillLocationList(Location location)
    {
        Date now = new Date();

        TimeZone timeZone = TimeZone.getDefault();
        String sTimeZone = timeZone.getDisplayName(false, TimeZone.SHORT);

        locationData.put("Latitude",String.valueOf(location.getLatitude()));

        locationData.put("Altitude",String.valueOf(location.getAltitude()));

        locationData.put("Longitude",String.valueOf(location.getLongitude()));

        locationData.put("DateTime",timeCalculator.getDateTime(now));

        locationData.put("Accuracy",String.valueOf(location.getAccuracy()));

        locationData.put("Time_Zone",sTimeZone);

        return;
    }

    private void updateLocation(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }

                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }
            });

        }

        updateLocationList(true);

    }

}
