package com.android.dimitris.fleetmanagerandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dimitris on 11/15/15.
 */
public class LocationReceiver implements LocationListener {

    private LocationManager locationManager;
    private Location currentBestLocation = null;
    private LocationTrackingService service;

    private static int FIVE_MINUTES = 1000 * 60 * 5;
    private static final int TWO_MINUTES = 1000 * 60 * 2;


    public LocationReceiver(LocationTrackingService locationTrackingService){
        service = locationTrackingService;
        locationManager = (LocationManager) service.getSystemService(Context.LOCATION_SERVICE);
        registerForNetworkLocationUpdates();
        registerForGPSLocationUpdates();
    }

    private void registerForGPSLocationUpdates(){
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(service);
            String preferredValueString = preferences.getString("distance_updates","15");
            int preferredValue = Integer.parseInt(preferredValueString);
            Log.e("GPS Location Manager", "Starting with accuracy "+preferredValue+" meters");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, preferredValue, this);
        } catch (Exception e){
            Log.e("Location Manager", e.getMessage());
            Toast.makeText(service,"Please enable GPS Provider. Failed to get signal", Toast.LENGTH_LONG).show();
        }
    }

    private void registerForNetworkLocationUpdates() {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(service);
            String preferredValueString = preferences.getString("distance_updates","15");
            int preferredValue = Integer.parseInt(preferredValueString);
            Log.e("GPS Location Manager", "Starting with accuracy "+preferredValue+" meters");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, preferredValue, this);
        } catch (Exception e){
            Log.e("Location Manager",e.getMessage());
            Toast.makeText(service,"Please enable Network Access. Failed to get signal", Toast.LENGTH_LONG).show();
        }
    }

    private Location getLastPosition(){
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider: matchingProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > bestTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
                else if (time < bestTime && bestAccuracy == Float.MAX_VALUE && time > bestTime){
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        return bestResult;
    }

    public void stopListeningToLocationChanges(){
        locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        examineNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)){
            registerForGPSLocationUpdates();
        } else if (provider.equals(LocationManager.NETWORK_PROVIDER)){
            registerForNetworkLocationUpdates();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation){
        if(currentBestLocation == null){
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignifacantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if(isSignificantlyNewer){
            return true;
        } else if (isSignifacantlyOlder){
            return false;
        }

        int accuracyDelta = (int)(location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        if (isMoreAccurate){
            return true;
        } else if(isNewer && !isLessAccurate){
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider){
            return true;
        }

        return false;
    }

    private boolean isSameProvider(String provider1, String provider2){
        if (provider1 == null){
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void examineNewLocation(Location location) {
        if (isBetterLocation(location,currentBestLocation)){
            currentBestLocation = location;
            service.sendLocationValues(currentBestLocation);
        }
    }

    public void restartReceiver(){
        Log.e("LocationReceiver", "Restarting Location Receiver...");
        stopListeningToLocationChanges();
        registerForGPSLocationUpdates();
        registerForNetworkLocationUpdates();
    }

}
