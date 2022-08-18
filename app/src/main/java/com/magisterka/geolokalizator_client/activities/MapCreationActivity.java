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

import com.magisterka.geolokalizator_client.CallServerApi.ApiCallTime;
import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.MapDataEditor;
import com.magisterka.geolokalizator_client.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Database database;
    ApiCallTime apiCallTime;

    Map<String, Spinner> dropdownMap;

    private static String BASE_URL = "http://192.168.1.74/geolokalizator/time/";
    private static boolean DATA_FROM_ONLINE_DATABASE = false;

    Spinner dropdownYear;
    Spinner dropdownMonth;
    Spinner dropdownDay;
    Spinner dropdownHour;

    Button buttonCreateMap;

    MapDataEditor mapDataEditor;

    String year;
    String month;
    String day;
    String hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_creation);

        initClasses();

        initLayout();

        disableOnStart();

        setItemSelection();

        mapDropdowns();

        if(DATA_FROM_ONLINE_DATABASE) {
            initDataAccessApi();
        }

        setDataForDropdownYear("year");

    }

    private void mapDropdowns()
    {
        dropdownMap = new HashMap<String, Spinner>();

        dropdownMap.put("year", findViewById(R.id.spinner_year_map));
        dropdownMap.put("month", findViewById(R.id.spinner_month_map));
        dropdownMap.put("day", findViewById(R.id.spinner_day_map));
        dropdownMap.put("hour", findViewById(R.id.spinner_hour_map));

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

        mapDataEditor = new MapDataEditor();
    }

    private void initLayout()
    {
        dropdownYear = findViewById(R.id.spinner_year_map);
        dropdownMonth = findViewById(R.id.spinner_month_map);
        dropdownDay = findViewById(R.id.spinner_day_map);
        dropdownHour = findViewById(R.id.spinner_hour_map);
        buttonCreateMap = findViewById(R.id.button_create_map);
    }

    private void disableOnStart()
    {
        buttonCreateMap.setEnabled(false);
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setDataForDropdownYear(String dropdownType)
    {
        if(DATA_FROM_ONLINE_DATABASE) {
            Call<String[]> call = apiCallTime.getAvailableYears(1);
            callDropdownHandler(call,"year");
            return;
        }

        Cursor rawDropdownData = database.getAvailableYear(1);
        String[] dropdownData = mapDataEditor.getDropdownFiller(rawDropdownData);
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
        String[] dropdownData = mapDataEditor.getDropdownFiller(rawDropdownData);
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
        String[] dropdownData = mapDataEditor.getDropdownFiller(rawDropdownData);
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
        String[] dropdownData = mapDataEditor.getDropdownFiller(rawDropdownData);
        fillDropdownFromServer(dropdownHour, dropdownData);
    }

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

    public void createMap(View view) {

        Intent intent = new Intent(MapCreationActivity.this, MapActivity.class);
        intent.putExtra("dataFromOnlineDatabase",DATA_FROM_ONLINE_DATABASE);
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        intent.putExtra("day",day);
        intent.putExtra("hour",hour);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spinner_year_map:
                year = dropdownYear.getSelectedItem().toString();
                setDataForDropdownMonth();
                dropdownMonth.setEnabled(true);
                break;
            case R.id.spinner_month_map:
                month=dropdownMonth.getSelectedItem().toString();
                setDataForDropdownDay();
                dropdownDay.setEnabled(true);
                break;
            case R.id.spinner_day_map:
                day=dropdownDay.getSelectedItem().toString();
                setDataForDropdownHour();
                dropdownHour.setEnabled(true);
                break;
            case R.id.spinner_hour_map:
                hour=dropdownHour.getSelectedItem().toString();
                buttonCreateMap.setEnabled(true);
                break;
            default:
                break;
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}