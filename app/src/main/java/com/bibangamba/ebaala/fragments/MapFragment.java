package com.bibangamba.ebaala.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bibangamba.ebaala.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by davy on 5/9/2018.
 */

public class MapFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap_;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_map);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Log.e("INFO 1", "Laaunced Frag");

        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(0.348482, 32.582942))
                .bearing(0)
                .build();

        Log.e("Neveaa", "Under state");

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng sydney = new LatLng(0.3407, 32.5760);
        googleMap.addMarker(new MarkerOptions().position(sydney)
        .title("Sydney Live")
        .snippet("Tested").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
