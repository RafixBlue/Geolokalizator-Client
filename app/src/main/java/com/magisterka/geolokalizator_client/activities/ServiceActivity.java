package com.magisterka.geolokalizator_client.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.GraphDataEditor;
import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorLocation;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorService;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorSignal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ServiceActivity extends AppCompatActivity {

    private static long DAY_IN_MILLISECONDS = 86400000;

    private DataCollectorSignal signalCollector;
    private DataCollectorLocation locationCollector;
    private Database database;
    private GraphDataEditor graphDataEditor;

    private GraphView graph;
    private Button button_manual;
    private Button button_automatic;
//android:theme="@style/GraphViewCustom"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        initClasses();

        initLayout();

        graph = (GraphView) findViewById(R.id.graph);

        int[] numberOfDailyMeasurements = GraphDataEditor.getNumberOfMeasurementsWeek(database);

        DataPoint[] dataPoints = graphDataEditor.getActivityGraphPoints(numberOfDailyMeasurements);
        createActivityGraph(dataPoints);

    }


    private void initClasses()
    {
        database = new Database(this);
        signalCollector = new DataCollectorSignal(this);
        locationCollector = new DataCollectorLocation(this);
        graphDataEditor = new GraphDataEditor();

    }


    private void initLayout()
    {
        button_manual=findViewById(R.id.button_manual);
        button_automatic=findViewById(R.id.button_automatic);

    }


    public void startService(View view) {

        checkPermission();


        //DataCollectorService.isInstanceCreated();

        if(!button_automatic.getText().equals("Stop Automatic Measurement")) {

            startForegroundService(new Intent(this, DataCollectorService.class));
            button_automatic.setText("Stop Automatic Measurement");
        }
        else if(!button_automatic.getText().equals("Start Automatic Measurement")) {
            stopService(new Intent(this, DataCollectorService.class));
            button_automatic.setText("Start Automatic Measurement");
        }
    }


    public void getMeasurement(View view) {

        checkPermission();

        Toast.makeText(this, "Collecting data wait 30 seconds",Toast.LENGTH_SHORT).show();

        locationCollector.updateLocationList(false);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                database.insertCollectedData(locationCollector.getLocationData(),signalCollector.getSignalData(),1);

            }
        }, 20000);

    }


    public void test(View view) {

        /*//String[] test = timeCalculator.getLastSevenDaysDates();
        Date now = new Date();

        ContentValues locationData = new ContentValues();

        locationData.put("Latitude","222");

        locationData.put("Altitude","345");

        locationData.put("Longitude","444");

        locationData.put("DateTime","2022-06-04 12:30");

        locationData.put("Accurency","20");

        database.insert("location",locationData);

        int a = 5;
        int b = a;

         */
        //DataPoint[] points = GraphDataEditor.getCollectorGraphPoints(database, "RSSI",5);


        Intent intent = new Intent(ServiceActivity.this, GraphCreationActivity.class);
        startActivity(intent);


    }


    private void createActivityGraph(DataPoint[] points)
    {

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);


        graph.getViewport().setMinY(0);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(8);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(false);

        graph.getViewport().setScalableY(false);

        String[] xLabels = addEmptySides(getLastSevenDates());

        series.setSpacing(50);

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return xLabels[(int) value];
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }

    private String[] getLastSevenDates()
    {
        String [] lastSevenDates= new String[7];

        SimpleDateFormat form = new SimpleDateFormat("dd/MM");

        Date day = new Date();

        String dateDay;

        for(int i=0; i < 7; i++) {

            day.setTime(Calendar.getInstance().getTimeInMillis()-((6-i)*DAY_IN_MILLISECONDS));
            dateDay=form.format(day);
            lastSevenDates[i]=dateDay;

        }

        return lastSevenDates;
    }

    private String[] addEmptySides(String[] middle)
    {
        String[] newString = new String[middle.length+2];
        newString[0]="";
        for(int i=1;i<newString.length-1;i++)
        {
            newString[i]=middle[i-1];
        }
        newString[middle.length+1]="";

        return newString;
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(ServiceActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void checkPermission()
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.WAKE_LOCK);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.FOREGROUND_SERVICE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.INTERNET);
        }
    }

    private boolean checkForeground()
    {
        return true;
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            checkPermission();
        } else {
            Toast.makeText(this, "This app requires permission to be granted to work properly", Toast.LENGTH_SHORT).show();
        }
    });


    public void goToMapCreation(View view) {
        checkPermission();
        Intent intent = new Intent(ServiceActivity.this, MapCreationActivity.class);
        startActivity(intent);
    }
}