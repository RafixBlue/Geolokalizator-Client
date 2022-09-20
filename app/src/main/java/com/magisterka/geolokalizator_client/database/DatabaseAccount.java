package com.magisterka.geolokalizator_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccount {

    private static DbContext dbContext;

    public DatabaseAccount(Context context)
    {
        dbContext = new DbContext(context);
    }

    public int insertUser(int roleID, String Name)
    {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Role",roleID);
        cv.put("Name",Name);

        long result = db.insert("user",null, cv);

        if(result!=-1) {
            int user = dbContext.selectTopID("user");
            return user;
        }

        return 0;
    }

    public boolean userExistCheck(String userName)
    {
        SQLiteDatabase sqLiteDatabase = dbContext.getWritableDatabase();

        String Query = "Select * from user where Name = '" + userName+ "'";

        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

}
