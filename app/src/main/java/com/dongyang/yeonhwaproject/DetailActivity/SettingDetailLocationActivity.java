package com.dongyang.yeonhwaproject.DetailActivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongyang.yeonhwaproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by JongHwa on 2018-05-29.
 */

public class SettingDetailLocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    TextView location_default, location_detail;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_detail_location_main);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.location_detail_map);
        mapFragment.getMapAsync(this);

        ImageButton cancelBtn = findViewById(R.id.location_detail_cancel);
        cancelBtn.setOnClickListener(this);

        location_default = findViewById(R.id.location_detail_address_default);
        location_detail = findViewById(R.id.location_detail_address_road);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_detail_cancel:
                finishActivity();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent it = getIntent();
        double lat = it.getDoubleExtra("lat", 37.56);
        double lon = it.getDoubleExtra("lon", 126.97);

        Log.e("위경도", lat + ", " + lon);

        geocoder = new Geocoder(this);

        List<Address> list = null;

        try {
            list = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e){
            e.printStackTrace();
        }

        String countryName, postalCode, locality, thoroughFare, featureName;

        if(list != null){
            if(list.size() == 0){
                Toast.makeText(this, "해당되는 주소 정보는 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Address address = list.get(0);
                locality = address.getLocality();
                thoroughFare = address.getThoroughfare();
                featureName = address.getFeatureName();

                String detailHolder = thoroughFare + " " + featureName;

                LatLng where = new LatLng(lat, lon);

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(where));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(where);
                markerOptions.title(locality);
                markerOptions.snippet(detailHolder);
                googleMap.addMarker(markerOptions).showInfoWindow();

                location_default.setText(locality);
                location_detail.setText(detailHolder);
            }

        }



    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
    }
}
