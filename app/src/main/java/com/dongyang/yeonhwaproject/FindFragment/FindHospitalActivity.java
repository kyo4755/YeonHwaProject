package com.dongyang.yeonhwaproject.FindFragment;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dongyang.yeonhwaproject.FindActivity;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FindHospitalActivity extends Fragment{

    private FindPOJO data;
    private ArrayList<FindPOJO> arrays;
    FindMainAdapter adapter;

    LottieAnimationView loadingAnim;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_hospital_content, container, false);

        arrays = new ArrayList<>();

        listView = view.findViewById(R.id.find_hospital_listview);

        loadingAnim = view.findViewById(R.id.find_hospital_loading);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getContext(), FindDetailActivity.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
            }
        });

        getXMLData();

        return view;
    }

    private void getXMLData() {
        loadingAnim.setVisibility(View.VISIBLE);
        loadingAnim.playAnimation();
        loadingAnim.loop(true);

        AQuery aQuery = new AQuery(getActivity());
        ContentValues params = new ContentValues();
        params.put("serviceKey", GlobalInfo.findHosPharKey);

        if(GlobalInfo.isSettingLocation){
            params.put("WGS84_LON", String.valueOf(GlobalInfo.settingLongitude));
            params.put("WGS84_LAT", String.valueOf(GlobalInfo.settingLatitude));
        } else {
            GPSInfo gpsInfo = new GPSInfo(getContext());
            if(gpsInfo.isGetLocation()) {
                double latitude = gpsInfo.getLat();
                double longitude = gpsInfo.getLon();

                params.put("WGS84_LON", String.valueOf(longitude));
                params.put("WGS84_LAT", String.valueOf(latitude));
            }
        }

        FindHosPharNetworkTask findHosPharNetworkTask = new FindHosPharNetworkTask(GlobalInfo.findHosPharURL, params);
        findHosPharNetworkTask.execute();

        /*String fullURL = addParams(GlobalInfo.findHosPharURL, params);
        Log.e("message", fullURL);

        aQuery.ajax(fullURL, String.class, new AjaxCallback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    final int STEP_ITEM = 0, STEP_DIS = 1, STEP_ADDR = 2, STEP_NAME = 3,
                            STEP_TEL = 4, STEP_LAT = 5, STEP_LON = 6, STEP_HPID = 7, STEP_NONE = 100;

                    int current_step = STEP_NONE;

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();

                    InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));

                    parser.setInput(is, null);
                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String startTag = parser.getName();
                            if (startTag.equals("item")){
                                current_step = STEP_ITEM;
                                data = new FindPOJO();
                            }
                            else if (startTag.equals("distance"))       current_step = STEP_DIS;
                            else if (startTag.equals("dutyAddr"))       current_step = STEP_ADDR;
                            else if (startTag.equals("dutyName"))       current_step = STEP_NAME;
                            else if (startTag.equals("dutyTel1"))       current_step = STEP_TEL;
                            else if (startTag.equals("latitude"))       current_step = STEP_LAT;
                            else if (startTag.equals("longitude"))      current_step = STEP_LON;
                            else if (startTag.equals("hpid"))           current_step = STEP_HPID;
                            else                                        current_step = STEP_NONE;
                        }
                        else if (eventType == XmlPullParser.END_TAG) {
                            String endTag = parser.getName() ;
//                            if ((endTag.equals("item") && current_step != STEP_ITEM) ||
//                                    (endTag.equals("distance") && current_step != STEP_DIS) ||
//                                    (endTag.equals("dutyAddr") && current_step != STEP_ADDR) ||
//                                    (endTag.equals("dutyName") && current_step != STEP_NAME) ||
//                                    (endTag.equals("dutyTel1") && current_step != STEP_TEL) ||
//                                    (endTag.equals("latitude") && current_step != STEP_LAT) ||
//                                    (endTag.equals("longitude") && current_step != STEP_LON) ||
//                                    (endTag.equals("hpid") && current_step != STEP_HPID))
//                            {
//                                Toast.makeText(getContext(), "XML 파싱 오류", Toast.LENGTH_SHORT).show();
//                            }
                            if (endTag.equals("item"))     arrays.add(data);
                        }
                        else if (eventType == XmlPullParser.TEXT) {
                            String text = parser.getText();
                            if (current_step == STEP_DIS){
                                Log.e("Distance", "================" + text);
                                data.setDistance(text);
                            }
                            else if (current_step == STEP_ADDR){
                                //Log.e("Address", "================" + text);
                                data.setAddress(text);
                            }
                            else if (current_step == STEP_NAME){
                                //Log.e("Name", "================" + text);
                                data.setName(text);
                            }
                            else if (current_step == STEP_TEL){
                                //Log.e("Tel", "================" + text);
                                data.setTel(text);
                            }
                            else if (current_step == STEP_LAT){
                                //Log.e("Lat", "================" + text);
                                data.setLat(text);
                            }
                            else if (current_step == STEP_LON){
                                //Log.e("Lon", "================" + text);
                                data.setLon(text);
                            }
                            else if (current_step == STEP_HPID){
                                //Log.e("hpid", "================" + text);
                                data.setHpid(text);
                            }
                        }

                        eventType = parser.next();
                    }

                    if (arrays.size() > 0) {

                        adapter.getArItem().addAll(arrays);
                        adapter.notifyDataSetChanged();

                        loadingAnim.setVisibility(View.GONE);
                        loadingAnim.pauseAnimation();
                    }

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    private String addParams(String aurl, HashMap<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder(aurl + "?");

        if (params != null) {
            for (String key : params.keySet()) {
                stringBuilder.append(key + "=");
                stringBuilder.append(params.get(key) + "&");
            }
        }
        return stringBuilder.toString();
    }

    private String safeNextText(XmlPullParser parser, String name)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, name);
        String itemText = parser.nextText();
        if (parser.getEventType() != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, name);
        System.out.println("menu option: " + itemText);
        return itemText;
    }

    private class FindHosPharNetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public FindHosPharNetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {
            NetworkTask networkTask = new NetworkTask();
            InputStream is = networkTask.request(url, values);

            if (is != null) {
                try {
                    final int STEP_ITEM = 0, STEP_DIS = 1, STEP_ADDR = 2, STEP_NAME = 3,
                            STEP_TEL = 4, STEP_LAT = 5, STEP_LON = 6, STEP_HPID = 7, STEP_NONE = 100;

                    int current_step = STEP_NONE;

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();

                    parser.setInput(is, null);
                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String startTag = parser.getName();
                            if (startTag.equals("item")){
                                current_step = STEP_ITEM;
                                data = new FindPOJO();
                            }
                            else if (startTag.equals("distance"))       current_step = STEP_DIS;
                            else if (startTag.equals("dutyAddr"))       current_step = STEP_ADDR;
                            else if (startTag.equals("dutyName"))       current_step = STEP_NAME;
                            else if (startTag.equals("dutyTel1"))       current_step = STEP_TEL;
                            else if (startTag.equals("latitude"))       current_step = STEP_LAT;
                            else if (startTag.equals("longitude"))      current_step = STEP_LON;
                            else if (startTag.equals("hpid"))           current_step = STEP_HPID;
                            else                                        current_step = STEP_NONE;
                        }
                        else if (eventType == XmlPullParser.END_TAG) {
                            String endTag = parser.getName() ;
//                            if ((endTag.equals("item") && current_step != STEP_ITEM) ||
//                                    (endTag.equals("distance") && current_step != STEP_DIS) ||
//                                    (endTag.equals("dutyAddr") && current_step != STEP_ADDR) ||
//                                    (endTag.equals("dutyName") && current_step != STEP_NAME) ||
//                                    (endTag.equals("dutyTel1") && current_step != STEP_TEL) ||
//                                    (endTag.equals("latitude") && current_step != STEP_LAT) ||
//                                    (endTag.equals("longitude") && current_step != STEP_LON) ||
//                                    (endTag.equals("hpid") && current_step != STEP_HPID))
//                            {
//                                Toast.makeText(getContext(), "XML 파싱 오류", Toast.LENGTH_SHORT).show();
//                            }
                            if (endTag.equals("item"))     arrays.add(data);
                        }
                        else if (eventType == XmlPullParser.TEXT) {
                            String text = parser.getText();
                            if (current_step == STEP_DIS){
                                Log.e("Distance", "================" + text);
                                data.setDistance(text);
                            }
                            else if (current_step == STEP_ADDR){
                                //Log.e("Address", "================" + text);
                                data.setAddress(text);
                            }
                            else if (current_step == STEP_NAME){
                                //Log.e("Name", "================" + text);
                                data.setName(text);
                            }
                            else if (current_step == STEP_TEL){
                                //Log.e("Tel", "================" + text);
                                data.setTel(text);
                            }
                            else if (current_step == STEP_LAT){
                                //Log.e("Lat", "================" + text);
                                data.setLat(text);
                            }
                            else if (current_step == STEP_LON){
                                //Log.e("Lon", "================" + text);
                                data.setLon(text);
                            }
                            else if (current_step == STEP_HPID){
                                //Log.e("hpid", "================" + text);
                                data.setHpid(text);
                            }
                        }
                        eventType = parser.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getContext(), "서버와의 통신 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (arrays.size() > 0) {
                adapter = new FindMainAdapter(arrays);
                listView.setAdapter(adapter);

                loadingAnim.setVisibility(View.GONE);
                loadingAnim.pauseAnimation();
            }
        }
    }
}