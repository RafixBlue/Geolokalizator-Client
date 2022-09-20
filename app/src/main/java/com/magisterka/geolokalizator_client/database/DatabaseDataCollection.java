package com.magisterka.geolokalizator_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;

public class DatabaseDataCollection {

    private static DbContext dbContext;
    private static AccountInfoHelper accountInfoHelper;

    public DatabaseDataCollection(Context context)
    {
        dbContext = new DbContext(context);
        accountInfoHelper = new AccountInfoHelper(context);
    }

    public boolean insertCollectedData(ContentValues locationData,ContentValues signalData)
    {
        boolean successfullyInserted;

        if(signalData.isEmpty()) { return false; }
        if(locationData.isEmpty()) { return false; }

        successfullyInserted=dbContext.insert("location",locationData);

        if(!successfullyInserted) {
            return false;
        }

        successfullyInserted=dbContext.insert("signal",signalData);

        if(!successfullyInserted){
            dbContext.deleteLastRecord("location");
            return false;
        }

        successfullyInserted= insertUserData();

        if(!successfullyInserted){
            dbContext.deleteLastRecord("signal");
            dbContext.deleteLastRecord("location");
            return false;
        }

        return true;
    }

    private boolean insertUserData()
    {
        int userId = accountInfoHelper.getClientUserId();

        int newSignalId = dbContext.selectTopID("signal");
        int newLocationId = dbContext.selectTopID("location");

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_ID",userId);
        values.put("Signal_ID",newSignalId);
        values.put("Location_ID",newLocationId);

        long result = sqLiteDatabase.insert("user_data",null, values);
        if(result==-1) {
            return false; }
        else {
            return true; }
    }
}
