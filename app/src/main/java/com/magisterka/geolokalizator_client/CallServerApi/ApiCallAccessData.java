package com.magisterka.geolokalizator_client.CallServerApi;

import com.magisterka.geolokalizator_client.models.HourDataGraphModel;
import com.magisterka.geolokalizator_client.models.HourDataMapModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallAccessData {

    @GET("map/hour")
    Call<List<HourDataMapModel>> getMapDataByHour(@Query("userId") int userId,@Query("year") int year,@Query("month") int month,@Query("day") int day,@Query("hour") int hour);

    @GET("graph/hour")
    Call<List<HourDataGraphModel>> getGraphDataByHour(@Query("userId") int userId,@Query("year") int year,@Query("month") int month,@Query("day") int day,@Query("hour") int hour);
}
