package com.magisterka.geolokalizator_client.callserverapi;

import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataGraphModel;
import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataMapModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiCallAccessData {

    @GET("map/hour")
    Call<List<HourDataMapModel>> getMapDataByHour(@Header ("Authorization") String Token, @Query("year") int year, @Query("month") int month, @Query("day") int day, @Query("hour") int hour);

    @GET("graph/hour")
    Call<List<HourDataGraphModel>> getGraphDataByHour(@Header ("Authorization") String Token,@Query("year") int year,@Query("month") int month,@Query("day") int day,@Query("hour") int hour);
}
