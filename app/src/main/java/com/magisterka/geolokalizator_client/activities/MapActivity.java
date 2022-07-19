package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.magisterka.geolokalizator_client.Database;
import com.magisterka.geolokalizator_client.MapDataEditor;
import com.magisterka.geolokalizator_client.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

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


    private MapView map;
    private MapDataEditor mapDataEditor;
    private Database database;

    private String year;
    private String month;
    private String day;
    private String hour;

    private Cursor cursor;

    ArrayList<OverlayItem> anotherOverlayItemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            hour = extras.getString("hour");
        }

        map = findViewById(R.id.mapview);


        mapDataEditor = new MapDataEditor();
        database = new Database(this);
        cursor = database.getHourMeasurementWithLocation(year,month,day,hour);

        createMap();
    }

    private ArrayList<Marker> getMarkers()
    {
        ArrayList<Marker> markerList= new ArrayList<Marker>();
        Marker tempMarker;



        GeoPoint[] points = mapDataEditor.getMapPoints(cursor);
        String[] texts= mapDataEditor.getMapMarkerTextArray(cursor);


        for(int i=0;i< texts.length;i++)
        {
            tempMarker = new Marker(map);
            tempMarker.setTitle(texts[i]);
            tempMarker.setPosition(points[i]);

            markerList.add(tempMarker);
        }

        return markerList;
    }

    public void createMap()
    {

        ArrayList<Marker> markerList = getMarkers();
        GeoPoint startingPoint = mapDataEditor.getStartingPoint(cursor);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);


        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        /*for(int i=0;i<markerList.size();i++)
        {
            anotherOverlayItemArray.add(new OverlayItem(markerList.get(i)));
            anotherOverlayItemArray.add(markerList);
        }*/

        map.getOverlays().addAll(markerList);

        //Marker marker = markerList.get(34);
        //map.getOverlays().add(markerList.get(34));

        map.getController().setCenter(markerList.get(0).getPosition());
    }
}