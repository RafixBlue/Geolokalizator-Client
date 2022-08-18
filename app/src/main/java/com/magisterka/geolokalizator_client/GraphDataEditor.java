package com.magisterka.geolokalizator_client;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.magisterka.geolokalizator_client.activities.ServiceActivity;
import com.magisterka.geolokalizator_client.models.GraphDataPointsModel;
import com.magisterka.geolokalizator_client.models.HourDataGraphModel;

import java.nio.IntBuffer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphDataEditor extends ServiceActivity {

    private static int DATETIME_CURSOR_INDEX = 1;
    private static int ACCURACY_CURSOR_INDEX = 2;
    private static int NETWORK_PROVIDER_CURSOR_INDEX = 3;
    private static int NETWORK_TYPE_CURSOR_INDEX = 4;
    private static int RSRP_CURSOR_INDEX = 5;
    private static int RSRQ_CURSOR_INDEX = 6;
    private static int RSSI_CURSOR_INDEX = 7;
    private static int RSSNR_CURSOR_INDEX = 8;

    public GraphDataEditor() { }

    public static int[] getNumberOfMeasurementsWeek(Database database)//TODO make this understandable
    {
        int[] numberOfMeasurements = new int[7];

        Cursor cursor;

        for(int i = 0;i<7;i++)
        {
            cursor = database.countRowsLocationByDate(i,1);
            cursor.moveToFirst();
            numberOfMeasurements[i]=cursor.getInt(0);
        }

        return numberOfMeasurements;
    }

    public DataPoint[] getActivityGraphPoints(int[] numberOfDailyMeasurements)//TODO make this understandable
    {
        DataPoint[] points = new DataPoint[9];

        points[0] = new DataPoint(0,0);

        for(int i=0;i<7;i++)
        {
            points[7-i]= new DataPoint(7-i,numberOfDailyMeasurements[i]);
        }

        points[8] = new DataPoint(8,0);

        return points;
    }

    public GraphDataPointsModel setDataPoints(List<HourDataGraphModel> data, int size)
    {
        GraphDataPointsModel pointsModel = new GraphDataPointsModel(size);
        int pointsIndex = 0;
        int databaseDataIndex = 0;


        for (int i =0;i<size;i++)
        {

            pointsIndex = Integer.parseInt(data.get(databaseDataIndex).getDateTime());

            pointsModel.pointsRSRP[i] = new DataPoint(i,Integer.parseInt(data.get(databaseDataIndex).getRSRP()));
            pointsModel.pointsRSRQ[i] = new DataPoint(i,Integer.parseInt(data.get(databaseDataIndex).getRSSI()));
            pointsModel.pointsRSSI[i] = new DataPoint(i,Integer.parseInt(data.get(databaseDataIndex).getRSRQ()));
            pointsModel.pointsRSSNR[i] = new DataPoint(i,Integer.parseInt(data.get(databaseDataIndex).getRSSNR()));
            pointsModel.pointsAccuracy[i] = new DataPoint(i,Double.parseDouble(data.get(databaseDataIndex).getAccuracy()));

            if(pointsIndex == i && pointsIndex < data.size()-1) {
                databaseDataIndex++;
            }

        }

        return pointsModel;
    }

    public List<HourDataGraphModel> cursorToGraphModelList(Cursor cursor)
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

    public HourDataGraphModel getGraphModel(Cursor cursor)
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

    public static String[] getDropdownFiller(Cursor cursor) //TODO change name and make this understandable
    {
        String[] filler = new String[cursor.getCount()];

        cursor.moveToFirst();

        for(int i =0; i < cursor.getCount();i++)
        {
            filler[i] = cursor.getString(0);
            cursor.moveToNext();

        }

        return filler;
    }

    public String[] graphHourLabels(String hour)
    {
        String[] hourLabels = new String[60];

        for(int i =0;i<60;i++)
        {
            if(i < 10) {
                hourLabels[i] = hour +":0"+ i;
            }
            else {
                hourLabels[i] = hour +":"+ i;
            }

        }
        return hourLabels;
    }

}
