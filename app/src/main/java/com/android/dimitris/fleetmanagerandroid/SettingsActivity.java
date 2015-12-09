package com.android.dimitris.fleetmanagerandroid;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Created by dimitris on 12/9/15.
 */
public class SettingsActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return fragmentName.equals("com.android.dimitris.fleetmanagerandroid.SettingsActivity$SettingsFragment");
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_activity_fragment_headers,target);
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference pref = findPreference("update");
            pref.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if(key.equals("update")){

                return true;
            }
            return false;
        }
    }
}
