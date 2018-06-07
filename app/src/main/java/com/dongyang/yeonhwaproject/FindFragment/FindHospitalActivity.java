package com.dongyang.yeonhwaproject.FindFragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.FindMainAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_hospital_content, container, false);

        arrays = new ArrayList<>();

        ListView listView = view.findViewById(R.id.find_hospital_listview);
        adapter = new FindMainAdapter(arrays);

        loadingAnim = view.findViewById(R.id.find_hospital_loading);

        listView.setAdapter(adapter);
        getXMLData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getContext(), FindDetailActivity.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
            }
        });

        return view;
    }

    private void getXMLData() {
        loadingAnim.setVisibility(View.VISIBLE);
        loadingAnim.playAnimation();
        loadingAnim.loop(true);

        AQuery aQuery = new AQuery(getActivity());
        HashMap<String, String> params = new HashMap<>();
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

        String fullURL = addParams(GlobalInfo.findHosPharURL, params);
        Log.e("message", fullURL);

        aQuery.ajax(fullURL, String.class, new AjaxCallback<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    String startTag, endTag;
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();

                    InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));

                    parser.setInput(is, null);
                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                startTag = parser.getName();
                                if (startTag.equals("item"))        data  = new FindPOJO();
                                if (startTag.equals("dutyName"))    data.setName(parser.nextText());
                                if (startTag.equals("dutyAddr"))    data.setAddress(parser.nextText());
                                if (startTag.equals("dutyTel1"))    data.setTel(parser.nextText());
                                if (startTag.equals("distance"))    data.setDistance(parser.nextText());
                                break;
                            case XmlPullParser.END_TAG:
                                endTag = parser.getName();
                                if (endTag.equals("item"))          arrays.add(data);
                                break;
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
        });
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
}