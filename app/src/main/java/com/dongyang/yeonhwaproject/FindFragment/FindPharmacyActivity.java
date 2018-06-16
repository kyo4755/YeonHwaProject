package com.dongyang.yeonhwaproject.FindFragment;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.FindMainAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.Connection.NetworkTask;
import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FindPharmacyActivity extends Fragment{

    private FindPOJO data;
    private ArrayList<FindPOJO> arrays;
    FindMainAdapter adapter;

    LottieAnimationView loadingAnim;
    ListView listView;

    private int pageNo = 1;
    boolean isLastItemVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_pharmacy_content, container, false);

        listView = view.findViewById(R.id.find_pharmacy_listview);
        arrays = new ArrayList<>();
        adapter = new FindMainAdapter(arrays);
        listView.setAdapter(adapter);

        loadingAnim = view.findViewById(R.id.find_pharmacy_loading);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getContext(), FindDetailActivity.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isLastItemVisible){
                    getXMLData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        getXMLData();

        return view;
    }

    private void getXMLData() {
        loadingAnim.setVisibility(View.VISIBLE);
        loadingAnim.playAnimation();
        loadingAnim.loop(true);

        pageNo++;

        AQuery aQuery = new AQuery(getContext());
        String find_hospital_url = GlobalInfo.SERVER_URL + "find/pharmacy";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("page", String.valueOf(pageNo));

        if(GlobalInfo.isSettingLocation){
            params.put("lon", String.valueOf(GlobalInfo.settingLongitude));
            params.put("lat", String.valueOf(GlobalInfo.settingLatitude));
        } else {
            GPSInfo gpsInfo = new GPSInfo(getContext());
            if(gpsInfo.isGetLocation()) {
                double latitude = gpsInfo.getLat();
                double longitude = gpsInfo.getLon();

                params.put("lon", String.valueOf(longitude));
                params.put("lat", String.valueOf(latitude));
            }
        }

        aQuery.ajax(find_hospital_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Toast.makeText(getContext(), "LAT 전달 오류", Toast.LENGTH_SHORT).show();
                    } else if(result_code.equals("0002")){
                        Toast.makeText(getContext(), "LON 전달 오류", Toast.LENGTH_SHORT).show();
                    } else if(result_code.equals("0003")){
                        Toast.makeText(getContext(), "page 전달 오류", Toast.LENGTH_SHORT).show();
                    } else {
                        arrays = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(jsonObject.get("pharmacy_list").toString());

                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jObject = jsonArray.getJSONObject(i);
                            FindPOJO data = new FindPOJO();
                            data.setHpid(jObject.getString("hpid"));
                            data.setDistance(jObject.getString("distance"));
                            data.setAddress(jObject.getString("dutyAddr"));
                            data.setName(jObject.getString("dutyName"));
                            data.setTel(jObject.getString("dutyTel1"));
                            data.setAvg_point(jObject.getString("avg_point"));
                            data.setReview_count(jObject.getString("review_count"));
                            data.setIsHosPhar("phar");

                            arrays.add(data);
                        }

                        adapter.getArItem().addAll(arrays);
                        loadingAnim.setVisibility(View.GONE);
                        loadingAnim.pauseAnimation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}