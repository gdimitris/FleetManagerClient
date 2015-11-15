package com.android.dimitris.fleetmanagerandroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * Created by dimitris on 11/15/15.
 */
public class LocationTrackingService extends Service
{
    private static final String TAG = "TestMyLocation";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0;
    private final int NOTIFICATION_ID = 26373;
    long itsBatchId = 0;


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;
        public LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            final Location loc = location;
            Thread aThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendLocationValues(loc);
                }
            });
            aThread.start();
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    private void sendLocationValues(Location theLocation)
    {

        //A web service will be called and the user's current location will be stored in server
        String title = "Device ID: " + PublicHelpers.getDeviceUniqueID(getContentResolver());
        String body = "Current Coordinates: " + theLocation.getLatitude() + "," + theLocation.getLongitude();

        PublicHelpers.sendNotification(this,title,body,NOTIFICATION_ID);

    }

    LocationListener[] mLocationListeners = new LocationListener[]
            {
                    new LocationListener(LocationManager.GPS_PROVIDER),
                    new LocationListener(LocationManager.NETWORK_PROVIDER)
            };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        initializeLocationManager();
        itsLocationHandler.sendEmptyMessage(1);
    }

    private Handler itsLocationHandler = new Handler()
    {
        @Override
        public void handleMessage(Message theMessage)
        {
            if(theMessage.what == 1)
            {
                try
                {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
                }
                catch (java.lang.SecurityException ex)
                {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                }
                catch (IllegalArgumentException ex)
                {
                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                }
            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopSelf();
    }

    private void initializeLocationManager()
    {
        if (mLocationManager == null)
        {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}