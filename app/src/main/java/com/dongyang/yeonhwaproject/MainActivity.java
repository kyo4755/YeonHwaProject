package com.dongyang.yeonhwaproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dongyang.yeonhwaproject.FindFragment.FindDrugsActivity;
import com.dongyang.yeonhwaproject.FindFragment.FindHospitalActivity;
import com.dongyang.yeonhwaproject.FindFragment.FindPharmacyActivity;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public RequestManager glideManager;

    LinearLayout findHospital, findPharmacy, findDrugs, settings;
    TextView login_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        glideManager = Glide.with(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        final ImageView drawer_user_image = header.findViewById(R.id.drawer_user_image);
        drawer_user_image.post(new Runnable() {
            @Override
            public void run() {
                glideManager.load(R.drawable.default_user)
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                        .into(drawer_user_image);
            }
        });

        findHospital = findViewById(R.id.main_find_hospital);
        findPharmacy = findViewById(R.id.main_find_pharmacy);
        findDrugs = findViewById(R.id.main_find_drugs);
        settings = findViewById(R.id.main_settings);

        login_btn = header.findViewById(R.id.drawer_login_btn);
        register_btn = header.findViewById(R.id.drawer_register_btn);

        findHospital.setOnClickListener(this);
        findPharmacy.setOnClickListener(this);
        findDrugs.setOnClickListener(this);
        settings.setOnClickListener(this);

        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()){
            case R.id.menu_hospital :
                findHospitalClickListener();
                break;
            case R.id.menu_pharmacy :
                findPharmacyClickListener();
                break;
            case R.id.menu_drugs :
                findDrugsClickListener();
                break;
            case R.id.menu_setting :
                SettingsClickListener();
                break;
        }
        overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.main_find_hospital :
                findHospitalClickListener();
                break;
            case R.id.main_find_pharmacy :
                findPharmacyClickListener();
                break;
            case R.id.main_find_drugs :
                findDrugsClickListener();
                break;
            case R.id.main_settings :
                SettingsClickListener();
                break;
            case R.id.drawer_login_btn:
                LoginBtnClickListener();
                break;
            case R.id.drawer_register_btn:
                RegisterBtnClickListener();
                break;
        }
        overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
    }

    private void findHospitalClickListener() {
        Intent it = new Intent(MainActivity.this, FindActivity.class);
        it.putExtra("tabCount", 0);
        startActivity(it);
    }

    private void findPharmacyClickListener() {
        Intent it = new Intent(MainActivity.this, FindActivity.class);
        it.putExtra("tabCount", 1);
        startActivity(it);
    }

    private void findDrugsClickListener() {
        Intent it = new Intent(MainActivity.this, FindDrugsActivity.class);
        startActivity(it);
    }

    private void SettingsClickListener() {
        Intent it = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(it);
    }

    private void LoginBtnClickListener(){
        Intent it = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(it);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void RegisterBtnClickListener(){
        Intent it = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(it);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
