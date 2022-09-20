package com.magisterka.geolokalizator_client.callserverapi;

import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDataModel;
import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDateTimeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiCallSynchronization {

    @GET("get/last-date")
    Call<List<SynchronizationDateTimeModel>> GetLastSynchronizationByUser(@Header("Authorization") String Token);

    @GET("get/data")
    Call<List<SynchronizationDataModel>> GetData(@Header ("Authorization") String Token, @Body SynchronizationDateTimeModel model);

    @POST("insert/data")
    Call <String> PostData(@Header ("Authorization") String Token,@Body List<SynchronizationDataModel> model);

    @POST("insert/timezone")
    Call <String> PostTimeZone(@Header ("Authorization") String Token,@Body List<String> TimeZones);

    @GET("get/timezone")
    Call <List<SynchronizationDateTimeModel>> getLastSynchronizationDateTime(@Header ("Authorization") String Token );
}
