package com.parasinos.greenvancouver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.parasinos.greenvancouver.ProjectInfoActivity;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.models.Project;
import com.parasinos.greenvancouver.tasks.MapMarkerGenerator;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, EditText.OnEditorActionListener {

    private GoogleMap googleMap;
    private List<Marker> markers;
    //    private List<Project> projectList;
    private List<String> mapIDList;

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
                int numRecords = 10;
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
        markers = new ArrayList<>();
        mapIDList = new ArrayList<>();

        // get keyword from the search bar as query string
        EditText etQuery = view.findViewById(R.id.searchText);
        String query = etQuery.getText().toString();
        int numRecords = 10;
        String service_url = getString(R.string.map_api_url, query, numRecords);
        new MapMarkerGenerator(this, service_url).execute();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String mapID = "";
                for (int i = 0; i < markers.size(); i++) {
                    if (marker.equals(markers.get(i))) {
//                        mapID = projectList.get(i).getField().getMapID();
                        mapID = mapIDList.get(i);
                    }
                }
                // pass mapID of the selected project to ProjectInfoActivity
                Intent intent = new Intent(getActivity(), ProjectInfoActivity.class);
                intent.putExtra("mapID", mapID);
//                marker.showInfoWindow();
                startActivity(intent);
                return true;
            }
        });

        // Initialize Camera focus
        LatLng vancouver = new LatLng(49.2827, -123.1207);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(vancouver));
        CameraPosition initPosition
                = CameraPosition.builder().target(vancouver).zoom(10).bearing(0).tilt(0).build();
        this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(initPosition));

    }

    public void updateMarkers(List<Project> projectList) {
        for (Project p : projectList) {
//            this.projectList.add(p);
            String title = p.toString();
            LatLng latLng = new LatLng(p.getField().getGeom().getCoordinates()[1],
                    p.getField().getGeom().getCoordinates()[0]);
            String mapID = p.getField().getMapID();
            mapIDList.add(mapID);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            markers.add(marker);
        }
    }


}