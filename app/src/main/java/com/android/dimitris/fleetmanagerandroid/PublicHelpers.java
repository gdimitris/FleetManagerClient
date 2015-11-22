package com.android.dimitris.fleetmanagerandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;

/**
 * Created by dimitris on 11/15/15.
 */
public class PublicHelpers {

    public static String getDeviceUniqueID(ContentResolver resolver){
        return Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
    }

    public static String getCurrentCoords(Context context){
        return "coords";
    }

    public static void sendNotification(Context context, String title, String body, int notificationID){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(Notification.PRIORITY_LOW);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, mBuilder.build());
    }

    public static void saveInPreferences(Context context, String key, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String loadFromPreferences(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key,"");
    }
}
