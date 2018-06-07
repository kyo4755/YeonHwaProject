package com.dongyang.yeonhwaproject.DetailActivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Toolbar toolbar;

    TextView detail_location, detail_phone;
    Button detail_toolbar_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_find_main);

        toolbar = findViewById(R.id.detail_toolbar);
        detail_location = findViewById(R.id.detail_location);
        detail_phone= findViewById(R.id.detail_phone);

        Intent intent = getIntent();
        toolbar.setTitle(intent.getStringExtra("prefab_name"));
        detail_location.setText(intent.getStringExtra("prefab_address"));
        detail_phone.setText(intent.getStringExtra("prefab_tel"));


        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.detail_mapview);
        mapFragment.getMapAsync(this);

        detail_toolbar_call = findViewById(R.id.detail_toolbar_call);
        detail_toolbar_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+intent.getStringExtra("prefab_tel"))));

                //startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));

            }
        });


    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Intent intent = getIntent();

        LatLng SEOUL = new LatLng(Double.parseDouble(intent.getStringExtra("x_lat")), Double.parseDouble(intent.getStringExtra("y_lon")));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);

        markerOptions.title("병원");
        markerOptions.snippet(intent.getStringExtra("prefab_name"));

        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}
