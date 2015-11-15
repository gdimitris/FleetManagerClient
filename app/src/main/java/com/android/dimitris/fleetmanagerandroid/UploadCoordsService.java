package com.android.dimitris.fleetmanagerandroid;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dimitris on 11/15/15.
 */
public class UploadCoordsService extends IntentService {

    private static final String DEBUG_TAG = "UploadCoordsService";


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
        //PublicHelpers.sendNotification(this,);
    }


}
