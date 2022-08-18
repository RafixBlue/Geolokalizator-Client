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

//TODO add check if measurement for this minute exist
//TODO change Accurency to Accuracy!!!!!!!!!!!!!!!!!!!!!
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


    public Cursor countRowsLocationByDate(int daysBack,int profileID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(ID) FROM location WHERE strftime('%Y %m %d',DateTime) = strftime('%Y %m %d','now','-"+daysBack+" days')",null);

        return cursor;
    }


    ////////  Graph Activity  ///////
    public Cursor getAvailableYear(int profileID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%Y',l.DateTime) FROM location l, profile_data pd WHERE Profile_ID = '" + profileID +"'GROUP BY strftime('%Y',l.DateTime)",null);

        return cursor;

    }

    public Cursor getAvailableMonth(int profileID, String year)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%m',l.DateTime) FROM location l, profile_data pd WHERE pd.Profile_ID = 1 AND strftime('%Y',l.DateTime) = '"+year+"' GROUP BY strftime('%m',l.DateTime)",null);

        return cursor;

    }

    public Cursor getAvailableDay(int profileID, String year,String month)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%d',l.DateTime) FROM location l, profile_data pd WHERE pd.Profile_ID = 1 AND strftime('%Y',l.DateTime) = '"+year+"' AND strftime('%m',l.DateTime) = '"+month+"' GROUP BY strftime('%d',l.DateTime)",null);

        return cursor;

    }

    public Cursor getAvailableHour(int profileID, String year,String month,String day)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%H',l.DateTime) FROM location l, profile_data pd WHERE pd.Profile_ID = '"+profileID+"' AND strftime('%Y',l.DateTime) = '"+year+"' AND strftime('%m',l.DateTime) = '"+month+"' AND strftime('%d',l.DateTime) = '"+day+"' GROUP BY strftime('%H',l.DateTime)",null);

        return cursor;

    }

    public Cursor getHourMeasurement(String year,String month,String day, String hour)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("Select pd.ID,strftime('%M',l.DateTime), l.Accurency, s.Network_Provider,s.Network_Type,s.RSRP,s.RSRQ,s.RSSI,s.RSSNR " +
                "From location l,profile_data pd Inner Join signal s On pd.Location_ID=l.ID AND s.ID=pd.Signal_ID Where l.ID IN (" +
                "Select ID From location Where strftime('%Y %m %d %H',DateTime) = '" +year+ " " +month+ " " +day+" "+hour+"') " +
                "Order BY l.DateTime",null);//"Order BY pd.ID",null);

        return cursor;
    }

    public String getMaxMinOfHour(String year,String month,String day, String hour)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("Select MAX(strftime('%M',l.DateTime)) From location l,profile_data pd On pd.Location_ID=l.ID Where l.ID IN (Select ID From location Where strftime('%Y %m %d %H',DateTime) = '" +year+ " " +month+ " " +day+" "+hour+"')",null);

        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public Cursor getHourMeasurementWithLocation(String year,String month,String day, String hour)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("Select pd.ID,strftime('%H %M',l.DateTime),l.Latitude, l.Longitude, l.Altitude, l.Accurency, s.Network_Provider,s.Network_Type,s.RSRP,s.RSRQ,s.RSSI,s.RSSNR " +
                "From location l,profile_data pd Inner Join signal s On pd.Location_ID=l.ID AND s.ID=pd.Signal_ID Where l.ID IN (" +
                "Select ID From location Where strftime('%Y %m %d %H',DateTime) = '" +year+ " " +month+ " " +day+" "+hour+"') " +
                "Order BY l.DateTime",null);

        return cursor;
    }
    //Select pd.ID,strftime('%H:%M',l.DateTime), l.Accurency, s.Network_Provider,s.Network_Type,s.RSRP,s.RSRQ,s.RSSI,s.RSSNR
    //From location l,profile_data pd
    //Inner Join signal s
    //On pd.Location_ID=l.ID AND s.ID=pd.Signal_ID
    //Where l.ID IN (Select ID From location Where strftime('%Y %m %d %H',DateTime) = '2022 06 13 09')
    //Order BY pd.ID




}
