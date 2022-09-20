package com.magisterka.geolokalizator_client.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.magisterka.geolokalizator_client.database.parser.DateDropdownParser;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;

import static com.magisterka.geolokalizator_client.database.parser.DateDropdownParser.getDropdownArray;

public class DatabaseDateDropdownFiller {
    private static DbContext dbContext;
    private static AccountInfoHelper accountInfoHelper;


    public DatabaseDateDropdownFiller(Context context)
    {
        dbContext = new DbContext(context);

        accountInfoHelper = new AccountInfoHelper(context);
    }
    //TODO add timezones
    public String[] getAvailableYear()
    {
        int userId = accountInfoHelper.getClientUserId();

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%Y',l.DateTime) FROM location l, user_data pd WHERE user_ID = '" + userId +"'GROUP BY strftime('%Y',l.DateTime)",null);

        String[] dropdownArray = getDropdownArray(cursor);

        cursor.close();

        return dropdownArray;

    }

    public String[] getAvailableMonth(String year)
    {
        int userId = accountInfoHelper.getClientUserId();

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%m',l.DateTime) FROM location l, user_data pd WHERE pd.user_ID = " + userId + " AND strftime('%Y',l.DateTime) = '"+year+"' GROUP BY strftime('%m',l.DateTime)",null);

        String[] dropdownArray = getDropdownArray(cursor);

        cursor.close();

        return dropdownArray;

    }

    public String[] getAvailableDay(String year,String month)
    {
        int userId = accountInfoHelper.getClientUserId();

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%d',l.DateTime) FROM location l, user_data pd WHERE pd.user_ID = " + userId + " AND strftime('%Y',l.DateTime) = '"+year+"' AND strftime('%m',l.DateTime) = '"+month+"' GROUP BY strftime('%d',l.DateTime)",null);

        String[] dropdownArray = getDropdownArray(cursor);

        cursor.close();

        return dropdownArray;

    }

    public String[] getAvailableHour(String year,String month,String day)
    {
        int userId = accountInfoHelper.getClientUserId();

        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%H',l.DateTime) FROM location l, user_data pd WHERE pd.user_ID = '"+userId+"' AND strftime('%Y',l.DateTime) = '"+year+"' AND strftime('%m',l.DateTime) = '"+month+"' AND strftime('%d',l.DateTime) = '"+day+"' GROUP BY strftime('%H',l.DateTime)",null);

        String[] dropdownArray = getDropdownArray(cursor);

        cursor.close();

        return dropdownArray;

    }


}
