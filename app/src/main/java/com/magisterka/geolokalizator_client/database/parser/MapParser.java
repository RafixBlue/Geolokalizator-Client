package com.magisterka.geolokalizator_client.database.parser;

import android.database.Cursor;

import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataMapModel;

import java.util.ArrayList;
import java.util.List;

public class MapParser {

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

    public static List<HourDataMapModel> cursorToMapModel(Cursor cursor)
    {
        List<HourDataMapModel> dataGraphModelsList = new ArrayList<HourDataMapModel>();
        boolean lastRowFinished = false;

        cursor.moveToFirst();

        while(!lastRowFinished)
        {
            cursor.moveToNext();

            dataGraphModelsList.add(getMapModel(cursor));

            lastRowFinished = cursor.isLast();
        }

        return dataGraphModelsList;
    }

    private static HourDataMapModel getMapModel(Cursor cursor)
    {
        HourDataMapModel dataMapModel = new HourDataMapModel();

        dataMapModel.setDateTime(cursor.getString(DATETIME_CURSOR_INDEX));
        dataMapModel.setLatitude(cursor.getString(LATITUDE_CURSOR_INDEX));
        dataMapModel.setLongitude(cursor.getString(LONGITUDE_CURSOR_INDEX));
        dataMapModel.setAltitude(cursor.getString(ALTITUDE_CURSOR_INDEX));
        dataMapModel.setAccuracy(cursor.getString(ACCURACY_CURSOR_INDEX));
        dataMapModel.setNetwork_Provider(cursor.getString(NETWORK_PROVIDER_CURSOR_INDEX));
        dataMapModel.setNetwork_Type(cursor.getString(NETWORK_TYPE_CURSOR_INDEX));
        dataMapModel.setRsrp(cursor.getString(RSRP_CURSOR_INDEX));
        dataMapModel.setRsrq(cursor.getString(RSRQ_CURSOR_INDEX));
        dataMapModel.setRssi(cursor.getString(RSSI_CURSOR_INDEX));
        dataMapModel.setRssnr(cursor.getString(RSSNR_CURSOR_INDEX));

        return dataMapModel;
    }
}
