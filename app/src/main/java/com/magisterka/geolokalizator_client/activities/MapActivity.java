package com.magisterka.geolokalizator_client.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.magisterka.geolokalizator_client.database.DatabaseMap;
import com.magisterka.geolokalizator_client.sharedpreferences.AccountInfoHelper;
import com.magisterka.geolokalizator_client.callserverapi.ApiCallAccessData;
import com.magisterka.geolokalizator_client.activities.datahandling.MapDataEditor;
import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.models.accessdatamodels.HourDataMapModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity {

    private ApiCallAccessData apiCallAccessData;
    private AccountInfoHelper accountInfoHelper;
    private static String BASE_URL = "http://192.168.1.74/geolokalizator/data/";

    private List<HourDataMapModel> mapModels;
    private List<Marker> markerList;

    private MapView map;
    private MapDataEditor mapDataEditor;
    private DatabaseMap databaseMap;

    private String year;
    private String month;
    private String day;
    private String hour;

    private static boolean dataFromOnlineDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        accountInfoHelper = new AccountInfoHelper(this);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        getExtras();

        map = findViewById(R.id.mapview);

        mapDataEditor = new MapDataEditor();

        databaseMap = new DatabaseMap(this);

        if(dataFromOnlineDatabase) {
            initDataAccessApi();
            createMapFromOnlineDataBase();
        }
        else {
            createMapFromLocalDataBase();
        }
    }

    private void createMapFromLocalDataBase()
    {
        mapModels = databaseMap.getHourMeasurementWithLocation(year,month,day,hour);

        markerList = mapDataEditor.getMarkers(map,mapModels);

        createMap();
    }

    private void createMapFromOnlineDataBase()
    {
        String token = accountInfoHelper.getToken();
        Call<List<HourDataMapModel>> call = apiCallAccessData.getMapDataByHour(token,Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour));
        callDropdownHandler(call);
    }

    private void callDropdownHandler(Call<List<HourDataMapModel>> call)
    {
        call.enqueue(new Callback<List<HourDataMapModel>>() {
            @Override
            public void onResponse(Call<List<HourDataMapModel>> call, Response<List<HourDataMapModel>> response) {

                if(response.code() != 200) { return; }//TODO Add info about error

                List<HourDataMapModel> dataFromCall = response.body();

                markerList = mapDataEditor.getMarkers(map,dataFromCall);

                createMap();
            }

            @Override
            public void onFailure(Call<List<HourDataMapModel>> call, Throwable t) { }

        });
    }

    private void initDataAccessApi()
    {
        OkHttpClient client = RetrofitLogging();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCallAccessData = retrofit.create(ApiCallAccessData.class);
    }

    private OkHttpClient RetrofitLogging()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return client;
    }

    public void createMap()
    {

        //GeoPoint startingPoint = mapDataEditor.getStartingPoint(cursor);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();

        map.getOverlays().add(compassOverlay);

        map.getOverlays().addAll(markerList);

        map.getController().setCenter(markerList.get(0).getPosition());
    }

    private void getExtras()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            hour = extras.getString("hour");
            dataFromOnlineDatabase = extras.getBoolean("dataFromOnlineDatabase");
        }
    }
}