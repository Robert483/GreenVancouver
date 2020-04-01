package com.parasinos.greenvancouver.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parasinos.greenvancouver.fragments.MapFragment;
import com.parasinos.greenvancouver.handlers.HttpHandler;
import com.parasinos.greenvancouver.models.APIRetrieval;
import com.parasinos.greenvancouver.models.Project;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Async task class to get json by making HTTP call
 */
public class MapMarkerGenerator extends AsyncTask<Void, Void, List<Project>> {
    private WeakReference<MapFragment> fragment;
    private String url;

    public MapMarkerGenerator(MapFragment fragment, String url) {
        this.fragment = new WeakReference<>(fragment);
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Project> doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        if (jsonStr != null) {
            Gson gson = new Gson();
            APIRetrieval responses = gson.fromJson(jsonStr, APIRetrieval.class);
            return responses.getRecords();
        } else {
            final Activity activity = this.fragment.get().getActivity();
            if (activity == null) {
                return null;
            }

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
    protected void onPostExecute(List<Project> result) {
        super.onPostExecute(result);
        this.fragment.get().updateMarkers(result);

    }
}




