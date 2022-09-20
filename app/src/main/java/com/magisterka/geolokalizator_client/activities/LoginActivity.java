package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;
import com.magisterka.geolokalizator_client.callserverapi.ApiCallAccount;
import com.magisterka.geolokalizator_client.database.DbContext;
import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.models.accountmodels.AccountInfoModel;
import com.magisterka.geolokalizator_client.models.accountmodels.LoginModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static String BASE_URL = "http://192.168.1.74/geolokalizator/account/";
    private AccountInfoHelper accountInfoHelper;
    private DbContext db;
    private ApiCallAccount apiCallAccount;
    private EditText Login;
    private EditText Password;
    private SharedPreferences accountPreferences;
    TextView x;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountInfoHelper = new AccountInfoHelper(this);

        db=new DbContext(this);

        OkHttpClient client = RetrofitLogging();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallAccount = retrofit.create(ApiCallAccount.class);

        Login = findViewById(R.id.LoginText);
        Password = findViewById(R.id.PasswordText);
        x = findViewById(R.id.textViewx);

        register = findViewById(R.id.button_register);
        register.setTransformationMethod(null);

    }

    private OkHttpClient RetrofitLogging()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return client;
    }

    public void Register(View view) {

        //String test = accountPreferences.getString(TOKEN,"token");
        //Toast.makeText(getApplicationContext(), "what", Toast.LENGTH_LONG).show();
        //int a = 10;
        Intent intent = new Intent(LoginActivity.this, ServiceActivity.class);
        startActivity(intent);
    }

    public void Login(View view) {

        LoginModel model=new LoginModel();

        String userLogin = Login.getText().toString();
        model.setName(userLogin);

        model.setPassword(Password.getText().toString());

        Call<String> call = apiCallAccount.PostData(model);
        callTokenHandler(call, userLogin);

    }

    private void callTokenHandler(Call<String> call,String userLogin)
    {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.code() != 200) {
                    return; }//TODO Add info about error

                String token = response.body();

                if(!token.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "You loged in", Toast.LENGTH_LONG).show();
                    accountInfoHelper.saveToken(token);
                    GetAccountInfo(userLogin);
                    return;
                }

                Toast.makeText(getApplicationContext(), "Wrong Login or Password", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void GetAccountInfo(String userLogin)
    {
        String token = accountInfoHelper.getToken();

        Call<AccountInfoModel> call = apiCallAccount.GetAccountInfo(token);
        callAccountInfoHandler(call);
    }

    private void callAccountInfoHandler(Call<AccountInfoModel> call)
    {
        call.enqueue(new Callback<AccountInfoModel>() {
            @Override
            public void onResponse(Call<AccountInfoModel> call, Response<AccountInfoModel> response) {

                if(response.code() != 200) {
                    return; }//TODO Add info about error

                AccountInfoModel accountInfo = response.body();

                accountInfoHelper.saveAccountInfo(accountInfo);
                accountInfoHelper.CreateNewUser();
                return;

            }

            @Override
            public void onFailure(Call<AccountInfoModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }



}

