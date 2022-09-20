package com.magisterka.geolokalizator_client.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.magisterka.geolokalizator_client.database.parser.MapParser;
import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataMapModel;

import java.util.ArrayList;
import java.util.List;

import static com.magisterka.geolokalizator_client.database.parser.MapParser.cursorToMapModel;

public class DatabaseMap {

    private static DbContext dbContext;

    public DatabaseMap(Context context)
    {
        dbContext = new DbContext(context);
    }

    public List<HourDataMapModel> getHourMeasurementWithLocation(String year, String month, String day, String hour)
    {
        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
        //TODO add timezone
        Cursor cursor = sqLiteDatabase.rawQuery(
                "Select pd.ID,strftime('%H %M',l.DateTime),l.Latitude, l.Longitude, l.Altitude, l.Accuracy, " +
                        "s.Network_Provider,s.Network_Type,s.RSRP,s.RSRQ,s.RSSI,s.RSSNR " +
                        "From location l,user_data pd Inner Join signal s On pd.Location_ID=l.ID AND s.ID=pd.Signal_ID Where l.ID IN (" +
                        "Select ID From location Where strftime('%Y %m %d %H',DateTime) = '" +year+ " " +month+ " " +day+" "+hour+"') " +
                        "Order BY l.DateTime",null);

        cursor.moveToFirst();

        List<HourDataMapModel> dataMapModels = cursorToMapModel(cursor);

        cursor.close();

        return dataMapModels;
    }




}
