package com.magisterka.geolokalizator_client.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.magisterka.geolokalizator_client.datacollection.DataCollectorLocation;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorService;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorSignal;
import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.R;

public class LoginRegisterActivity extends AppCompatActivity {

    private static final int ACCESS_BACKGROUND_LOCATION = 5;
    private static final int READ_PHONE_STATE = 11;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int FOREGROUND_SERVICE = 2;
    DataCollectorLocation dataCollector;

    boolean gps_enabled = false;
    Database db;

    DataCollectorService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        db=new Database(this);

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
            case ACCESS_BACKGROUND_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else{
                    Toast.makeText(this, "This app requires permision to be granted to work properly", Toast.LENGTH_SHORT).show();}
            case READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else{
                    Toast.makeText(this, "This app requires permision to be granted to work properly", Toast.LENGTH_SHORT).show();}
            case FOREGROUND_SERVICE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else{
                    Toast.makeText(this, "This app requires permision to be granted to work properly", Toast.LENGTH_SHORT).show();}
        }
    }


}

/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, DataCollectorService.class));
        } else {
            startService(new Intent(this, DataCollectorService.class));
        }*/

       /* requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE},FOREGROUND_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE},FOREGROUND_SERVICE);}

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},ACCESS_BACKGROUND_LOCATION);}

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},READ_PHONE_STATE);
            requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},ACCESS_BACKGROUND_LOCATION);
            return;
        }*/

/*if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //dataCollector.updateLocationList(false);
            boolean stop = true;
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }*/
// stopService(new Intent(this, DataCollectorService.class));