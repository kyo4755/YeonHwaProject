package com.dongyang.yeonhwaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.DetailActivity.SettingDetailLocationActivity;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;

/**
 * Created by JongHwa on 2018-05-29.
 */

public class SettingLocationActivity extends AppCompatActivity implements View.OnClickListener{

    public static Activity settingLocationActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_location_main);

        settingLocationActivity = SettingLocationActivity.this;

        ImageButton cancelBtn = findViewById(R.id.location_cancel);
        cancelBtn.setOnClickListener(this);

        LinearLayout myLocationSetting = findViewById(R.id.location_myloca_setting);
        myLocationSetting.setOnClickListener(this);

        final EditText findLocaEdittext = findViewById(R.id.location_edittext);
        findLocaEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        if(findLocaEdittext.length() != 0){
                            Intent it = new Intent(SettingLocationActivity.this, SettingDetailLocationActivity.class);
                            it.putExtra("location", findLocaEdittext.getText().toString());
                            startActivity(it);
                        }
                }

                return true;
            }
        });
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

        GPSInfo gpsInfo = new GPSInfo(SettingLocationActivity.this);

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


}
