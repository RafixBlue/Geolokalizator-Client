package com.magisterka.geolokalizator_client;

import android.database.Cursor;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapDataEditor {

    private static int TIME_CURSOR_INDEX = 1;
    private static int LATITUDE_CURSOR_INDEX = 2;
    private static int LONGITUDE_CURSOR_INDEX = 3;
    private static int ALTITUDE_CURSOR_INDEX = 4;
    private static int ACCURENCY_CURSOR_INDEX = 5;
    private static int PROVIDER_CURSOR_INDEX = 6;
    private static int NETWORK_TYPE_CURSOR_INDEX = 7;
    private static int RSRP_CURSOR_INDEX = 8;
    private static int RSRQ_CURSOR_INDEX = 9;
    private static int RSSI_CURSOR_INDEX = 10;
    private static int RSSNR_CURSOR_INDEX = 11;


    private String[] readCursor(Cursor cursor,int index)
    {
        cursor.moveToFirst();
        String[] cursorData = new String[cursor.getCount()];

        for(int i =0; i < cursor.getCount();i++)
        {
            cursorData[i]=cursor.getString(index);
            cursor.moveToNext();

        }
        int a = 10;
        return cursorData;

    }

    public GeoPoint getStartingPoint(Cursor cursor)
    {
        cursor.moveToFirst();
        Double dLatitude = Double.parseDouble(cursor.getString(LATITUDE_CURSOR_INDEX));
        Double dLongitude = cursor.getDouble(LONGITUDE_CURSOR_INDEX);

        GeoPoint startingPoint = new GeoPoint(dLatitude, dLongitude);

        return startingPoint;
    }

    public GeoPoint[] getMapPoints(Cursor cursor)
    {
        String[] latitude = readCursor(cursor,LATITUDE_CURSOR_INDEX);
        String[] longitude = readCursor(cursor,LONGITUDE_CURSOR_INDEX);

        Double dLatitude;
        Double dLongitude;
        GeoPoint[] points = new GeoPoint[latitude.length];

        for(int i=0;i < latitude.length;i++)
        {
            dLatitude = Double.parseDouble(latitude[i]);
            dLongitude = Double.parseDouble(longitude[i]);

            points[i] = new GeoPoint(dLatitude, dLongitude);
        }

        return points;
    }

    public String[] getMapMarkerTextArray(Cursor cursor)
    {
        String[] time = readCursor(cursor,TIME_CURSOR_INDEX);
        String[] altitude = readCursor(cursor,ALTITUDE_CURSOR_INDEX);
        String[] accurency = readCursor(cursor,ACCURENCY_CURSOR_INDEX);
        String[] networkProvider = readCursor(cursor,PROVIDER_CURSOR_INDEX);
        String[] networkType = readCursor(cursor,NETWORK_TYPE_CURSOR_INDEX);
        String[] rsrp = readCursor(cursor,RSRP_CURSOR_INDEX);
        String[] rsrq = readCursor(cursor,RSRQ_CURSOR_INDEX);
        String[] rssi = readCursor(cursor,RSSI_CURSOR_INDEX);
        String[] rssnr = readCursor(cursor,RSSNR_CURSOR_INDEX);

        String[] finalText= new String[time.length];

        for(int i=0;i<cursor.getCount();i++)
        {
            finalText[i]=getMapMarkerText(time[i],altitude[i],accurency[i],networkProvider[i],networkType[i],rsrp[i],rsrq[i],rssi[i],rssnr[i]);
        }

        return finalText;
    }

    public String getMapMarkerText(String time, String altitude, String accurency, String networkProvider, String networkType,   String rsrp, String rsrq, String rssi, String rssnr)
    {
        altitude=altitude.substring(0,altitude.indexOf(".")+2);
        accurency=accurency.substring(0,accurency.indexOf(".")+2);

        String topText = networkProvider + " " + networkType + " " + time + "\n";
        String middleText = "RSSI = "+rssi+"dB RSRP = "+rsrp+"dB\nRSRQ = "+rsrq+"dB RSSNR = "+rssnr+"dB\n";
        String bottomText = "Accurency "+accurency+"m Altitude "+altitude+"m";

        String text = topText+middleText+bottomText;

        return text;
    }

    public String[] getDropdownFiller(Cursor cursor) { //TODO better name here too

        String[] filler = new String[cursor.getCount()];

        cursor.moveToFirst();

        for(int i =0; i < cursor.getCount();i++)
        {
            filler[i] = cursor.getString(0);
            cursor.moveToNext();
        }

        return filler;
    }
}
