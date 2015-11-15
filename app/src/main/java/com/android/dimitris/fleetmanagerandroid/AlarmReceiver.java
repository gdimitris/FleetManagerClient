package com.android.dimitris.fleetmanagerandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dimitris on 11/15/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 263789;
    public static final String ACTION = "com.android.dimitris.fleetmanager.alarm";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UploadCoordsService.class);
        context.startService(i);
    }
}
