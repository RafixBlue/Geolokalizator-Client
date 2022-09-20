package com.magisterka.geolokalizator_client.callserverapi;

import com.magisterka.geolokalizator_client.models.accountmodels.AccountInfoModel;
import com.magisterka.geolokalizator_client.models.accountmodels.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiCallAccount {

    @POST("login")
    Call<String> PostData(@Body LoginModel model);

    @GET("info")
    Call<AccountInfoModel> GetAccountInfo(@Header("Authorization") String Token);
}
