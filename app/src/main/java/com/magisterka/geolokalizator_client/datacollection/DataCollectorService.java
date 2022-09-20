package com.magisterka.geolokalizator_client.datacollection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.magisterka.geolokalizator_client.R;
import com.magisterka.geolokalizator_client.activities.ServiceActivity;

public class DataCollectorService extends Service {

    private static DataCollectorService instance = null;
    private DataCollectorThread dataCollectorThread ;
    private static String NOTIFICATION_TITLE = "Data Collector";
    private static String NOTIFICATION_TEXT = "Data is being collected!";

    public void onCreate()
    {
        super.onCreate();
        instance = this;
        dataCollectorThread = new DataCollectorThread(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        createNotificationChannel();

        dataCollectorThread.start();

        Notification notification = createNotification();

        startForeground(1, notification);

        return START_STICKY;

    }

    public static boolean isInstanceCreated() {
        return instance != null;
    }

    private Notification createNotification()
    {
        Intent intentNew = new Intent(this, ServiceActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intentNew, PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, "ChannelID")
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText((NOTIFICATION_TEXT))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingintent).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
        return notification;
    }


    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("ChannelID", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }


    @Override
    public void onDestroy() {

        dataCollectorThread.interrupt();

        instance = null;

        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}
