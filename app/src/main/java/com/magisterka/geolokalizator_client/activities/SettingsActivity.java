package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.magisterka.geolokalizator_client.database.DatabaseSynchronization;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;
import com.magisterka.geolokalizator_client.callserverapi.ApiCallSynchronization;
import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.sharedpreferences.SettingsHelper;
import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDataModel;
import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDateTimeModel;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static String BASE_URL = "http://192.168.1.74/geolokalizator/synchronization/";
    private DatabaseSynchronization databaseSynchronization;
    private Spinner dropdown;
    private Button button;
    private SettingsHelper settingsHelper;
    private AccountInfoHelper accountInfoHelper;
    private ApiCallSynchronization apiCallSynchronization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        accountInfoHelper = new AccountInfoHelper(this);

        button = findViewById(R.id.buttonMSynchro);

        dropdown = findViewById(R.id.spinner);

        dropdown.setOnItemSelectedListener(this);

        settingsHelper = new SettingsHelper();

        databaseSynchronization = new DatabaseSynchronization(this);

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




    private void initDataAccessApi()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiCallSynchronization = retrofit.create(ApiCallSynchronization.class);
    }
    
    private void CallRetrofit()
    {

        String token = accountInfoHelper.getToken();

        List<SynchronizationDataModel> model = databaseSynchronization.getDataForSynchronization("2000:01:01 11",null);
        
        Call<String> call = apiCallSynchronization.PostData(token,model);

        emptyCallHandler(call);

    }

    private void emptyCallHandler(Call<String> call)
    {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.code() != 200) {
                    Log.e("retrofit",response.errorBody().toString());
                    return; }

                Log.e("retrofit",response.body().toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }

        });
    }


    public void manualSynchronization(View view) {

        sendTimeZones();

        getTimeZones();
        //CallRetrofit();
    }

    public void getTimeZones()//List<SynchronizationDateTimeModel>
    {
        String token = accountInfoHelper.getToken();

        Call<List<SynchronizationDateTimeModel>> call = apiCallSynchronization.getLastSynchronizationDateTime(token);

        synchronizationTimeCallHandler(call);


    }

    public void sendTimeZones()
    {
        String token = accountInfoHelper.getToken();
        int userId = accountInfoHelper.getClientUserId();
        List<String> timeZonesList = databaseSynchronization.getTimeZones();
        
        Call<String> call = apiCallSynchronization.PostTimeZone(token,timeZonesList);

        emptyCallHandler(call);
    }


    private void synchronizationTimeCallHandler(Call<List<SynchronizationDateTimeModel>> call)
    {
        call.enqueue(new Callback<List<SynchronizationDateTimeModel>>() {
            @Override
            public void onResponse(Call<List<SynchronizationDateTimeModel>> call, Response<List<SynchronizationDateTimeModel>> response) {

                if(response.code() != 200) {
                    Log.e("retrofit",response.errorBody().toString());
                    return; }

                List<SynchronizationDateTimeModel> synchronizationDate = response.body();

            }

            @Override
            public void onFailure(Call<List<SynchronizationDateTimeModel>> call, Throwable t) {
            }

        });
    }


    
}

