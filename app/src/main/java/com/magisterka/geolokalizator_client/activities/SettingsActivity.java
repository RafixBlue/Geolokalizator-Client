package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.SettingsHelper;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner dropdown;
    private SettingsHelper settingsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dropdown = findViewById(R.id.spinner);

        dropdown.setOnItemSelectedListener(this);

        settingsHelper = new SettingsHelper();

        fillDropdown();

    }

    private void fillDropdown()
    {
        String[] items = new String[]{"Change Selected Time","1 min","2 min","5 min","10 min","15 min"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch(position){
            case 1:
                Toast.makeText(this, "Time interval changed to 1 min", Toast.LENGTH_SHORT).show();
                settingsHelper.saveSettingTimeInterval(1,this);
                break;
            case  2:
                Toast.makeText(this, "Time interval changed to 2 min", Toast.LENGTH_SHORT).show();
                settingsHelper.saveSettingTimeInterval(2,this);
                break;
            case  3:
                Toast.makeText(this, "Time interval changed to 5 min", Toast.LENGTH_SHORT).show();
                settingsHelper.saveSettingTimeInterval(5,this);
                break;
            case  4:
                Toast.makeText(this, "Time interval changed to 10 min", Toast.LENGTH_SHORT).show();
                settingsHelper.saveSettingTimeInterval(10,this);
                break;
            case  5:
                Toast.makeText(this, "Time interval changed to 15 min", Toast.LENGTH_SHORT).show();
                settingsHelper.saveSettingTimeInterval(15,this);
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

