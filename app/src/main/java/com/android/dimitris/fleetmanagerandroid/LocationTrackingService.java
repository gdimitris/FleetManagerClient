package com.android.dimitris.fleetmanagerandroid;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by dimitris on 11/15/15.
 */
public class LocationTrackingService extends Service
{
    private static final String TAG = "LOCATION_TRACKER";
    private final int NOTIFICATION_ID = 26373;
    private LocationReceiver locationReceiver;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG,"onStart was called!");
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate was called");
        locationReceiver = new LocationReceiver(this);
    }


    @Override
    public void onDestroy()
    {
        Log.e(TAG,"onDestroy was Called!");
        super.onDestroy();
        locationReceiver.stopListeningToLocationChanges();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void sendLocationValues(Location theLocation)
    {

        //A web service will be called and the user's current location will be stored in server
        Log.e(TAG, "sendLocationValues was called");
        String title = "Device ID: " + PublicHelpers.getDeviceUniqueID(getContentResolver());
        String body = "Current Coordinates: " + theLocation.getLatitude() + "," + theLocation.getLongitude();

        Log.e(TAG, "Sending notification...");
        PublicHelpers.sendNotification(this, title, body, NOTIFICATION_ID);
    }



}