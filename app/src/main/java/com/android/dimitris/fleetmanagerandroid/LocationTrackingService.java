package com.android.dimitris.fleetmanagerandroid;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by dimitris on 11/15/15.
 */
public class LocationTrackingService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = "LOCATION_TRACKER";
    //private static final String SERVER_URL = "http://4fef2401.ngrok.com/";
    //private static final String SERVER_URL = "https://dimitrisg.pythonanywhere.com/";
    private final int NOTIFICATION_ID = 26373;
    private LocationReceiver locationReceiver;
    public static boolean isRunning = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        isRunning = true;
        super.onStartCommand(intent, flags, startId);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        Log.e(TAG,"onStart was called!");
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        isRunning = true;
        Log.e(TAG, "onCreate was called");
        locationReceiver = new LocationReceiver(this);
    }


    @Override
    public void onDestroy()
    {
        Log.e(TAG,"onDestroy was Called!");
        super.onDestroy();
        locationReceiver.stopListeningToLocationChanges();
        isRunning = false;
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void sendLocationValues(Location theLocation)
    {
        if(theLocation == null)
            return;

        //A web service will be called and the user's current location will be stored in server
        Log.e(TAG, "sendLocationValues was called");
        String deviceID = PublicHelpers.getDeviceUniqueID(getContentResolver());
        String title = "Device ID: " + deviceID;
        String body = "Current Coordinates: " + theLocation.getLatitude() + "," + theLocation.getLongitude();

        Log.e(TAG, "Sending notification...");
        PublicHelpers.sendNotification(this, title, body, NOTIFICATION_ID);

        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        HttpRequestTask uploadTask = new HttpRequestTask();
        String url = getResources().getString(R.string.server_url);
        uploadTask.execute(url+ deviceID,"?lat="+theLocation.getLatitude()+"&lon="+theLocation.getLongitude()+"&time="+timestamp);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("distance_updates")){
            locationReceiver.restartReceiver();
        }
    }
}