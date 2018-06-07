package com.dongyang.yeonhwaproject.FindFragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.FindMainAdapter;
import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
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

    private final String key = "qykcrKr3huKnjZV66xsvPHACE4seVKMSy6yWtpXPqvEBBWLFqYkzcm7bNXfDdrYs0pZ9uWh%2BlHKwh6pzSNw9Mw%3D%3D";
    private final String url = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_hospital_content, container, false);

        arrays = new ArrayList<>();

        ListView listView = view.findViewById(R.id.find_hospital_listview);
        adapter = new FindMainAdapter(arrays);

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
        AQuery aQuery = new AQuery(getActivity());
        HashMap<String, String> params = new HashMap<>();
        params.put("serviceKey", key);
        params.put("Q0", "서울특별시");
        params.put("Q1", "구로구");

        String fullURL = addParams(url, params);
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
                                if (startTag.equals("item")) {
                                    data  = new FindPOJO();
                                }
                                if (startTag.equals("dutyName")){
                                    data.setName(parser.nextText());
                                }
                                if (startTag.equals("dutyAddr")){
                                    data.setAddress(parser.nextText());
                                }
                                if (startTag.equals("dutyTel1")){
                                    data.setTel(parser.nextText());
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                endTag = parser.getName();
                                if (endTag.equals("item")){
                                    arrays.add(data);
                                }
                                break;
                        }
                        eventType = parser.next();
                    }

                    if (arrays.size() > 0) {
                        adapter.getArItem().addAll(arrays);
                        adapter.notifyDataSetChanged();
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
}