package com.magisterka.geolokalizator_client;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.magisterka.geolokalizator_client.activities.ServiceActivity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphDataSet extends ServiceActivity {

    private GraphView graph;
    private TimeCalculator timeCalculator;

    public GraphDataSet() {

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
}
