package com.dongyang.yeonhwaproject.FindFragment;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.FindDetailAdapter;
import com.dongyang.yeonhwaproject.Adapter.FindMainAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.Connection.NetworkTask;
import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;
import com.dongyang.yeonhwaproject.POJO.FindDetailPOJO;
import com.dongyang.yeonhwaproject.POJO.FindDrugsPOJO;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FindDetailContentActivity extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    private Marker currentMarker = null;

    ListView listView;
    FindDetailAdapter adapter;

    Intent intent;

    FindDetailPOJO data;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_find_content, container, false);

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.detail_mapview);
        mapFragment.getMapAsync(this);

        listView = view.findViewById(R.id.detail_find_content_listview);
        adapter = new FindDetailAdapter();
        listView.setAdapter(adapter);

        getDetailInfo();

        return view;
    }

    private void getDetailInfo() {
        intent = getActivity().getIntent();
        String hpid = intent.getStringExtra("hpid");
        final String ishosphar = intent.getStringExtra("ishosphar");

        String find_detail_url = GlobalInfo.SERVER_URL + "find/getHospital";
        if(ishosphar.equals("phar")){
            find_detail_url = GlobalInfo.SERVER_URL + "find/getPharmacy";
        }

        AQuery aQuery = new AQuery(getContext());

        Map<String, String> params = new LinkedHashMap<>();
        params.put("hpid", hpid);

        aQuery.ajax(find_detail_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Toast.makeText(getContext(), "HPID 전달 오류", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject detail;
                        if(ishosphar.equals("hos")){
                            detail = new JSONObject(jsonObject.getString("hospital_detail"));
                        } else {
                            detail = new JSONObject(jsonObject.getString("pharmacy_detail"));
                        }

                        data = new FindDetailPOJO();
                        if(detail.has("hpid")) data.setHpid(detail.getString("hpid"));
                        if(detail.has("dutyName")) data.setDutyName(detail.getString("dutyName"));
                        if(detail.has("dutyAddr")) data.setDutyAddr(detail.getString("dutyAddr"));
                        if(detail.has("dutyTel1")) data.setDutyTel1(detail.getString("dutyTel1"));
                        if(detail.has("dutyTel3")) data.setDutyTel3(detail.getString("dutyTel3"));
                        if(detail.has("hvgc")) data.setHvgc(detail.getString("hvgc"));
                        if(detail.has("hvec")) data.setHvec(detail.getString("hvec"));
                        if(detail.has("dutyHano")) data.setDutyHano(detail.getString("dutyHano"));
                        if(detail.has("dutyInf")) data.setDutyInf(detail.getString("dutyInf"));
                        if(detail.has("dutyTime1c")) data.setDutyTime1c(detail.getString("dutyTime1c"));
                        if(detail.has("dutyTime2c")) data.setDutyTime2c(detail.getString("dutyTime2c"));
                        if(detail.has("dutyTime3c")) data.setDutyTime3c(detail.getString("dutyTime3c"));
                        if(detail.has("dutyTime4c")) data.setDutyTime4c(detail.getString("dutyTime4c"));
                        if(detail.has("dutyTime5c")) data.setDutyTime5c(detail.getString("dutyTime5c"));
                        if(detail.has("dutyTime6c")) data.setDutyTime6c(detail.getString("dutyTime6c"));
                        if(detail.has("dutyTime7c")) data.setDutyTime7c(detail.getString("dutyTime7c"));
                        if(detail.has("dutyTime8c")) data.setDutyTime8c(detail.getString("dutyTime8c"));
                        if(detail.has("dutyTime1s")) data.setDutyTime1s(detail.getString("dutyTime1s"));
                        if(detail.has("dutyTime2s")) data.setDutyTime2s(detail.getString("dutyTime2s"));
                        if(detail.has("dutyTime3s")) data.setDutyTime3s(detail.getString("dutyTime3s"));
                        if(detail.has("dutyTime4s")) data.setDutyTime4s(detail.getString("dutyTime4s"));
                        if(detail.has("dutyTime5s")) data.setDutyTime5s(detail.getString("dutyTime5s"));
                        if(detail.has("dutyTime6s")) data.setDutyTime6s(detail.getString("dutyTime6s"));
                        if(detail.has("dutyTime7s")) data.setDutyTime7s(detail.getString("dutyTime7s"));
                        if(detail.has("dutyTime8s")) data.setDutyTime8s(detail.getString("dutyTime8s"));
                        if(detail.has("wgs84Lon")) data.setWgs84Lon(detail.getString("wgs84Lon"));
                        if(detail.has("wgs84Lat")) data.setWgs84Lat(detail.getString("wgs84Lat"));
                        if(detail.has("dutyImg")) data.setDutyimg(detail.getString("dutyImg"));

                        ArrayList<FindDrugsPOJO> alist = new ArrayList<>();
                        FindDrugsPOJO adata;

                        if(data.getDutyAddr() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("주소");
                            adata.setMsg(data.getDutyAddr());
                            alist.add(adata);
                        }
                        if(data.getDutyTel1() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("전화번호");
                            adata.setMsg(data.getDutyTel1());
                            alist.add(adata);
                        }
                        if(data.getDutyInf() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("기관 상세 설명");
                            adata.setMsg(data.getDutyInf());
                            alist.add(adata);
                        }
                        if(data.getDutyTime1c() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("운영 시간");


                            adata.setMsg("월요일  -  " + (data.getDutyTime1s() == null ? "휴진" : data.getDutyTime1s() + "  ~  " + data.getDutyTime1c()) + "\n\n" +
                                    "화요일  -  " + (data.getDutyTime2s() == null ? "휴진" : data.getDutyTime2s() + "  ~  " + data.getDutyTime2c()) + "\n\n" +
                                    "수요일  -  " + (data.getDutyTime3s() == null ? "휴진" : data.getDutyTime3s() + "  ~  " + data.getDutyTime3c()) + "\n\n" +
                                    "목요일  -  " + (data.getDutyTime4s() == null ? "휴진" : data.getDutyTime4s() + "  ~  " + data.getDutyTime4c()) + "\n\n" +
                                    "금요일  -  " + (data.getDutyTime5s() == null ? "휴진" : data.getDutyTime5s() + "  ~  " + data.getDutyTime5c()) + "\n\n" +
                                    "토요일  -  " + (data.getDutyTime6s() == null ? "휴진" : data.getDutyTime6s() + "  ~  " + data.getDutyTime6c()) + "\n\n" +
                                    "일요일  -  " + (data.getDutyTime7s() == null ? "휴진" : data.getDutyTime7s() + "  ~  " + data.getDutyTime7c()) + "\n\n" +
                                    "공휴일  -  " + (data.getDutyTime8s() == null ? "휴진" : data.getDutyTime8s() + "  ~  " + data.getDutyTime8c()));
                            alist.add(adata);
                        }
                        if(data.getDutyTel3() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("응급실 전화");
                            adata.setMsg(data.getDutyTel3());
                            alist.add(adata);
                        }
                        if(data.getHvgc() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("입원실 수");
                            adata.setMsg(data.getHvgc());
                            alist.add(adata);
                        }
                        if(data.getHvec() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("응급실 수");
                            adata.setMsg(data.getHvec());
                            alist.add(adata);
                        }
                        if(data.getDutyHano() != null){
                            adata = new FindDrugsPOJO();
                            adata.setExplain("병상 수");
                            adata.setMsg(data.getDutyHano());
                            alist.add(adata);
                        }
                        if(data.getDutyimg() != null){
                            FindDetailActivity.setImage(data.getDutyimg());
                        }

                        adapter.setList(alist);

                        while(mGoogleMap==null);

                        setmGoogleMap();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void setmGoogleMap(){
        setDefaultLocation();

        LatLng SEOUL = new LatLng(Double.parseDouble(data.getWgs84Lat()), Double.parseDouble(data.getWgs84Lon()));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);

        markerOptions.title("병원");
        markerOptions.snippet(data.getDutyName());

        mGoogleMap.addMarker(markerOptions).showInfoWindow();

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
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
        //currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }

}