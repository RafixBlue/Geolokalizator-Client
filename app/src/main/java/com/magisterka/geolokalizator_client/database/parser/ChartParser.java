package com.magisterka.geolokalizator_client.database.parser;

import android.database.Cursor;

import com.magisterka.geolokalizator_client.database.DbContext;
import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataGraphModel;

import java.util.ArrayList;
import java.util.List;

public class ChartParser {

    private static int DATETIME_CURSOR_INDEX = 1;
    private static int ACCURACY_CURSOR_INDEX = 2;
    private static int NETWORK_PROVIDER_CURSOR_INDEX = 3;
    private static int NETWORK_TYPE_CURSOR_INDEX = 4;
    private static int RSRP_CURSOR_INDEX = 5;
    private static int RSRQ_CURSOR_INDEX = 6;
    private static int RSSI_CURSOR_INDEX = 7;
    private static int RSSNR_CURSOR_INDEX = 8;

    public static List<HourDataGraphModel> cursorToGraphModels(Cursor cursor)
    {
        List<HourDataGraphModel> dataGraphModelsList = new ArrayList<HourDataGraphModel>();
        boolean lastRowFinished = false;

        cursor.moveToFirst();

        while(!lastRowFinished)
        {
            cursor.moveToNext();

            dataGraphModelsList.add(getGraphModel(cursor));

            lastRowFinished = cursor.isLast();
        }

        return dataGraphModelsList;
    }

    private static HourDataGraphModel getGraphModel(Cursor cursor)
    {
        HourDataGraphModel dataGraphModel = new HourDataGraphModel();

        dataGraphModel.setDateTime(cursor.getString(DATETIME_CURSOR_INDEX));
        dataGraphModel.setAccuracy(cursor.getString(ACCURACY_CURSOR_INDEX));
        dataGraphModel.setNetwork_Provider(cursor.getString(NETWORK_PROVIDER_CURSOR_INDEX));
        dataGraphModel.setNetwork_Type(cursor.getString(NETWORK_TYPE_CURSOR_INDEX));
        dataGraphModel.setRsrp(cursor.getString(RSRP_CURSOR_INDEX));
        dataGraphModel.setRsrq(cursor.getString(RSRQ_CURSOR_INDEX));
        dataGraphModel.setRssi(cursor.getString(RSSI_CURSOR_INDEX));
        dataGraphModel.setRssnr(cursor.getString(RSSNR_CURSOR_INDEX));

        return dataGraphModel;
    }

    public static int[] getNumberOfMeasurementsWeek(Cursor cursor)//TODO make this understandable
    {
        int[] numberOfMeasurements = new int[7];

        cursor.moveToFirst();

        for(int i = 0;i<cursor.getCount();i++)
        {
            numberOfMeasurements[cursor.getInt(1)]=cursor.getInt(0);
            cursor.moveToNext();
        }

        return numberOfMeasurements;
    }
}
