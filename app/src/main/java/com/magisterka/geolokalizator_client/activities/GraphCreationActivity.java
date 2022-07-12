package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.GraphDataSet;
import com.magisterka.geolokalizator_client.R;

public class GraphCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Database database;

    Switch switchRSRP;
    Switch switchRSRQ;
    Switch switchRSSI;
    Switch switchRSSNR;

    Spinner dropdownYear;
    Spinner dropdownMonth;
    Spinner dropdownDay;
    Spinner dropdownHour;

    Button buttonCreateGraph;

    GraphDataSet graphDataSet;

    String year;
    String month;
    String day;
    String hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_creation);

        database = new Database(this);

        buttonCreateGraph = findViewById(R.id.button_graph);

        switchRSRP = findViewById(R.id.switch_rsrp);
        switchRSRQ = findViewById(R.id.switch_rsrq);
        switchRSSI = findViewById(R.id.switch_rssi);
        switchRSSNR = findViewById(R.id.switch_rssnr);

        dropdownYear = findViewById(R.id.spinner_year);
        dropdownMonth = findViewById(R.id.spinner_month);
        dropdownDay = findViewById(R.id.spinner_day);
        dropdownHour = findViewById(R.id.spinner_hour);

        buttonCreateGraph.setEnabled(false);
        dropdownMonth.setEnabled(false);
        dropdownDay.setEnabled(false);
        dropdownHour.setEnabled(false);

        graphDataSet = new GraphDataSet();

        dropdownYear.setOnItemSelectedListener(this);
        dropdownMonth.setOnItemSelectedListener(this);
        dropdownDay.setOnItemSelectedListener(this);
        dropdownHour.setOnItemSelectedListener(this);


        fillDropdownYear();


    }


    private void fillDropdownYear()
    {
        String[] items = graphDataSet.getDropdownFiller(database.getAvailableYear(1));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownYear.setAdapter(adapter);
    }

    private void fillDropdownMonth()
    {

        String[] items = graphDataSet.getDropdownFiller(database.getAvailableMonth(1,year));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownMonth.setAdapter(adapter);
    }

    private void fillDropdownDay()
    {

        String[] items = graphDataSet.getDropdownFiller(database.getAvailableDay(1,year,month));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownDay.setAdapter(adapter);
    }

    private void fillDropdownHour()
    {

        String[] items = graphDataSet.getDropdownFiller(database.getAvailableHour(1,year,month,day));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdownHour.setAdapter(adapter);
    }

    public void createGraph(View view) {

        Intent intent = new Intent(GraphCreationActivity.this, GraphActivity.class);
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        intent.putExtra("day",day);
        intent.putExtra("hour",hour);
        intent.putExtra("series",getSwitchStates());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spinner_year:
                year = dropdownYear.getSelectedItem().toString();
                fillDropdownMonth();
                dropdownMonth.setEnabled(true);
                break;
            case R.id.spinner_month:
                month=dropdownMonth.getSelectedItem().toString();
                fillDropdownDay();
                dropdownDay.setEnabled(true);
                break;
            case R.id.spinner_day:
                day=dropdownDay.getSelectedItem().toString();
                fillDropdownHour();
                dropdownHour.setEnabled(true);
                break;
            case R.id.spinner_hour:
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

    boolean[] getSwitchStates()
    {
        boolean[] switchStates = new boolean[4];

        switchStates[0] = switchRSRP.isChecked();
        switchStates[1] = switchRSRQ.isChecked();
        switchStates[2] = switchRSSI.isChecked();
        switchStates[3] = switchRSSNR.isChecked();

        return switchStates;
    }


}