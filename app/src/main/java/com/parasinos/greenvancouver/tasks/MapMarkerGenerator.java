package com.parasinos.greenvancouver.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.parasinos.greenvancouver.fragments.MapFragment;
import com.parasinos.greenvancouver.handlers.HttpHandler;
import com.parasinos.greenvancouver.models.APIRetrieval;

import java.lang.ref.WeakReference;

/**
 * Async task class to get json by making HTTP call
 */
public class MapMarkerGenerator extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> activity;
    private String url;

    public MapMarkerGenerator(Activity activity, String url) {
        this.activity = new WeakReference<>(activity);
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        if (jsonStr != null) {
            Gson gson = new Gson();
            APIRetrieval responses = gson.fromJson(jsonStr, APIRetrieval.class);
            MapFragment.projectList = responses.getRecords();
        } else {
            final Activity activity = this.activity.get();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity.getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }
}




