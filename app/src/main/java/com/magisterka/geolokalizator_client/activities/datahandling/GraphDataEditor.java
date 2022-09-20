package com.magisterka.geolokalizator_client.activities.datahandling;

import android.database.Cursor;

import com.jjoe64.graphview.series.DataPoint;
import com.magisterka.geolokalizator_client.database.DbContext;
import com.magisterka.geolokalizator_client.activities.ServiceActivity;
import com.magisterka.geolokalizator_client.models.accessdatamodels.GraphDataPointsModel;
import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataGraphModel;

import java.util.ArrayList;
import java.util.List;

public class GraphDataEditor extends ServiceActivity {



    public GraphDataEditor() { }

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
