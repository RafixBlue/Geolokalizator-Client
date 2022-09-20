package com.magisterka.geolokalizator_client.database.parser;

import android.database.Cursor;

import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDataModel;

import java.util.ArrayList;
import java.util.List;

public class SynchronizationParser {

    private static int USER_ID_CURSOR_INDEX =0;
    private static int DATETIME_CURSOR_INDEX = 1;
    private static int LATITUDE_CURSOR_INDEX = 2;
    private static int LONGITUDE_CURSOR_INDEX = 3;
    private static int ALTITUDE_CURSOR_INDEX = 4;
    private static int ACCURACY_CURSOR_INDEX = 5;
    private static int NETWORK_PROVIDER_CURSOR_INDEX = 6;
    private static int NETWORK_TYPE_CURSOR_INDEX = 7;
    private static int RSRP_CURSOR_INDEX = 8;
    private static int RSRQ_CURSOR_INDEX = 9;
    private static int RSSI_CURSOR_INDEX = 10;
    private static int RSSNR_CURSOR_INDEX = 11;

    public static List<SynchronizationDataModel> cursorToSynchronizationModels(Cursor cursor)
    {
        List<SynchronizationDataModel> models = new ArrayList<>();

        while(cursor.moveToNext())
        {
            models.add(getModel(cursor));
        }

        return models;
    }

    private static SynchronizationDataModel getModel(Cursor cursor)
    {
        SynchronizationDataModel dataModel = new SynchronizationDataModel();

        dataModel.setTimeZone("CET"); //TODO add Timezone
        dataModel.setDateTime(cursor.getString(DATETIME_CURSOR_INDEX));
        dataModel.setLatitude(cursor.getString(LATITUDE_CURSOR_INDEX));
        dataModel.setLongitude(cursor.getString(LONGITUDE_CURSOR_INDEX));
        dataModel.setAltitude(cursor.getString(ALTITUDE_CURSOR_INDEX));
        dataModel.setAccuracy(cursor.getString(ACCURACY_CURSOR_INDEX));
        dataModel.setNetwork_Provider(cursor.getString(NETWORK_PROVIDER_CURSOR_INDEX));
        dataModel.setNetwork_Type(cursor.getString(NETWORK_TYPE_CURSOR_INDEX));
        dataModel.setRsrp(cursor.getString(RSRP_CURSOR_INDEX));
        dataModel.setRsrq(cursor.getString(RSRQ_CURSOR_INDEX));
        dataModel.setRssi(cursor.getString(RSSI_CURSOR_INDEX));
        dataModel.setRssnr(cursor.getString(RSSNR_CURSOR_INDEX));

        return dataModel;
    }

    public static List<String> cursorToTimeZoneList(Cursor cursor)
    {
        cursor.moveToFirst();

        List<String> timeZonesList = new ArrayList<>(cursor.getCount());

        cursor.moveToFirst();

        boolean lastRowFinished = false;

        while(!lastRowFinished)
        {
            if(cursor.getString(0).isEmpty())
            {
                timeZonesList.add("CET");
            }
            else
            {
                timeZonesList.add(cursor.getString(0));
            }

            lastRowFinished = cursor.isLast();

            cursor.moveToNext();
        }

        return timeZonesList;
    }

}
