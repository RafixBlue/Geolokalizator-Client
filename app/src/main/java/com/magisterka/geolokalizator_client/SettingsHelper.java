package com.magisterka.geolokalizator_client;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SettingsHelper {

    public static final String PREFERENCES = "preferences";
    public static final String TIME_INTERVALS = "timeIntervals";

    public void saveSettingTimeInterval(int time, Context context)
    {

        SharedPreferences settingsPreferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settingsPreferences.edit();

        settingsEditor.putInt(TIME_INTERVALS, time);
        settingsEditor.commit();

    }

    public int LoadSettingTimeInterval(Context context) {
        SharedPreferences settingsPreferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settingsPreferences.edit();

        return settingsPreferences.getInt(TIME_INTERVALS, 1);
    }


}
