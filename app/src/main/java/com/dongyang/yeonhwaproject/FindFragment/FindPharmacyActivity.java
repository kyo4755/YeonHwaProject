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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FindPharmacyActivity extends Fragment{

    private FindPOJO data;
    private ArrayList<FindPOJO> arrays;
    FindMainAdapter adapter;

    LottieAnimationView loadingAnim;
    ListView listView;

    private int pageNo = 0;
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void getXMLData() {
        loadingAnim.setVisibility(View.VISIBLE);
        loadingAnim.playAnimation();
        loadingAnim.loop(true);

        pageNo++;

        ContentValues params = new ContentValues();
        params.put("ServiceKey", GlobalInfo.findPharKey);

        if(GlobalInfo.isSettingLocation){
            params.put("yPos", String.valueOf(GlobalInfo.settingLatitude));
            params.put("xPos", String.valueOf(GlobalInfo.settingLongitude));
            params.put("pageNo", pageNo);
        } else {
            GPSInfo gpsInfo = new GPSInfo(getContext());
            if(gpsInfo.isGetLocation()) {
                double latitude = gpsInfo.getLat();
                double longitude = gpsInfo.getLon();

                params.put("yPos", String.valueOf(latitude));
                params.put("xPos", String.valueOf(longitude));
            }
        }

        FindHosPharNetworkTask findHosPharNetworkTask = new FindPharmacyActivity.FindHosPharNetworkTask(GlobalInfo.findPharURL, params);
        findHosPharNetworkTask.execute();


    }

    private class FindHosPharNetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        FindHosPharNetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... voids) {
            NetworkTask networkTask = new NetworkTask();
            InputStream result = networkTask.request(url, values);

            try {
                final int STEP_ITEM = 0, STEP_DIS = 1, STEP_ADDR = 2, STEP_NAME = 3,
                        STEP_TEL = 4, STEP_LAT = 5, STEP_LON = 6, STEP_HPID = 7, STEP_NONE = 100;

                int current_step = STEP_NONE;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(result, null);
                int eventType = parser.getEventType();

                arrays = new ArrayList<>();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String startTag = parser.getName();
                        if (startTag.equals("item")){
                            current_step = STEP_ITEM;
                            data = new FindPOJO();
                        }
                        else if (startTag.equals("distance"))       current_step = STEP_DIS;
                        else if (startTag.equals("addr"))       current_step = STEP_ADDR;
                        else if (startTag.equals("yadmNm"))       current_step = STEP_NAME;
                        else if (startTag.equals("telno"))       current_step = STEP_TEL;
                        else if (startTag.equals("xPos"))       current_step = STEP_LAT;
                        else if (startTag.equals("yPos"))      current_step = STEP_LON;
                        else if (startTag.equals("ykiho"))           current_step = STEP_HPID;
                        else                                        current_step = STEP_NONE;
                    }
                    else if (eventType == XmlPullParser.END_TAG) {
                        String endTag = parser.getName() ;
                        if (endTag.equals("item"))     arrays.add(data);
                    }
                    else if (eventType == XmlPullParser.TEXT) {
                        String text = parser.getText();
                        if (current_step == STEP_DIS){
                            System.out.println("distance : " + text);
                            data.setDistance(text);
                        }
                        else if (current_step == STEP_ADDR)                   data.setAddress(text);
                        else if (current_step == STEP_NAME)                   data.setName(text);
                        else if (current_step == STEP_TEL)                    data.setTel(text);
                        else if (current_step == STEP_LAT)                    data.setLat(text);
                        else if (current_step == STEP_LON)                    data.setLon(text);
                        else if (current_step == STEP_HPID)                   data.setHpid(text);
                    }
                    eventType = parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (arrays.size() > 0) {
                adapter.getArItem().addAll(arrays);
                loadingAnim.setVisibility(View.GONE);
                loadingAnim.pauseAnimation();
            }
        }
    }
}