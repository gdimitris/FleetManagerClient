package com.android.dimitris.fleetmanagerandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startServiceButton;
    private Button stopServiceButton;
    private TextView fleetManagerStatus;
    private TextView internetStatus;
    private TextView locationStatus;
    private TextView deviceID;
    private TextView fullNameView;

    private static final long FIVE_MINUTES_IN_MILLIS = 300000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullNameView = (TextView) findViewById(R.id.nameTextView);
        fleetManagerStatus = (TextView) findViewById(R.id.fleetManagerStatus);
        internetStatus = (TextView) findViewById(R.id.internetAccessStatus);
        locationStatus = (TextView) findViewById(R.id.locationServiceStatus);
        deviceID = (TextView) findViewById(R.id.deviceIDTextView);
        deviceID.setText(PublicHelpers.getDeviceUniqueID(getContentResolver()));

        startServiceButton = (Button) findViewById(R.id.startServiceButton);
        startServiceButton.setOnClickListener(this);

        stopServiceButton = (Button) findViewById(R.id.stopServiceButton);
        stopServiceButton.setOnClickListener(this);
        checkAllServices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, UpdateNameActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAllServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkAllServices();
    }

//    public void scheduleAlarm(){
//        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long currentTimeMillis = System.currentTimeMillis();
//
//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, currentTimeMillis, FIVE_MINUTES_IN_MILLIS, pendingIntent);
//    }
//
//    public void cancelAlarm(){
//        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//
//        if (pendingIntent != null) {
//            alarm.cancel(pendingIntent);
//            pendingIntent.cancel();
//        }
//    }

    private boolean isNetworkActive(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isLocationServiceActive(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void checkLocationTrackingStatus(){
        if (LocationTrackingService.isRunning)
            setLabelStatusEnabled(fleetManagerStatus);
        else
            setLabelStatusDisabled(fleetManagerStatus);
    }

    private void checkNetworkStatus(){
        if (isNetworkActive())
            setLabelStatusEnabled(internetStatus);
        else
            setLabelStatusDisabled(internetStatus);
    }

    private void checkLocationStatus(){
        if (isLocationServiceActive())
            setLabelStatusEnabled(locationStatus);
        else
            setLabelStatusDisabled(locationStatus);
    }

    private void checkAllServices(){
        checkName();
        checkNetworkStatus();
        checkLocationStatus();
        checkLocationTrackingStatus();
    }

    private void setLabelStatusEnabled(TextView textView){
        textView.setText("Enabled");
        textView.setTextColor(Color.GREEN);
    }

    private void setLabelStatusDisabled(TextView textView){
        textView.setText("Disabled");
        textView.setTextColor(Color.RED);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();

        if(viewID == startServiceButton.getId()){
            if(isNameSetInPreferences())
                startLocationTrackingService();
            else {
                Intent intent = new Intent(this, UpdateNameActivity.class);
                startActivity(intent);
            }
        } else if (viewID == stopServiceButton.getId()){
            stopLocationTrackingService();
        }
    }

    private void stopLocationTrackingService() {
        Intent stopIntent = new Intent(this, LocationTrackingService.class);
        stopService(stopIntent);
        setLabelStatusDisabled(fleetManagerStatus);
    }

    private void startLocationTrackingService() {
        Intent startIntent = new Intent(this, LocationTrackingService.class);
        startService(startIntent);
        setLabelStatusEnabled(fleetManagerStatus);
    }

    private boolean isNameSetInPreferences(){
        String name = PublicHelpers.loadFromPreferences(this,"Name");
        String surname = PublicHelpers.loadFromPreferences(this, "Surname");

        return (!name.equals("") && !surname.equals(""));
    }

    private void checkName(){
        String name = PublicHelpers.loadFromPreferences(this, "Name");
        String surname = PublicHelpers.loadFromPreferences(this, "Surname");

        fullNameView.setText(name + " " + surname);
    }
}
