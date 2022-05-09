package com.magisterka.geolokalizator_client;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LoginRegisterActivity extends AppCompatActivity {

    DataCollectorLocation dataCollector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        dataCollector = new DataCollectorLocation(this);
    }
}