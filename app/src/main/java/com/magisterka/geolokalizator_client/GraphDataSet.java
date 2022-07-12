package com.magisterka.geolokalizator_client;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.magisterka.geolokalizator_client.activities.ServiceActivity;

import java.nio.IntBuffer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphDataSet extends ServiceActivity {

    private GraphView graph;
    private TimeCalculator timeCalculator;

    public GraphDataSet() {

    }

    public static int[] getNumberOfMeasurementsWeek(Database database)
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

    public DataPoint[] getActivityGraphPoints(int[] numberOfDailyMeasurements)
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


    public static DataPoint[] getCollectorGraphPoints(Cursor cursor, String dataTypeName,int index)
    {

        //Cursor cursor = database.getHourMeasurement("2022","06","13","09");

        cursor.moveToFirst();

        DataPoint[] points = new DataPoint[cursor.getCount()];
        int[] pointsFill = new int[cursor.getCount()];

        for(int i =0; i < cursor.getCount();i++)
        {
            pointsFill[cursor.getInt(1)]=cursor.getInt(index);
            if(i>0){
                cursor.moveToNext();
            }

        }
        for(int i =0; i < cursor.getCount();i++)
        {
            if(pointsFill[i]==0 && i != 0) {
                points[i]= new DataPoint(i,points[i-1].getY());
            }
            else {
                points[i]= new DataPoint(i,pointsFill[i]);
            }
        }

        return points;
    }

    public static String[] getDropdownFiller(Cursor cursor)
    {
        String[] filler = new String[cursor.getCount()];

        int a = cursor.getCount();

        cursor.moveToFirst();

        for(int i =0; i < cursor.getCount();i++)
        {
            filler[i] = cursor.getString(0);
            cursor.moveToNext();


        }

        return filler;
    }

}
