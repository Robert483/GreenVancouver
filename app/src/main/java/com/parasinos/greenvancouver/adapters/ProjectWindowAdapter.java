package com.parasinos.greenvancouver.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.parasinos.greenvancouver.R;

public class ProjectWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Bitmap image;
    private View view;
    private TextView tvTitle;
    private TextView tvSnippet;
    private ImageView ivInfo;

    public ProjectWindowAdapter(Context context) {
        view = View.inflate(context, R.layout.item_project, null);
        tvTitle = view.findViewById(R.id.projectTitle);
        tvSnippet = view.findViewById(R.id.snippet);
        ivInfo = view.findViewById(R.id.markerInfoImg);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        tvTitle.setText(marker.getTitle());
        tvSnippet.setText(marker.getSnippet());

        if (image == null) {
            ivInfo.setVisibility(View.GONE);
            ivInfo.setImageResource(android.R.color.transparent);
        } else {
            ivInfo.setVisibility(View.VISIBLE);
            ivInfo.setImageBitmap(image);
            image = null;
        }

        return view;
    }
}
