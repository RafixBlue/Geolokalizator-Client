package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.magisterka.geolokalizator_client.CallServerApi.ApiCallTime;
import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.GraphDataEditor;
import com.magisterka.geolokalizator_client.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraphCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Database database;
    private ApiCallTime apiCallTime;
    private Map<String, Spinner> dropdownMap;
    private Map<String, Boolean> switchStates;

    private static String BASE_URL = "http://192.168.1.74/geolokalizator/time/";
    private static boolean DATA_FROM_ONLINE_DATABASE = false;

    private Switch switchRSRP;
    private Switch switchRSRQ;
    private Switch switchRSSI;
    private Switch switchRSSNR;

    private Spinner dropdownYear;
    private Spinner dropdownMonth;
    private Spinner dropdownDay;
    private Spinner dropdownHour;

    private Button buttonCreateGraph;

    private GraphDataEditor graphDataEditor;

    private String year;
    private String month;
    private String day;
    private String hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_creation);

        initClasses();

        initLayout();

        disableOnStart();

        setItemSelection();

        //fillDropdownYear();

        mapDropdowns();

        if(DATA_FROM_ONLINE_DATABASE) {
            initDataAccessApi();
        }

        setDataForDropdownYear("year");

    }

    private void mapDropdowns()
    {
        dropdownMap = new HashMap<String, Spinner>();

        dropdownMap.put("year", findViewById(R.id.spinner_year_graph));
        dropdownMap.put("month", findViewById(R.id.spinner_month_graph));
        dropdownMap.put("day", findViewById(R.id.spinner_day_graph));
        dropdownMap.put("hour", findViewById(R.id.spinner_hour_graph));

    }

    private void initDataAccessApi()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallTime = retrofit.create(ApiCallTime.class);
    }

    private void initClasses()
    {
        database = new Database(this);
        graphDataEditor = new GraphDataEditor();
    }

    private void initLayout()
    {
        buttonCreateGraph = findViewById(R.id.button_create_graph);

        switchRSRP = findViewById(R.id.switch_rsrp_graph);
        switchRSRQ = findViewById(R.id.switch_rsrq__graph);
        switchRSSI = findViewById(R.id.switch_rssi_graph);
        switchRSSNR = findViewById(R.id.switch_rssnr_graph);

        dropdownYear = findViewById(R.id.spinner_year_graph);
        dropdownMonth = findViewById(R.id.spinner_month_graph);
        dropdownDay = findViewById(R.id.spinner_day_graph);
        dropdownHour = findViewById(R.id.spinner_hour_graph);
    }

    private void disableOnStart()
    {
        buttonCreateGraph.setEnabled(false);
        dropdownMonth.setEnabled(false);
        dropdownDay.setEnabled(false);
        dropdownHour.setEnabled(false);
    }

    private void setItemSelection()
    {
        dropdownYear.setOnItemSelectedListener(this);
        dropdownMonth.setOnItemSelectedListener(this);
        dropdownDay.setOnItemSelectedListener(this);
        dropdownHour.setOnItemSelectedListener(this);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setDataForDropdownYear(String dropdownType)
    {
        if(DATA_FROM_ONLINE_DATABASE) {
            Call<String[]> call = apiCallTime.getAvailableYears(1);
            callDropdownHandler(call,"year");
            return;
        }

        Cursor rawDropdownData = database.getAvailableYear(1);
        String[] dropdownData = graphDataEditor.getDropdownFiller(rawDropdownData);
        fillDropdownFromServer(dropdownYear, dropdownData);
    }

    private void setDataForDropdownMonth()
    {
        if(DATA_FROM_ONLINE_DATABASE) {
            Call<String[]> call = apiCallTime.getAvailableMonths(1, Integer.parseInt(year));
            callDropdownHandler(call,"month");
            return;
        }

        Cursor rawDropdownData = database.getAvailableMonth(1,year);
        String[] dropdownData = graphDataEditor.getDropdownFiller(rawDropdownData);
        fillDropdownFromServer(dropdownMonth, dropdownData);
    }

    private void setDataForDropdownDay()
    {
        if(DATA_FROM_ONLINE_DATABASE) {
            Call<String[]> call = apiCallTime.getAvailableDays(1, Integer.parseInt(year),Integer.parseInt(month));
            callDropdownHandler(call,"day");
            return;
        }

        Cursor rawDropdownData = database.getAvailableDay(1,year,month);
        String[] dropdownData = graphDataEditor.getDropdownFiller(rawDropdownData);
        fillDropdownFromServer(dropdownDay, dropdownData);
    }

    private void setDataForDropdownHour()
    {
        if(DATA_FROM_ONLINE_DATABASE) {
            Call<String[]> call = apiCallTime.getAvailableHours(1, Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
            callDropdownHandler(call,"hour");
            return;
        }

        Cursor rawDropdownData = database.getAvailableHour(1,year,month,day);
        String[] dropdownData = graphDataEditor.getDropdownFiller(rawDropdownData);
        fillDropdownFromServer(dropdownHour, dropdownData);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fillDropdownFromServer(Spinner dropdown, String[] items)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void callDropdownHandler(Call<String[]> call,String dropdownType)
    {
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {

                if(response.code() != 200) { return; }//TODO Add info about error

                String[] dataFromCall = response.body();

                Spinner dropdown = dropdownMap.get(dropdownType);

                fillDropdownFromServer(dropdown,dataFromCall);

            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) { }

        });
    }

    public void createGraph(View view) {

        Intent intent = new Intent(GraphCreationActivity.this, GraphActivity.class);
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        intent.putExtra("day",day);
        intent.putExtra("hour",hour);
        intent.putExtra("showRSRP",switchRSRP.isChecked());
        intent.putExtra("showRSRQ",switchRSRQ.isChecked());
        intent.putExtra("showRSSI",switchRSSI.isChecked());
        intent.putExtra("showRSSNR",switchRSSNR.isChecked());
        intent.putExtra("dataFromOnlineDatabase",DATA_FROM_ONLINE_DATABASE);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spinner_year_graph:
                year = dropdownYear.getSelectedItem().toString();
                setDataForDropdownMonth();
                dropdownMonth.setEnabled(true);
                break;
            case R.id.spinner_month_graph:
                month=dropdownMonth.getSelectedItem().toString();
                setDataForDropdownDay();
                dropdownDay.setEnabled(true);
                break;
            case R.id.spinner_day_graph:
                day=dropdownDay.getSelectedItem().toString();
                setDataForDropdownHour();
                dropdownHour.setEnabled(true);
                break;
            case R.id.spinner_hour_graph:
                hour=dropdownHour.getSelectedItem().toString();
                buttonCreateGraph.setEnabled(true);
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}