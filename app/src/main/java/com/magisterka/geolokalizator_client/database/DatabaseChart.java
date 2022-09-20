package com.magisterka.geolokalizator_client.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataGraphModel;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;

import java.util.List;

import static com.magisterka.geolokalizator_client.database.parser.ChartParser.cursorToGraphModels;
import static com.magisterka.geolokalizator_client.database.parser.ChartParser.getNumberOfMeasurementsWeek;

public class DatabaseChart {

    private static DbContext dbContext;
    private static AccountInfoHelper accountInfoHelper;

    public DatabaseChart(Context context)
    {
        dbContext = new DbContext(context);
        accountInfoHelper = new AccountInfoHelper(context);
    }

    public List<HourDataGraphModel> getHourMeasurement(String year, String month, String day, String hour)
    {
        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

        //TODO add timezone
        Cursor cursor = sqLiteDatabase.rawQuery("Select pd.ID,strftime('%M',l.DateTime), l.Accuracy, s.Network_Provider,s.Network_Type,s.RSRP,s.RSRQ,s.RSSI,s.RSSNR " +
                "From location l,user_data pd Inner Join signal s On pd.Location_ID=l.ID AND s.ID=pd.Signal_ID Where l.ID IN (" +
                "Select ID From location Where strftime('%Y %m %d %H',DateTime) = '" +year+ " " +month+ " " +day+" "+hour+"') " +
                "Order BY l.DateTime",null);//"Order BY pd.ID",null);

        List<HourDataGraphModel> data = cursorToGraphModels(cursor);

        cursor.close();

        return data;
    }


    public int[] getWeeksMeasurementCount()
    {
        int userId = accountInfoHelper.getClientUserId();

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

        //TODO add timezone
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT COUNT(l.ID) , strftime('%d',l.DateTime) " +
                        "FROM location l, user_data ud " +
                        "WHERE ud.Location_ID = l.ID " +
                        "AND ud.user_ID = " + userId +
                        "AND strftime('%Y %m %d',l.DateTime) >= strftime('%Y %m %d','now','-7 days')"+
                        "Group by strftime('%Y %m %d',l.DateTime)"

                ,null);

        int[] measurementsWeek = getNumberOfMeasurementsWeek(cursor);

        cursor.close();

        return measurementsWeek;
    }


}
