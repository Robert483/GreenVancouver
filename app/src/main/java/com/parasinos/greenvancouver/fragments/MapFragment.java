package com.parasinos.greenvancouver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parasinos.greenvancouver.ProjectInfoActivity;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Project;
import com.parasinos.greenvancouver.tasks.MapMarkerGenerator;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private List<Marker> markers;
    private List<String> mapIDList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.frag_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        super.onViewCreated(view, savedInstanceState);
        markers = new ArrayList<>();
        mapIDList = new ArrayList<>();

        // get keyword from the search bar as query string
        String query = "energy";
        int numRecords = 10;
        String service_url = getString(R.string.map_api_url, query, numRecords);
        new MapMarkerGenerator(this, service_url).execute();
//        for (Marker marker : markers){
//            marker.on
//        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String mapID ="";
                for (int i = 0; i < markers.size(); i++){
                    if (marker.equals(markers.get(i))){
                        mapID = mapIDList.get(i);
                    }
                }
                Intent intent = new Intent(getActivity(), ProjectInfoActivity.class);
                intent.putExtra("mapID", mapID);
                marker.showInfoWindow();
                return false;
            }
        });

        LatLng vancouver = new LatLng(49.2827, -123.1207);
        this.googleMap.addMarker(new MarkerOptions().position(vancouver).title("City Hall"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(vancouver));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(49.2276, -123.0076)).title("MetroTown"));

        CameraPosition initPosition
                = CameraPosition.builder().target(vancouver).zoom(12).bearing(0).tilt(0).build();
        this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(initPosition));

    }

    public void updateMarkers(List<Project> projectList) {
        for (Project p : projectList) {
            String title = p.toString();
            LatLng latLng = new LatLng(p.getField().getGeom().getCoordinates()[1],
                    p.getField().getGeom().getCoordinates()[0]);
            String mapID = p.getField().getMapID();
            mapIDList.add(mapID);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
            markers.add(marker);
        }
    }

//
//    public boolean onMarkerClick(Marker marker) {
//        Intent intent = new Intent(getActivity(), ProjectInfoActivity.class);
//        marker.showInfoWindow();
//        return false;
//    }

}