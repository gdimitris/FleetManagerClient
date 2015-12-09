package com.android.dimitris.fleetmanagerandroid;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

/**
 * Created by dimitris on 12/9/15.
 */
public class ApkUpdateReceiver extends BroadcastReceiver {

    private DownloadManager downloadManager;
    private long enqueue;

    public ApkUpdateReceiver(DownloadManager downloadManager, long enqueue){
        this.downloadManager = downloadManager;
        this.enqueue = enqueue;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(enqueue);
            Cursor cursor = downloadManager.query(query);

            if(cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if(DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)){
                    installNewApk(context, cursor);
                }
            }
        }
    }

    private void installNewApk(Context context,Cursor cursor) {
        String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
        Uri fileUri = Uri.fromFile(new File(uri));

        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(fileUri,"application/vnd.android.package-archive");
        context.startActivity(promptInstall);
    }
}
