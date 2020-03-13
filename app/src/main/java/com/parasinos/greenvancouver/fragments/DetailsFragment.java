package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parasinos.greenvancouver.ProjectInfoActivity;
import com.parasinos.greenvancouver.R;

public class DetailsFragment extends Fragment {


    public static DetailsFragment newInstance(/*String param1, String param2*/) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        ProjectInfoActivity infoActivity = (ProjectInfoActivity) getActivity();
        if (infoActivity != null) {
            String mapID = infoActivity.mapID;
            View v = getView();
            if (v != null) {
            TextView tvTest = v.findViewById(R.id.tv_projectType);
            tvTest.setText(mapID);
            }
        }


    }

}
