package com.magisterka.geolokalizator_client.datacollection;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.PowerManager;


import com.magisterka.geolokalizator_client.database.DatabaseDataCollection;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;
import com.magisterka.geolokalizator_client.database.DbContext;
import com.magisterka.geolokalizator_client.sharedpreferences.SettingsHelper;

import java.util.Calendar;

public class DataCollectorThread extends Thread {

    private DataCollectorSignal signalCollector;
    private DataCollectorLocation locationCollector;
    private DatabaseDataCollection databaseDataCollection;
    private Context context;

    private ContentValues signalData;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    private SettingsHelper settingsHelper;
    private static int MINUTE = 60000;


    public DataCollectorThread(Context newContext)
    {
        context = newContext;
        databaseDataCollection = new DatabaseDataCollection(context);
        signalCollector = new DataCollectorSignal(context);
        locationCollector = new DataCollectorLocation(context);
        settingsHelper = new SettingsHelper();
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GeolokalizatorApp:GeoWakelocktag"); //if stops working use this @SuppressLint("InvalidWakeLockTag") and move pm and wl back to method
    }

    @SuppressLint("NewApi")
    @Override
    public void run() {

        locationCollector.updateLocationList(false);

        signalData = signalCollector.getSignalData();

        Calendar lastInsertTime = Calendar.getInstance();

        wakeLock.acquire();

        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(!Thread.currentThread().isInterrupted())
        {
            if(dataCollectionTime(lastInsertTime))
            {
                lastInsertTime = Calendar.getInstance();

                initiateDataCollection();
            }
        }

        wakeLock.release();
    }



    @SuppressLint("NewApi")
    private void initiateDataCollection()
    {
        ContentValues locationData = locationCollector.getLocationData();

        if(!locationData.isEmpty())
        {
            databaseDataCollection.insertCollectedData(locationData,signalData);//TODO profileid

            locationCollector.updateLocationList(false);

            signalData = signalCollector.getSignalData();
        }
        

    }

    private boolean dataCollectionTime(Calendar lastInsertTime)
    {
        long timeDifference =  Calendar.getInstance().getTimeInMillis() - lastInsertTime.getTimeInMillis();
        int timeInterval = settingsHelper.loadSettingTimeInterval(context);

        if(timeDifference > timeInterval*MINUTE ) {
            return true;  }
        else {
            return false; }
    }
}
