package com.dongyang.yeonhwaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongyang.yeonhwaproject.Adapter.FindTabPagerAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;

    public static Activity findActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_main);

        findActivity = FindActivity.this;

        Intent it = getIntent();
        int tabCount = it.getIntExtra("tabCount", 0);

        Toolbar toolbar = findViewById(R.id.toolbar_find_hp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        findLocation();

        TabLayout tabLayout = findViewById(R.id.find_toolbar_tab);
        tabLayout.addTab(tabLayout.newTab().setText("병원").setIcon(R.drawable.tab_hostpital));
        tabLayout.addTab(tabLayout.newTab().setText("약국").setIcon(R.drawable.tab_pharmacy));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.find_viewPager);

        FindTabPagerAdapter pagerAdapter = new FindTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        // intent로 받은 값에 따라 tablayout의 content를 변환
        viewPager.setCurrentItem(tabCount);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ImageButton back_btn = findViewById(R.id.find_back_btn);
        back_btn.setOnClickListener(this);

        LinearLayout findLocation = findViewById(R.id.find_my_location);
        findLocation.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.find_back_btn :
                finishActivity();
                break;
            case R.id.find_my_location :
                moveSettingLocationActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
    }

    private void moveSettingLocationActivity(){
        Intent it = new Intent(FindActivity.this, SettingLocationActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
    }

    private void findLocation(){

        GPSInfo gpsInfo = new GPSInfo(FindActivity.this);

        if(gpsInfo.isGetLocation()){
            double latitude = gpsInfo.getLat();
            double longitude = gpsInfo.getLon();

            if(GlobalInfo.isSettingLocation){
                latitude = GlobalInfo.settingLatitude;
                longitude = GlobalInfo.settingLongitude;
            }

            Geocoder geocoder = new Geocoder(this);

            List<Address> list = null;

            try {
                list = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e){
                e.printStackTrace();
            }

            if(list != null){
                if(list.size() == 0){
                    Toast.makeText(this, "해당되는 주소 정보는 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("FindLocation", list.get(0).toString());

                    TextView myLocation = findViewById(R.id.find_my_location_text);

                    Address address = list.get(0);

                    String locality = address.getLocality();
                    String thoroughFare = address.getThoroughfare();
                    String feature = address.getFeatureName();

                    String locationStr;

                    if(feature == null){
                        locationStr = locality + " " + thoroughFare;
                    } else {
                        locationStr = thoroughFare + " " + feature;
                    }

                    myLocation.setText(locationStr);

//                    String cut[] = list.get(0).toString().split(" ");
//                    for(int i=0; i<cut.length; i++){
//                        System.out.println("cut["+i+"] : " + cut[i]);
//                    }
//
//                    if(cut.length == 6){
//                        String [] innerCut = cut[5].split("\"");
//                        myLocation.setText(cut[4] + " " + innerCut[0]);
//                    }else if(cut.length == 5) {
//                        String [] innerCut = cut[4].split("\"");
//                        myLocation.setText(cut[3] + " " + innerCut[0]);
//                    } else if(cut.length == 4) {
//                        String [] innerCut = cut[3].split("\"");
//                        myLocation.setText(cut[2] + " " + innerCut[0]);
//                    } else if(cut.length == 3) {
//                        String [] innerCut = cut[2].split("\"");
//                        myLocation.setText(cut[1] + " " + innerCut[0]);
//                    }
                }

            }

        } else {
            gpsInfo.showSettingAlert();
        }
    }


}
