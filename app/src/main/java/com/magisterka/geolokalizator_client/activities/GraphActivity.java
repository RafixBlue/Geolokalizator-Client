package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.GraphDataSet;
import com.magisterka.geolokalizator_client.R;

public class GraphActivity extends AppCompatActivity {

    private GraphView graph;
    private Database database;

    String year;
    String month;
    String day;
    String hour;
    boolean[] seriesToRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            hour = extras.getString("hour");
            seriesToRender = extras.getBooleanArray("series");
        }



        graph = (GraphView) findViewById(R.id.graph_big);
        database = new Database(this);
        Cursor cursor = database.getHourMeasurement(year,month,day,hour);

        DataPoint[] pointsRSRP=GraphDataSet.getCollectorGraphPoints(cursor, "RSRP",5);
        DataPoint[] pointsRSRQ=GraphDataSet.getCollectorGraphPoints(cursor, "RSRQ",6);
        DataPoint[] pointsRSSI=GraphDataSet.getCollectorGraphPoints(cursor, "RSSI",7);
        DataPoint[] pointsRSSNR=GraphDataSet.getCollectorGraphPoints(cursor, "RSSNR",8);


        createActivityGraph(pointsRSRP,pointsRSRQ,pointsRSSI,pointsRSSNR,seriesToRender,hour);
    }



    private String[] GraphHourLabels(String Hour)
    {
        String[] HourLabels = new String[60];

        for(int i =0;i<60;i++)
        {
            if(i < 10) {
                HourLabels[i] = Hour +":0"+ i;
            }
            else {
                HourLabels[i] = Hour +":"+ i;
            }

        }

        return HourLabels;
    }

    private void createActivityGraph(DataPoint[] pointsRSRP,DataPoint[] pointsRSRQ,DataPoint[] pointsRSSI,DataPoint[] pointsRSSNR, boolean[] seriesToRender, String hour)
    {

        if(seriesToRender[0]==true) {
            LineGraphSeries<DataPoint> seriesRSRP = new LineGraphSeries<>(pointsRSRP);
            seriesRSRP.setTitle("RSRP");
            seriesRSRP.setColor(Color.BLUE);
            graph.addSeries(seriesRSRP);
        }

        if(seriesToRender[1]==true) {
            LineGraphSeries<DataPoint> seriesRSRQ = new LineGraphSeries<>(pointsRSRQ);
            seriesRSRQ.setTitle("RSRQ");
            seriesRSRQ.setColor(Color.RED);
            graph.addSeries(seriesRSRQ);
        }

        if(seriesToRender[2]==true) {
            LineGraphSeries<DataPoint> seriesRSSI = new LineGraphSeries<>(pointsRSSI);
            seriesRSSI.setTitle("RSSI");
            seriesRSSI.setColor(Color.YELLOW);
            graph.addSeries(seriesRSSI);
        }

        if(seriesToRender[3]==true) {
            LineGraphSeries<DataPoint> seriesRSSNR = new LineGraphSeries<>(pointsRSSNR);
            seriesRSSNR.setTitle("RSSNR");
            seriesRSSNR.setColor(Color.GREEN);
            graph.addSeries(seriesRSSNR);
        }

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(59);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        String[] xLabels = GraphHourLabels(hour);

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


}