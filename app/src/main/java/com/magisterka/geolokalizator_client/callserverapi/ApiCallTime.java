package com.magisterka.geolokalizator_client.callserverapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiCallTime {

    @GET("year")
    Call<String[]> getAvailableYears(@Header("Authorization") String Token);

    @GET("month")
    Call<String[]> getAvailableMonths(@Header ("Authorization") String Token,@Query("year") int year);

    @GET("day")
    Call<String[]> getAvailableDays(@Header ("Authorization") String Token,@Query("year") int year,@Query("month") int month);

    @GET("hour")
    Call<String[]> getAvailableHours(@Header ("Authorization") String Token,@Query("year") int year,@Query("month") int month,@Query("day") int day);

}
