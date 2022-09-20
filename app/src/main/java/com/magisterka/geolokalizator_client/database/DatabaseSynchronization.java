package com.magisterka.geolokalizator_client.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.magisterka.geolokalizator_client.database.parser.SynchronizationParser;
import com.magisterka.geolokalizator_client.models.synchronizationmodel.SynchronizationDataModel;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;

import java.util.ArrayList;
import java.util.List;

import static com.magisterka.geolokalizator_client.database.parser.SynchronizationParser.cursorToSynchronizationModels;
import static com.magisterka.geolokalizator_client.database.parser.SynchronizationParser.cursorToTimeZoneList;

public class DatabaseSynchronization {

    private static DbContext dbContext;
    private AccountInfoHelper accountInfoHelper;

    public DatabaseSynchronization(Context context)
    {
        dbContext = new DbContext(context);

    }


    public List<SynchronizationDataModel> getDataForSynchronization(String lastSynchronizationDate, String lastSynchroTimeZone)
    {
        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

        //TODO add timezone
        Cursor cursor = sqLiteDatabase.rawQuery(
                "Select pd.user_ID,l.DateTime,l.Latitude, l.Longitude, l.Altitude, l.Accuracy, " +
                        "s.Network_Provider,s.Network_Type,s.RSRP,s.RSRQ,s.RSSI,s.RSSNR  " +
                        "From location l,user_data pd Inner Join signal s On pd.Location_ID=l.ID AND s.ID=pd.Signal_ID  " +
                        "Where strftime('%Y %m %d %H',DateTime) > '"+lastSynchronizationDate +"' " + //"AND l.Time_Zone = '"+ lastSynchroTimeZone + "' " +
                        "Group BY l.DateTime " +
                        "Order BY l.DateTime " ,null);

        List<SynchronizationDataModel> models = cursorToSynchronizationModels(cursor);

        cursor.close();

        return models;
    }


    public List<String> getTimeZones()
    {
        int userId = accountInfoHelper.getClientUserId();

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

        String Query ="Select l.Time_Zone from location l, user_data ud Where ud.User_ID = "+userId+" AND ud.Location_ID = l.ID Group by l.Time_Zone";

        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        List<String> cursorToTimeZoneList = cursorToTimeZoneList(cursor);

        cursor.close();

        return cursorToTimeZoneList;
    }


}
