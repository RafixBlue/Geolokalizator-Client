package com.magisterka.geolokalizator_client.database.parser;

import android.database.Cursor;

public class DateDropdownParser {

    public static String[] getDropdownArray(Cursor cursor)
    {
        cursor.moveToFirst();
        int fillerSize = cursor.getCount();

        String[] filler = new String[fillerSize];

        for(int i =0; i < fillerSize;i++)
        {
            filler[i] = cursor.getString(0);
            cursor.moveToNext();

        }

        return filler;
    }
}
