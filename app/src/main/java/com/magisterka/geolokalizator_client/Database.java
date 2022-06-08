package com.magisterka.geolokalizator_client;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

    public Database(Context context) {
        super(context, "BazaDanych.db", null, 5);
    }//Konstruktor klasy tworzy instancje bazy danych w plikach wewnetrznych telefonu.

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create Table profile" +
                "(ID INT primary key," +
                "Name TEXT)");

        sqLiteDatabase.execSQL("create Table location" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Latitude TEXT, " +
                "Altitude TEXT, " +
                "Longitude TEXT," +
                "Time TEXT," +
                "Accurency TEXT)");

        sqLiteDatabase.execSQL("create Table signal" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Network_Provider TEXT, " +
                "Network_Type TEXT, " +
                "RSSI TEXT," +
                "RSRP TEXT," +
                "RSRQ TEXT," +
                "RSSNR TEXT)");

        sqLiteDatabase.execSQL("create Table profile_data" +
                "(ID INTEGER PRIMARY KEY autoincrement," +
                "Profile_ID INT, " +
                "Signal_ID INT, " +
                "Location_ID INT, " +
                "FOREIGN KEY (Profile_ID) REFERENCES profile (ID)," +
                "FOREIGN KEY (Signal_ID) REFERENCES signal (ID)," +
                "FOREIGN KEY (Location_ID) REFERENCES location (ID))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists profile");
        sqLiteDatabase.execSQL("drop Table if exists location");
        sqLiteDatabase.execSQL("drop Table if exists signal");
        sqLiteDatabase.execSQL("drop Table if exists profile_data");

        sqLiteDatabase.execSQL("create Table profile" +
                "(ID INT primary key," +
                "Name TEXT)");

        sqLiteDatabase.execSQL("create Table location" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Latitude TEXT, " +
                "Altitude TEXT, " +
                "Longitude TEXT," +
                "Time TEXT," +
                "Accurency TEXT)");

        sqLiteDatabase.execSQL("create Table signal" +
                "(ID INTEGER PRIMARY KEY autoincrement, " +
                "Network_Provider TEXT, " +
                "Network_Type TEXT, " +
                "RSSI TEXT," +
                "RSRP TEXT," +
                "RSRQ TEXT," +
                "RSSNR TEXT)");

        sqLiteDatabase.execSQL("create Table profile_data" +
                "(ID INTEGER PRIMARY KEY autoincrement," +
                "Profile_ID INT, " +
                "Signal_ID INT, " +
                "Location_ID INT, " +
                "FOREIGN KEY (Profile_ID) REFERENCES profile (ID)," +
                "FOREIGN KEY (Signal_ID) REFERENCES signal (ID)," +
                "FOREIGN KEY (Location_ID) REFERENCES location (ID))");

        sqLiteDatabase.execSQL("INSERT INTO signal(ID,Network_Provider,Network_Type,RSSI,RSRP,RSRQ,RSSNR) VALUES (0,'None','None','0','0','0','0')");
    }

    public boolean insertProfile(int ID, String Name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID",ID);
        cv.put("Name",Name);
        long resoult = db.insert("profile",null, cv);
        if(resoult==-1) { return false; }
        else { return true; }
    }

    public boolean insertSignal(ContentValues signalData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long resoult = db.insert("signal",null, signalData);

        if(resoult==-1) {
            return false; }
        else {
            return true; }
    }

    public boolean insertLocation(ContentValues locationData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long resoult = db.insert("location",null, locationData);

        if(resoult==-1) {
            return false; }
        else {
            return true; }
    }

    public void insertProfileData(int profileID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int locationID = selectID("location");
        int signalID=selectID("signal");

        sqLiteDatabase.execSQL("INSERT INTO profile_data(Profile_ID,Signal_ID,Location_ID) VALUES ("+profileID+","+signalID+","+locationID+")");
    }

    public void insertProfileData(int profileID,int signalID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int locationID = selectID("location");

        sqLiteDatabase.execSQL("INSERT INTO profile_data(Profile_ID,Signal_ID,Location_ID) VALUES ("+profileID+","+signalID+","+locationID+")");
    }

    private int selectID(String table)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor =sqLiteDatabase.rawQuery("Select MAX(ID) From '"+table+"'",null);
        cursor.moveToFirst();
        int data = cursor.getInt(cursor.getColumnIndexOrThrow("MAX(ID)"));
        return data;
    }

}
