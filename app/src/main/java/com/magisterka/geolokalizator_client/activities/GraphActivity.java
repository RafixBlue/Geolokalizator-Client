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
import com.magisterka.geolokalizator_client.CallServerApi.ApiCallAccessData;
import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.GraphDataEditor;
import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.models.GraphDataPointsModel;
import com.magisterka.geolokalizator_client.models.HourDataGraphModel;
import com.magisterka.geolokalizator_client.models.HourDataMapModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraphActivity extends AppCompatActivity {

    private GraphDataEditor graphDataEditor;
    private GraphDataPointsModel pointsModel;
    private GraphView graph;
    private Database database;

    private ApiCallAccessData apiCallAccessData;
    private static String BASE_URL = "http://192.168.1.74/geolokalizator/data/";

    public boolean showRSRP;
    public boolean showRSRQ;
    public boolean showRSSI;
    public boolean showRSSNR;
    public boolean showAccuracy;

    private String year;
    private String month;
    private String day;
    private String hour;

    private static boolean dataFromOnlineDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graphDataEditor = new GraphDataEditor();

        database = new Database(this);

        pointsModel = new GraphDataPointsModel(60);

        graph = (GraphView) findViewById(R.id.graph_big);

        getExtras();

        if(dataFromOnlineDatabase) {
            initDataAccessApi();
            createGraphFromOnlineDataBase();
        }
        else {
            createGraphFromLocalDataBase();
        }
    }

    private void createGraphFromLocalDataBase()
    {

        Cursor cursor = database.getHourMeasurement(year,month,day,hour);

        List<HourDataGraphModel> data = graphDataEditor.cursorToGraphModelList(cursor);

        pointsModel = graphDataEditor.setDataPoints(data,60);

        createGraph();

    }

    private void createGraphFromOnlineDataBase()
    {
        Call<List<HourDataGraphModel>> call = apiCallAccessData.getGraphDataByHour(1,Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour));
        callDropdownHandler(call);
    }

    private void initDataAccessApi()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallAccessData = retrofit.create(ApiCallAccessData.class);
    }

    private void callDropdownHandler(Call<List<HourDataGraphModel>> call)
    {
        call.enqueue(new Callback<List<HourDataGraphModel>>() {
            @Override
            public void onResponse(Call<List<HourDataGraphModel>> call, Response<List<HourDataGraphModel>> response) {

                if(response.code() != 200) { return; }//TODO Add info about error

                List<HourDataGraphModel> dataFromCall = response.body();
                pointsModel = graphDataEditor.setDataPoints(dataFromCall,60);
                createGraph();
            }

            @Override
            public void onFailure(Call<List<HourDataGraphModel>> call, Throwable t) { }

        });
    }

    private void getExtras()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dataFromOnlineDatabase=extras.getBoolean("dataFromOnlineDatabase");
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            hour = extras.getString("hour");
            showRSRP = extras.getBoolean("showRSRP");
            showRSRQ = extras.getBoolean("showRSRQ");
            showRSSI = extras.getBoolean("showRSSI");
            showRSSNR = extras.getBoolean("showRSSNR");
        }
    }




    private void AddSeriesToGraph(DataPoint[] points, String title, int color)
    {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setTitle(title);
        series.setColor(color);
        graph.addSeries(series);
    }

    private void createGraph()
    {
        if(showRSRP) {
            AddSeriesToGraph(pointsModel.pointsRSRP, "RSRP", Color.BLUE);
        }

        if(showRSRQ) {
            AddSeriesToGraph(pointsModel.pointsRSRQ, "RSRQ", Color.GREEN);
        }

        if(showRSSI) {
            AddSeriesToGraph(pointsModel.pointsRSSI, "RSSI", Color.YELLOW);
        }

        if(showRSSNR) {
            AddSeriesToGraph(pointsModel.pointsRSSNR, "RSSNR", Color.RED);
        }

        if(showAccuracy) {
            AddSeriesToGraph(pointsModel.pointsAccuracy, "Accuracy", Color.CYAN);
        }

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(59);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        String[] xLabels = graphDataEditor.graphHourLabels(hour);

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