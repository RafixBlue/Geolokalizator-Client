package com.magisterka.geolokalizator_client.CallServerApi;

import com.magisterka.geolokalizator_client.models.QueryDateTimeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallTime {

    @GET("year")
    Call<String[]> getAvailableYears(@Query("userId") int userId);

    @GET("month")
    Call<String[]> getAvailableMonths(@Query("userId") int userId,@Query("year") int year);

    @GET("day")
    Call<String[]> getAvailableDays(@Query("userId") int userId,@Query("year") int year,@Query("month") int month);

    @GET("hour")
    Call<String[]> getAvailableHours(@Query("userId") int userId,@Query("year") int year,@Query("month") int month,@Query("day") int day);

}
