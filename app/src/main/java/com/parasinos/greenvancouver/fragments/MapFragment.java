package com.parasinos.greenvancouver.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.ProjectInfoActivity;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Project;
import com.parasinos.greenvancouver.tasks.MapMarkerGenerator;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback, //GoogleMap.OnMarkerClickListener,
        View.OnClickListener, EditText.OnEditorActionListener {

    private GoogleMap googleMap;
    private List<Marker> markers;
    private List<String> mapIdList;
    private String selectMapId = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // fragment buttons onclick handling
        Button bZoomIn = v.findViewById(R.id.btnZoomIn);
        bZoomIn.setOnClickListener(this);
        Button bZoomOut = v.findViewById(R.id.btnZoomOut);
        bZoomOut.setOnClickListener(this);
        Button bMapType = v.findViewById(R.id.btnChangeMapType);
        bMapType.setOnClickListener(this);
        Button bTransDetails = v.findViewById(R.id.transDetail);
        bTransDetails.setOnClickListener(this);
        Button bBookMark = v.findViewById(R.id.bookMark);
        bBookMark.setOnClickListener(this);

        // fragment EditText editor action handling
        EditText etQuery = v.findViewById(R.id.searchText);
        etQuery.setOnEditorActionListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnZoomIn:
                this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.btnZoomOut:
                this.googleMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.transDetail:
                if (!selectMapId.equals("")) {
                    Intent intent = new Intent(getActivity(), ProjectInfoActivity.class);
                    intent.putExtra("mapId", selectMapId);
                    startActivity(intent);
                }
                break;
            case R.id.bookMark:
                if (!selectMapId.equals("")) {
                    // TODO

                    Toast.makeText(getActivity(), "Bookmarked " + selectMapId,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnChangeMapType:
                if (this.googleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
                    this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else
                    this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            default:
                // Nothing
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        googleMap.clear();

        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event != null &&
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event == null || !event.isShiftPressed()) {
                String query = v.getText().toString();

                SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("pref", Context.MODE_PRIVATE);
                int numRecords = sharedPreferences.getInt("maxResult", 10);

                String service_url = getString(R.string.map_api_url, query, numRecords);
                new MapMarkerGenerator(this, service_url).execute();
                return true; // consume.
            }
        }
        return false; // pass on to other listeners.
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.frag_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        super.onViewCreated(view, savedInstanceState);

        // initialise markers and mapIdList fo each query
        markers = new ArrayList<>();
        mapIdList = new ArrayList<>();

        // get keyword from the search bar as query string
        EditText etQuery = view.findViewById(R.id.searchText);
        String query = etQuery.getText().toString();

        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("pref", Context.MODE_PRIVATE);
        int numRecords = sharedPreferences.getInt("maxResult", 10);

        String service_url = getString(R.string.map_api_url, query, numRecords);
        new MapMarkerGenerator(this, service_url).execute();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (this.googleMap != null) {
            // set up infoWindow adapter
            this.googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(final Marker marker) {
                    @SuppressLint("InflateParams") final View v = getLayoutInflater().inflate(R.layout.marker_info_window, null);
                    for (int i = 0; i < markers.size(); i++) {
                        if (marker.equals(markers.get(i))) {
                            selectMapId = mapIdList.get(i);
                        }
                    }
//                    final ImageView ivInfo = v.findViewById(R.id.markerInfoImg);
                    TextView tvTitle = v.findViewById(R.id.projectTitle);
                    final TextView tvSnippet = v.findViewById(R.id.snippet);

//                    String path = String.join("/", "projects", selectMapId, "images", "0");
//                    DatabaseReference dbImageRef = FirebaseDatabase.getInstance().getReference(path);
//                    dbImageRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Log.v("E_Value", "DATA: " + dataSnapshot.getValue());
//                            String imgUrl = (String) dataSnapshot.getValue();
//                            assert imgUrl != null;
//                            if (!imgUrl.equals("")) {
//                                Toast.makeText(getActivity(), imgUrl, Toast.LENGTH_SHORT).show();
//                                Picasso.get().load(imgUrl).into(ivInfo);
//
//                                Picasso.get().load(imgUrl).into(ivInfo, new com.squareup.picasso.Callback() {
//                                    @Override
//                                    public void onSuccess() {
//                                        tvSnippet.setText(marker.getSnippet());
//                                        if (marker.isInfoWindowShown()){
//                                            marker.showInfoWindow();
//                                        }
//                                    }
//                                    @Override
//                                    public void onError(Exception e) {
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                    tvTitle.setText(marker.getTitle());
                    tvSnippet.setText(marker.getSnippet());

                    return v;
                }
            });


            // Initialize Camera focus on Vancouver City Hall
            LatLng vancouver = new LatLng(49.2827, -123.1207);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(vancouver));
            CameraPosition initPosition
                    = CameraPosition.builder().target(vancouver).zoom(10).bearing(0).tilt(0).build();
            this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(initPosition));
        }

    }

//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//        for (int i = 0; i < markers.size(); i++) {
//            if (marker.equals(markers.get(i))) {
//                selectMapId = mapIdList.get(i);
//            }
//        }
//        return true;
//    }

    public void updateMarkers(List<Project> projectList) {
        for (Project p : projectList) {
            String title = p.toString();
            LatLng latLng = new LatLng(p.getField().getGeom().getCoordinates()[1],
                    p.getField().getGeom().getCoordinates()[0]);
            String mapID = p.getField().getMapID();
            mapIdList.add(mapID);
            String address = p.getField().getAddress();

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            markerOptions.snippet(address);
            Marker marker = googleMap.addMarker(markerOptions);
            markers.add(marker);
        }
    }

}