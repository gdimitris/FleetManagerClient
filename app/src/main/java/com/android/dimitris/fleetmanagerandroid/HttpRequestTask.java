package com.android.dimitris.fleetmanagerandroid;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dimitris on 11/16/15.
 */
public class HttpRequestTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        try{
            URL url = new URL(params[0] + params[1]);
            Log.e("Task", "Url is :"+url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(params[1]);
            out.flush();
            out.close();

            InputStream is = connection.getInputStream();
            Log.e("Task Result", is.toString());


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }
}
