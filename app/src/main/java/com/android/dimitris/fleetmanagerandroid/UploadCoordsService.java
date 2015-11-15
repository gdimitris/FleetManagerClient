package com.android.dimitris.fleetmanagerandroid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by dimitris on 11/15/15.
 */
public class UploadCoordsService extends IntentService {

    private static final String DEBUG_TAG = "UploadCoordsService";
    private static final int NOTIFICATION_ID = 66666;

    public UploadCoordsService(){
        super("UploadCoordsService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public UploadCoordsService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(DEBUG_TAG, "Starting Service");
        sendNotification();
    }

    private void sendNotification(){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Service Status")
                .setContentText("Service started successfully")
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(soundUri)
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
