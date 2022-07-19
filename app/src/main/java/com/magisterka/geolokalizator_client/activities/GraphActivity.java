package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.GraphDataEditor;
import com.magisterka.geolokalizator_client.R;

public class GraphActivity extends AppCompatActivity {

    private GraphView graph;
    private Database database;

    private static int RSRP_CURSOR_INDEX = 5;
    private static int RSRQ_CURSOR_INDEX = 6;
    private static int RSSI_CURSOR_INDEX = 7;
    private static int RSSNR_CURSOR_INDEX = 8;

    private String year;
    private String month;
    private String day;
    private String hour;
    boolean[] seriesToRender;

    private String maxMinuteOfHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        database = new Database(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            hour = extras.getString("hour");
            seriesToRender = extras.getBooleanArray("series");
        }

        maxMinuteOfHour = database.getMaxMinOfHour(year,month,day,hour);

        graph = (GraphView) findViewById(R.id.graph_big);

        Cursor cursor = database.getHourMeasurement(year,month,day,hour);

        DataPoint[] pointsRSRP= GraphDataEditor.getCollectorGraphPoints(cursor, "RSRP",RSRP_CURSOR_INDEX,maxMinuteOfHour);
        DataPoint[] pointsRSRQ= GraphDataEditor.getCollectorGraphPoints(cursor, "RSRQ",RSRQ_CURSOR_INDEX,maxMinuteOfHour);
        DataPoint[] pointsRSSI= GraphDataEditor.getCollectorGraphPoints(cursor, "RSSI",RSSI_CURSOR_INDEX,maxMinuteOfHour);
        DataPoint[] pointsRSSNR= GraphDataEditor.getCollectorGraphPoints(cursor, "RSSNR",RSSNR_CURSOR_INDEX,maxMinuteOfHour);


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