package com.magisterka.geolokalizator_client.datacollection;

import android.content.Context;
import android.database.Cursor;

import com.magisterka.geolokalizator_client.database.DatabaseSynchronization;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;
import com.magisterka.geolokalizator_client.database.DbContext;
import com.magisterka.geolokalizator_client.callserverapi.ApiCallSynchronization;
import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDataModel;
import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDateTimeModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SynchronizationThread extends Thread {

    private List<SynchronizationDateTimeModel> synchronizationModel;
    private Context context;
    private DatabaseSynchronization databaseSynchronization;
    private static String BASE_URL = "http://192.168.1.74/geolokalizator/synchronization/";
    private ApiCallSynchronization apiCallSynchronization;
    private AccountInfoHelper accountInfoHelper;

    public SynchronizationThread(Context newContext)
    {
        context = newContext;
        databaseSynchronization = new DatabaseSynchronization(context);
        accountInfoHelper = new AccountInfoHelper(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallSynchronization = retrofit.create(ApiCallSynchronization.class);

    }


    private void postData(SynchronizationDateTimeModel dateTimeModel)
    {
        String lastSynchronizationDate = dateTimeModel.getDateTime();
        String lastSynchroTimeZone = dateTimeModel.getTimeZone();

        String token = accountInfoHelper.getToken();

        List<SynchronizationDataModel> model = databaseSynchronization.getDataForSynchronization(lastSynchronizationDate,lastSynchroTimeZone);

        Call<String> call = apiCallSynchronization.PostData(token,model);
        callDataHandler(call);
    }


    @Override
    public void run() {
        //TODO add info to service
        //TODO add list of timezones

        //postTimeZones()

        getLastSynchronizationDate();




        if(!synchronizationModel.isEmpty())
        {
            for(int i =0;i > synchronizationModel.size();i++)
            {
                postData(synchronizationModel.get(i));
            }

        }


    }

    private void getLastSynchronizationDate() {

        String token = accountInfoHelper.getToken();

        Call<List<SynchronizationDateTimeModel>> call = apiCallSynchronization.GetLastSynchronizationByUser(token);

        callLastSynchronizationHandler(call);


    }


    private void accountCheck()
    {

    }


    private void callDataHandler(Call<String> call)
    {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.code() != 200) { return; }//TODO Add info about error
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) { }

        });
    }

    private void callLastSynchronizationHandler(Call<List<SynchronizationDateTimeModel>> call)
    {
        call.enqueue(new Callback<List<SynchronizationDateTimeModel>>() {
            @Override
            public void onResponse(Call<List<SynchronizationDateTimeModel>> call, Response<List<SynchronizationDateTimeModel>> response) {

                if(response.code() != 200) { return; }//TODO Add info about error

                //List<SynchronizationDataModel> dataFromCall = response.body();
            }

            @Override
            public void onFailure(Call<List<SynchronizationDateTimeModel>> call, Throwable t) { }

        });
    }

}
