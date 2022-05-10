package com.magisterka.geolokalizator_client;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;


import java.util.ArrayList;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class DataCollectorLocation{

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;

    private ArrayList<ArrayList<String>> locationData;

    private Context context;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;

    private LocationManager locationManager;


    public DataCollectorLocation(Context newContext) {
        this.context=newContext;

        locationRequest = LocationRequest.create()
                .setInterval(DEFAULT_UPDATE_INTERVAL * 1000)
                .setFastestInterval(FAST_UPDATE_INTERVAL * 1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
    }

    public ArrayList<ArrayList<String>> getLocationData()
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
            fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location != null) {
                        fillLocationList(location);
                    }
                    else if(!alreadyUpdated){
                        updateLocation();
                    }
                    //TODO add notification about lack of gps signal
                }

            });
        }
    }

    private void fillLocationList(Location location)
    {
        locationData = new ArrayList<>(6);
        for(int i=0; i < 6; i++) {
            locationData.add(new ArrayList());
        }
        locationData.get(0).add(0,"Latitude");
        locationData.get(0).add(1,String.valueOf(location.getLatitude()));

        locationData.get(1).add(0,"Altitude");
        locationData.get(1).add(1,String.valueOf(location.getAltitude()));

        locationData.get(2).add(0,"Longitude");
        locationData.get(2).add(1,String.valueOf(location.getLongitude()));

        locationData.get(3).add(0,"Altitude");
        locationData.get(3).add(1,String.valueOf(location.getAltitude()));

        locationData.get(4).add(0,"Time");
        locationData.get(4).add(1,String.valueOf(location.getTime()));

        locationData.get(5).add(0,"Accurency");
        locationData.get(5).add(1,String.valueOf(location.getAccuracy()));

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
