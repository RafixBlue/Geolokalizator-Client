package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.magisterka.geolokalizator_client.database.DatabaseDateDropdownFiller;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;
import com.magisterka.geolokalizator_client.callserverapi.ApiCallTime;
import com.magisterka.geolokalizator_client.activities.datahandling.MapDataEditor;
import com.magisterka.geolokalizator_client.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private DatabaseDateDropdownFiller database;
    private ApiCallTime apiCallTime;

    private Map<String, Spinner> dropdownMap;

    private int clientUserId;
    private String token;

    private static String BASE_URL = "http://192.168.1.74/geolokalizator/time/";
    private static boolean DATA_FROM_ONLINE_DATABASE;
    private boolean dataFromOnlineDatabase;
    private AccountInfoHelper accountInfoHelper;

    private Spinner dropdownYear;
    private Spinner dropdownMonth;
    private Spinner dropdownDay;
    private Spinner dropdownHour;

    private Switch switchOnlineData;

    private Button buttonCreateMap;

    private MapDataEditor mapDataEditor;

    private String year;
    private String month;
    private String day;
    private String hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_creation);

        initClasses();

        initInfo();

        initLayout();

        disableOnStart();

        setItemSelection();

        OnCheckedChangeListener();

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
        OkHttpClient client = RetrofitLogging();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallTime = retrofit.create(ApiCallTime.class);
    }

    private OkHttpClient RetrofitLogging()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return client;
    }

    private void initInfo()
    {
        clientUserId = accountInfoHelper.getClientUserId();
        token = accountInfoHelper.getToken();
    }

    private void initClasses()
    {
        database = new DatabaseDateDropdownFiller(this);

        mapDataEditor = new MapDataEditor();

        accountInfoHelper = new AccountInfoHelper(this);
    }

    private void initLayout()
    {
        switchOnlineData = findViewById((R.id.switch_online_map));
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
        if(dataFromOnlineDatabase) {
            Call<String[]> call = apiCallTime.getAvailableYears(token);
            callDropdownHandler(call,"year");
            return;
        }

        String[] dropdownArray = database.getAvailableYear();
        fillDropdown(dropdownYear, dropdownArray);
    }

    private void setDataForDropdownMonth()
    {
        if(dataFromOnlineDatabase) {
            Call<String[]> call = apiCallTime.getAvailableMonths(token, Integer.parseInt(year));
            callDropdownHandler(call,"month");
            return;
        }

        String[] dropdownArray = database.getAvailableMonth(year);
        fillDropdown(dropdownMonth, dropdownArray);
    }

    private void setDataForDropdownDay()
    {
        if(dataFromOnlineDatabase) {
            Call<String[]> call = apiCallTime.getAvailableDays(token, Integer.parseInt(year),Integer.parseInt(month));
            callDropdownHandler(call,"day");
            return;
        }

        String[] dropdownArray = database.getAvailableDay(year,month);
        fillDropdown(dropdownDay, dropdownArray);
    }

    private void setDataForDropdownHour()
    {
        if(dataFromOnlineDatabase) {
            Call<String[]> call = apiCallTime.getAvailableHours(token, Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
            callDropdownHandler(call,"hour");
            return;
        }

        String[] dropdownArray = database.getAvailableHour(year,month,day);
        fillDropdown(dropdownHour, dropdownArray);
    }

    private void fillDropdown(Spinner dropdown, String[] items)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, items);
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

                fillDropdown(dropdown,dataFromCall);


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

    public void OnCheckedChangeListener()
    {
        switchOnlineData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataFromOnlineDatabase = switchOnlineData.isChecked();
                setDataForDropdownYear("year");
            }
        });
    }



}