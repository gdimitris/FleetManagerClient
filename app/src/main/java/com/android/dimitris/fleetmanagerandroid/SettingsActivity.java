package com.android.dimitris.fleetmanagerandroid;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.net.Uri;
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
                requestDownloadForNewApk();
                return true;
            }
            return false;
        }

        private void requestDownloadForNewApk() {
            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://4fef2401.ngrok.com/content/current_version.apk"));
            long enqueue = downloadManager.enqueue(request);
            ApkUpdateReceiver receiver = new ApkUpdateReceiver(downloadManager,enqueue);
            getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }
}
