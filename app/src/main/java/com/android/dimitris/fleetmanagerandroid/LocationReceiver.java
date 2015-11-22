package com.android.dimitris.fleetmanagerandroid;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by dimitris on 11/15/15.
 */
public class LocationReceiver implements LocationListener {

    private LocationManager locationManager;
    private Location latestLocation;
    private LocationTrackingService service;

    private static int EIGHT_METERS = 8;
    private static int FIVE_SECONDS = 5000;


    public LocationReceiver(LocationTrackingService locationTrackingService){
        service = locationTrackingService;
        locationManager = (LocationManager) service.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, FIVE_SECONDS, EIGHT_METERS, this);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);
    }

//    public Location getLocation(){
//        if(latestLocation != null)
//            return latestLocation;
//
//        return getLastPosition();
//    }
//
//    private Location getLastPosition(){
//        Location bestResult = null;
//        float bestAccuracy = Float.MAX_VALUE;
//        long bestTime = Long.MIN_VALUE;
//
//        List<String> matchingProviders = locationManager.getAllProviders();
//        for (String provider: matchingProviders) {
//            Location location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
//                float accuracy = location.getAccuracy();
//                long time = location.getTime();
//
//                if ((time > bestTime && accuracy < bestAccuracy)) {
//                    bestResult = location;
//                    bestAccuracy = accuracy;
//                    bestTime = time;
//                }
//                else if (time < bestTime && bestAccuracy == Float.MAX_VALUE && time > bestTime){
//                    bestResult = location;
//                    bestTime = time;
//                }
//            }
//        }
//
//        return bestResult;
//    }

    public void stopListeningToLocationChanges(){
        locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        latestLocation = location;
        service.sendLocationValues(latestLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
