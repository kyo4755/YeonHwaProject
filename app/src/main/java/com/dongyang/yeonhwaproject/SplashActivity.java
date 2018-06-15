package com.dongyang.yeonhwaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;

    boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    LottieAnimationView loadingAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        loadingAnim = findViewById(R.id.splash_loading);

        callPermission();

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
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
            nextActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;
        }
//        else if(requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            isAccessCoarseLocation = true;
//        }

        if (isAccessFineLocation) {
            isPermission = true;
            nextActivity();
        } else {
            shutdownApp();
        }
    }

    private void nextActivity() {
        if (isPermission) {
            loadingAnim.setVisibility(View.VISIBLE);
            loadingAnim.playAnimation();
            loadingAnim.loop(true);

            AQuery aQuery = new AQuery(SplashActivity.this);
            String getDrugListURL = GlobalInfo.SERVER_URL + "drugs/getList";

            aQuery.ajax(getDrugListURL, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String result, AjaxStatus status) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String result_code = jsonObject.get("result").toString();

                        if (result_code.equals("0010")) {
                            Toast.makeText(SplashActivity.this, "약 정보를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONArray jsonArray = new JSONArray(jsonObject.get("drug_list").toString());
                            GlobalInfo.drug_id = new String[jsonArray.length()];
                            GlobalInfo.drug_name = new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObject = jsonArray.getJSONObject(i);

                                GlobalInfo.drug_id[i] = jObject.getString("id");
                                GlobalInfo.drug_name[i] = jObject.getString("name");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        loadingAnim.setVisibility(View.GONE);
                        loadingAnim.pauseAnimation();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                SplashActivity.this.startActivity(mainIntent);
                                SplashActivity.this.finish();
                                overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
                            }
                        }, SPLASH_DISPLAY_LENGTH);
                    }
                }
            });
        } else {
            shutdownApp();
        }
    }

    private void shutdownApp() {
        ActivityCompat.finishAffinity(this);
        System.runFinalizersOnExit(true);
        System.exit(0);
    }
}
