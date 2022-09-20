package com.magisterka.geolokalizator_client.activities.datahandling;

import android.database.Cursor;

import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataMapModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapDataEditor {

    private static int DATETIME_CURSOR_INDEX = 1;
    private static int LATITUDE_CURSOR_INDEX = 2;
    private static int LONGITUDE_CURSOR_INDEX = 3;
    private static int ALTITUDE_CURSOR_INDEX = 4;
    private static int ACCURACY_CURSOR_INDEX = 5;
    private static int NETWORK_PROVIDER_CURSOR_INDEX = 6;
    private static int NETWORK_TYPE_CURSOR_INDEX = 7;
    private static int RSRP_CURSOR_INDEX = 8;
    private static int RSRQ_CURSOR_INDEX = 9;
    private static int RSSI_CURSOR_INDEX = 10;
    private static int RSSNR_CURSOR_INDEX = 11;


    public List<HourDataMapModel> cursorToMapModel(Cursor cursor)
    {
        List<HourDataMapModel> dataGraphModelsList = new ArrayList<HourDataMapModel>();
        boolean lastRowFinished = false;

        cursor.moveToFirst();

        while(!lastRowFinished)
        {
            cursor.moveToNext();

            dataGraphModelsList.add(getMapModel(cursor));

            lastRowFinished = cursor.isLast();
        }

        return dataGraphModelsList;
    }

    public HourDataMapModel getMapModel(Cursor cursor)
    {
        HourDataMapModel dataMapModel = new HourDataMapModel();

        dataMapModel.setDateTime(cursor.getString(DATETIME_CURSOR_INDEX));
        dataMapModel.setLatitude(cursor.getString(LATITUDE_CURSOR_INDEX));
        dataMapModel.setLongitude(cursor.getString(LONGITUDE_CURSOR_INDEX));
        dataMapModel.setAltitude(cursor.getString(ALTITUDE_CURSOR_INDEX));
        dataMapModel.setAccuracy(cursor.getString(ACCURACY_CURSOR_INDEX));
        dataMapModel.setNetwork_Provider(cursor.getString(NETWORK_PROVIDER_CURSOR_INDEX));
        dataMapModel.setNetwork_Type(cursor.getString(NETWORK_TYPE_CURSOR_INDEX));
        dataMapModel.setRsrp(cursor.getString(RSRP_CURSOR_INDEX));
        dataMapModel.setRsrq(cursor.getString(RSRQ_CURSOR_INDEX));
        dataMapModel.setRssi(cursor.getString(RSSI_CURSOR_INDEX));
        dataMapModel.setRssnr(cursor.getString(RSSNR_CURSOR_INDEX));

        return dataMapModel;
    }

    public GeoPoint[] getMapPoints(List<HourDataMapModel> data)
    {
        double Latitude;
        double Longitude;

        int numberOfPoints = data.size();

        GeoPoint[] points = new GeoPoint[numberOfPoints];

        for(int i=0;i < numberOfPoints;i++)
        {
            Latitude = Double.parseDouble(data.get(i).getLatitude());
            Longitude = Double.parseDouble(data.get(i).getLongitude());

            points[i] = new GeoPoint(Latitude, Longitude);
        }

        return points;
    }

    public List<Marker> getMarkers(MapView map, List<HourDataMapModel> data)
    {
        List<Marker> markerList= new ArrayList<Marker>();

        GeoPoint[] points = getMapPoints(data);
        String[] texts= getMapMarkerTextList(data);

        for(int i=0;i< texts.length;i++)
        {

            markerList.add(getMarker(map,points[i],texts[i]));
        }

        return markerList;
    }

    private Marker getMarker(MapView map, GeoPoint point , String text)
    {
        Marker marker;
        marker = new Marker(map);
        marker.setTitle(text);
        marker.setPosition(point);

        return marker;
    }

    public String[] getMapMarkerTextList(List<HourDataMapModel> mapModels)
    {
        int numberOfModels = mapModels.size();

        String[] finalText= new String[numberOfModels];

        for(int i=0; i < numberOfModels; i++)
        {
            finalText[i]=getMapMarkerText(mapModels.get(i));
        }

        return finalText;
    }

    private String getMapMarkerText(HourDataMapModel mapModel)
    {
        String text = mapModel.getNetwork_Provider() + " " + mapModel.getNetwork_Type() + " " + mapModel.getDateTime() + "\n " +
                "RSSI = "+ mapModel.getRssi()+"dB RSRP = " + mapModel.getRsrp() +"dB\n" +
                "RSRQ = " + mapModel.getRsrq()+ "dB RSSNR = " + mapModel.getRssnr() + "dB\n " +
                "Accuracy "+mapModel.getAccuracy()+"m Altitude " + mapModel.getAltitude() + "m";

        return text;
    }

}
