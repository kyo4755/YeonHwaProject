package com.dongyang.yeonhwaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dongyang.yeonhwaproject.DetailActivity.SettingDetailLocationActivity;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;

/**
 * Created by JongHwa on 2018-05-29.
 */

public class SettingLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;

    boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GPSInfo gpsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_location_main);

        ImageButton cancelBtn = findViewById(R.id.location_cancel);
        cancelBtn.setOnClickListener(this);

        LinearLayout myLocationSetting = findViewById(R.id.location_myloca_setting);
        myLocationSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_myloca_setting:
                findLocationClickListener();
                break;
            case R.id.location_cancel:
                finishActivity();
                break;
        }
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
    }

    private void findLocationClickListener(){

        if(!isPermission){
            callPermission();
            return;
        }

        gpsInfo = new GPSInfo(SettingLocationActivity.this);

        if(gpsInfo.isGetLocation()){
            double latitude = gpsInfo.getLat();
            double longitude = gpsInfo.getLon();

            moveSettingLocationActivity(latitude, longitude);
        } else {
            gpsInfo.showSettingAlert();
        }
    }

    private void moveSettingLocationActivity(double lat, double lon){
        Intent it = new Intent(SettingLocationActivity.this, SettingDetailLocationActivity.class);
        it.putExtra("lat", lat);
        it.putExtra("lon", lon);
        startActivity(it);
        overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
    }

    private void callPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
            findLocationClickListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]grantResults){
        if(requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isAccessFineLocation = true;
        }
        else if(requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isAccessCoarseLocation = true;
        }

        if(isAccessFineLocation && isAccessCoarseLocation){
            isPermission = true;
        }
    }
}
