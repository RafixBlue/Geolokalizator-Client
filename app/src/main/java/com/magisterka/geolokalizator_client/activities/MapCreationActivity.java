package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.MapDataEditor;
import com.magisterka.geolokalizator_client.R;

public class MapCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Database database;

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

        fillDropdownYear();
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



    private void fillDropdownYear()
    {
        String[] items = mapDataEditor.getDropdownFiller(database.getAvailableYear(1));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownYear.setAdapter(adapter);
    }

    private void fillDropdownMonth()
    {
        String[] items = mapDataEditor.getDropdownFiller(database.getAvailableMonth(1,year));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownMonth.setAdapter(adapter);
    }

    private void fillDropdownDay()
    {
        String[] items = mapDataEditor.getDropdownFiller(database.getAvailableDay(1,year,month));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownDay.setAdapter(adapter);
    }

    private void fillDropdownHour()
    {
        String[] items = mapDataEditor.getDropdownFiller(database.getAvailableHour(1,year,month,day));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownHour.setAdapter(adapter);
    }


    public void createMap(View view) {
        Intent intent = new Intent(MapCreationActivity.this, MapActivity.class);
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
                fillDropdownMonth();
                dropdownMonth.setEnabled(true);
                break;
            case R.id.spinner_month_map:
                month=dropdownMonth.getSelectedItem().toString();
                fillDropdownDay();
                dropdownDay.setEnabled(true);
                break;
            case R.id.spinner_day_map:
                day=dropdownDay.getSelectedItem().toString();
                fillDropdownHour();
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