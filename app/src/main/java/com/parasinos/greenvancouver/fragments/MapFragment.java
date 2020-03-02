package com.parasinos.greenvancouver.fragments;

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
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Project;
import com.parasinos.greenvancouver.tasks.MapMarkerGenerator;

import java.util.ArrayList;
import java.util.List;



public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static List<Project> projectList= new ArrayList<>();

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

//        String refine = "";
//        int numRecords = 0;
        String query = "energy";
        String service_url = getString(R.string.map_api_url, query);
        new MapMarkerGenerator(getActivity(), service_url).execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng vancouver = new LatLng(49.2827, -123.1207);
        googleMap.addMarker(new MarkerOptions().position(vancouver).title("City Hall"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(vancouver));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(49.2276,-123.0076)).title("MetroTown"));
        for(Project p: projectList){
            String title = p.toString();
            LatLng latLng = new LatLng(p.getField().getGeom().getCoordinates()[1],
                    p.getField().getGeom().getCoordinates()[0]);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        }

        CameraPosition initPosition
                = CameraPosition.builder().target(vancouver).zoom(11).bearing(0).tilt(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(initPosition));

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}