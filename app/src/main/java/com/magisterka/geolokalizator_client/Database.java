package com.magisterka.geolokalizator_client;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

    public Database(Context context) {
        super(context, "BazaDanych.db", null, 7);
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
                "DateTime TEXT," +
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
                "DateTime TEXT," +
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
        //insertProfile(1,"test");
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


    public boolean insert(String tableName,ContentValues values)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long resoult = db.insert(tableName,null, values);
        if(resoult==-1) {
            return false; }
        else {
            return true; }
    }


    private boolean insertProfileData(int profileID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Profile_ID",profileID);
        values.put("Signal_ID",selectTopID("signal"));
        values.put("Location_ID",selectTopID("location"));

        long resoult = sqLiteDatabase.insert("profile_data",null, values);
        if(resoult==-1) {
            return false; }
        else {
            return true; }
    }


    private int selectTopID(String table)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor =sqLiteDatabase.rawQuery("Select MAX(ID) From '"+table+"'",null);
        cursor.moveToFirst();
        int data = cursor.getInt(cursor.getColumnIndexOrThrow("MAX(ID)"));
        return data;
    }


    public boolean insertCollectedData(ContentValues locationData,ContentValues signalData,int profileID)
    {
        boolean successfullyInserted;

        if(signalData.isEmpty()) { return false; }
        if(locationData.isEmpty()) { return false; }

        successfullyInserted=insert("location",locationData);
        if(!successfullyInserted) {
            return false;
        }

        successfullyInserted=insert("signal",signalData);
        if(!successfullyInserted){
            deleteLastRecord("location");
            return false;
        }

        successfullyInserted=insertProfileData(1);
        if(!successfullyInserted){
            deleteLastRecord("signal");
            deleteLastRecord("location");
            return false;
        }

        return true;
    }


    public void deleteLastRecord(String table)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("DELETE FROM "+table+" WHERE ID=(SELECT MAX(ID) FROM "+table+")");

    }

    public int[] getNumberOfMeasurementsWeek()
    {
        int[] numberOfMeasurements = new int[7];

        //TimeCalculator timeCalculator = new TimeCalculator();
        //String[] sevenDaysDates = timeCalculator.getLastSevenDaysDates();
        Cursor cursor;

        for(int i = 0;i<7;i++)
        {
            cursor = countRowsLocationByDate(i);
            cursor.moveToFirst();
            numberOfMeasurements[i]=cursor.getInt(0);
        }


        return numberOfMeasurements;
    }

    private Cursor countRowsLocationByDate(int daysBack)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Cursor cursor = sqLiteDatabase.rawQuery("Select COUNT(ID) from location Where Date =?", new String[]{dayDates});
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(ID) FROM location WHERE strftime('%Y %m %d',DateTime) = strftime('%Y %m %d','now','-"+daysBack+" days')",null);

        return cursor;
    }


}
