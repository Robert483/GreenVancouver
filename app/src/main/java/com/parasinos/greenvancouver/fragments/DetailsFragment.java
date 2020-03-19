package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parasinos.greenvancouver.ProjectInfoActivity;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Field;
import com.parasinos.greenvancouver.models.Project;
import com.parasinos.greenvancouver.tasks.SimpleRetrieval;

import java.util.List;

public class DetailsFragment extends Fragment {
    private String mapID;

    public static DetailsFragment newInstance(String mapId) {
        DetailsFragment fragment = new DetailsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        String service_url = getString(R.string.details_api_url, mapID);
        new SimpleRetrieval(this, service_url).execute();

    }

    public void updateProjectInfo(List<Project> result) {
        ProjectInfoActivity infoActivity = (ProjectInfoActivity) getActivity();

        if (infoActivity != null) {
            View v = getView();
            Project project = result.get(0);
            Field projectDetails = project.getField();

            if (v != null) {
                TextView tvTitle = v.findViewById(R.id.tv_projectName);
                TextView tvCategory = v.findViewById(R.id.tv_projectCategory);
                TextView tvType = v.findViewById(R.id.tv_projectType);
                TextView tvAddress = v.findViewById(R.id.tv_projectAddress);
                TextView tvDescription = v.findViewById(R.id.tv_projectDescription);
                TextView tvWebsite = v.findViewById(R.id.tv_projectWebsite);

                tvTitle.setText(project.toString());
                tvCategory.setText(projectDetails.getCategory1() == null ? "N/A" : projectDetails.getCategory1());
                tvType.setText(projectDetails.getCategory2() == null ? "N/A" : projectDetails.getCategory2());
                tvAddress.setText(projectDetails.getAddress() == null ? "N/A" : projectDetails.getAddress());
                tvDescription.setText(projectDetails.getShortDescription() == null ? "N/A" : projectDetails.getShortDescription());
                tvWebsite.setText(projectDetails.getUrl() == null ? "N/A" : projectDetails.getUrl());
            }
        }
    }

}

