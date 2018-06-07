package com.dongyang.yeonhwaproject.DetailActivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.FindActivity;
import com.dongyang.yeonhwaproject.R;
import com.dongyang.yeonhwaproject.SettingLocationActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by JongHwa on 2018-05-29.
 */

public class SettingDetailLocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    TextView location_default, location_detail;

    Geocoder geocoder;
    GoogleMap mGoogleMap;
    private Marker currentMarker = null;

    double newLat, newLon;

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

        Button settingLocationBtn = findViewById(R.id.location_setting_btn);
        settingLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalInfo.isSettingLocation = true;
                GlobalInfo.settingLatitude = newLat;
                GlobalInfo.settingLongitude = newLon;

                Toast.makeText(SettingDetailLocationActivity.this, "주소가 설정되었습니다.", Toast.LENGTH_SHORT).show();

                SettingLocationActivity activity1 = (SettingLocationActivity)SettingLocationActivity.settingLocationActivity;
                FindActivity activity2 = (FindActivity)FindActivity.findActivity;

                activity1.finish();
                activity2.finish();
                finish();
            }
        });
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
        mGoogleMap = googleMap;

        setDefaultLocation();

        Intent it = getIntent();
        double lat = it.getDoubleExtra("lat", 37.56);
        double lon = it.getDoubleExtra("lon", 126.97);
        String location = it.getStringExtra("location");
        System.out.println(location);

        Log.e("위경도", lat + ", " + lon);

        geocoder = new Geocoder(this);

        List<Address> list = null;

        try {
            if(location != null){
                list = geocoder.getFromLocationName(location, 1);
            }else{
                list = geocoder.getFromLocation(lat, lon, 1);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        if(list != null){
            if(list.size() == 0){
                Toast.makeText(this, "해당되는 주소 정보는 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder title = new StringBuilder();
                StringBuilder subTitle = new StringBuilder();

                Address address = list.get(0);

                String admin = address.getAdminArea();
                String locality = address.getLocality();
                String thoroughFare = address.getThoroughfare();
                String feature = address.getFeatureName();

                if(admin != null){
                    if(locality != null)    title.append(admin).append(" ").append(locality);
                    else                    title.append(admin);
                } else {
                    if(locality != null)    title.append(locality);
                    else                    title.append("");
                }

                if(thoroughFare != null){
                    if(feature != null)     subTitle.append(thoroughFare).append(" ").append(feature);
                    else                    subTitle.append(thoroughFare);
                } else {
                    if(feature != null)     subTitle.append(feature);
                    else                    subTitle.append("");
                }

                if(location != null){
                    newLat = address.getLatitude();
                    newLon = address.getLongitude();
                } else {
                    newLat = lat;
                    newLon = lon;
                }

                LatLng where = new LatLng(newLat, newLon);

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(where));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(where);
                markerOptions.title(title.toString());
                markerOptions.snippet(subTitle.toString());
                googleMap.addMarker(markerOptions).showInfoWindow();

                location_default.setText(title.toString());
                location_detail.setText(subTitle);
            }

        }

    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
    }

    public void setDefaultLocation() {

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요";

        if(currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }
}
