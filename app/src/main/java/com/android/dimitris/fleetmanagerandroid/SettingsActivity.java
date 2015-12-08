package com.android.dimitris.fleetmanagerandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by dimitris on 12/9/15.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
