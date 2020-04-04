package com.parasinos.greenvancouver.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parasinos.greenvancouver.ProjectInfoActivity;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.APIRetrieval;
import com.parasinos.greenvancouver.models.Field;
import com.parasinos.greenvancouver.models.Project;
import com.parasinos.greenvancouver.tasks.HttpHandler;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;

public class VolunteerFragment extends Fragment {
    private String mapID;
    private String subject;
    private String body;
    private String contact = "\nContact information:";

    public static VolunteerFragment newInstance(String mapId) {
        VolunteerFragment fragment = new VolunteerFragment();
        Bundle args = new Bundle();
        args.putString("mapId", mapId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapID = getArguments().getString("mapId");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        String service_url = getString(R.string.details_api_url, mapID);
        new Retrieval(this, service_url).execute();
        View v = Objects.requireNonNull(getView());
        Button send = v.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(emailIntent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volunteer, container, false);
    }

    private void updateProjectInfo(List<Project> result) {
        ProjectInfoActivity infoActivity = (ProjectInfoActivity) getActivity();

        if (infoActivity != null) {
            View v = getView();
            Project project = result.get(0);
            Field projectDetails = project.getField();

            if (v != null) {
                TextView orgName = v.findViewById(R.id.txtv_orgname);
                TextView orgDetails = v.findViewById(R.id.txtv_orgdetails);
                EditText volEmail = v.findViewById(R.id.txtv_volunteeremail);
                EditText volName = v.findViewById(R.id.txtv_volunteername);
                EditText volPhone = v.findViewById(R.id.txtv_volunteerphone);
                EditText volMsg = v.findViewById(R.id.txtv_volunteermessage);

                orgName.setText(projectDetails.getName());
                orgDetails.setText(String.join("\n", projectDetails.getCategory1(),
                        projectDetails.getShortDescription()));

                subject = "Volunteer Application for " + projectDetails.getName();
                body = "Hello, \nMy name is " + volName.getText().toString() + " and I would love "
                        + "an opportunity to help out with the " + projectDetails.getName()
                        + " project." + "\nReasons/Qualifications:\n" + volMsg.getText().toString();

                if (!TextUtils.isEmpty(volEmail.getText())) {
                    contact += "\n" + volEmail.getText().toString();
                }

                if (!TextUtils.isEmpty(volPhone.getText())) {
                    contact += "\n" + volPhone.getText().toString();
                }

                body += contact;
                body += "\n\nThank you,\n" + volName.getText().toString();
            }
        }
    }

    private static class Retrieval extends AsyncTask<Void, Void, List<Project>> {
        private WeakReference<VolunteerFragment> fragment;
        private String url;

        Retrieval(VolunteerFragment fragment, String url) {
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
            this.fragment.get().updateProjectInfo(result);
        }
    }
}
