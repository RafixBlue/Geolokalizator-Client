package com.magisterka.geolokalizator_client.datacollection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;


import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorLocation;
import com.magisterka.geolokalizator_client.datacollection.DataCollectorSignal;

import java.util.Calendar;

public class DataCollectorThread extends Thread {

    private DataCollectorSignal signalCollector;
    private DataCollectorLocation locationCollector;
    private Database database;
    private Context context;
    private Calendar lastInsertTime;

    public DataCollectorThread(Context context)
    {
        this.context =context;
        database = new Database(context);
        signalCollector = new DataCollectorSignal(context);
        locationCollector = new DataCollectorLocation(context);

    }

    @Override
    public void run() {

        locationCollector.updateLocationList(false);

        lastInsertTime = Calendar.getInstance();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        while(!Thread.currentThread().isInterrupted())
        {

            if(timePassed(lastInsertTime))
            {
                lastInsertTime = Calendar.getInstance();

                database.insertLocation(locationCollector.getLocationData());
                database.insertSignal(signalCollector.getSignalData());
                database.insertProfileData(1);

                locationCollector.updateLocationList(false);
            }

        }
        wl.release();
    }

    private boolean timePassed(Calendar lastInsertTime)
    {

        long timeDifference =  Calendar.getInstance().getTimeInMillis() - lastInsertTime.getTimeInMillis();

        if(timeDifference > 60000 ) {
            return true;  }
        else {
            return false; }
    }
}
