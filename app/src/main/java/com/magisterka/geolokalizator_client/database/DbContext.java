package com.magisterka.geolokalizator_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbContext extends SQLiteOpenHelper{

    public DbContext(Context context) {
        super(context, "BazaDanych.db", null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create Table user" +
                "(ID INT primary key autoincrement," +
                "Name TEXT," +
                "Role INTEGER )");

        sqLiteDatabase.execSQL("create Table location" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Latitude TEXT, " +
                "Altitude TEXT, " +
                "Longitude TEXT," +
                "DateTime TEXT," +
                "Accuracy TEXT," +
                "Time_Zone TEXT)");

        sqLiteDatabase.execSQL("create Table signal" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Network_Provider TEXT, " +
                "Network_Type TEXT, " +
                "RSSI TEXT," +
                "RSRP TEXT," +
                "RSRQ TEXT," +
                "RSSNR TEXT)");

        sqLiteDatabase.execSQL("create Table user_data" +
                "(ID INTEGER PRIMARY KEY autoincrement," +
                "User_ID INT, " +
                "Signal_ID INT, " +
                "Location_ID INT, " +
                "FOREIGN KEY (user_ID) REFERENCES user (ID)," +
                "FOREIGN KEY (Signal_ID) REFERENCES signal (ID)," +
                "FOREIGN KEY (Location_ID) REFERENCES location (ID))");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists user");
        sqLiteDatabase.execSQL("drop Table if exists location");
        sqLiteDatabase.execSQL("drop Table if exists signal");
        sqLiteDatabase.execSQL("drop Table if exists user_data");

        sqLiteDatabase.execSQL("create Table user" +
                "(ID INT primary key autoincrement," +
                "Name TEXT," +
                "Role INTEGER)");

        sqLiteDatabase.execSQL("create Table location" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Latitude TEXT, " +
                "Altitude TEXT, " +
                "Longitude TEXT," +
                "DateTime TEXT," +
                "Accuracy TEXT," +
                "Time_Zone TEXT)");

        sqLiteDatabase.execSQL("create Table signal" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Network_Provider TEXT, " +
                "Network_Type TEXT, " +
                "RSSI TEXT," +
                "RSRP TEXT," +
                "RSRQ TEXT," +
                "RSSNR TEXT)");

        sqLiteDatabase.execSQL("create Table user_data" +
                "(ID INTEGER PRIMARY KEY autoincrement," +
                "user_ID INT, " +
                "Signal_ID INT, " +
                "Location_ID INT, " +
                "FOREIGN KEY (user_ID) REFERENCES user (ID)," +
                "FOREIGN KEY (Signal_ID) REFERENCES signal (ID)," +
                "FOREIGN KEY (Location_ID) REFERENCES location (ID))");

        sqLiteDatabase.execSQL("INSERT INTO signal(ID,Network_Provider,Network_Type,RSSI,RSRP,RSRQ,RSSNR) VALUES (0,'None','None','0','0','0','0')");
        //insertuser(1,"test");
    }

    public boolean insert(String tableName,ContentValues values)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(tableName,null, values);

        if(result==-1) {
            return false; }
        else {
            return true; }
    }

    public int selectTopID(String table)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor =sqLiteDatabase.rawQuery("Select MAX(ID) From '"+table+"'",null);
        cursor.moveToFirst();
        int data = cursor.getInt(cursor.getColumnIndexOrThrow("MAX(ID)"));
        cursor.close();
        return data;
    }

    public void deleteLastRecord(String table)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM "+table+" WHERE ID=(SELECT MAX(ID) FROM "+table+")");

    }













}
