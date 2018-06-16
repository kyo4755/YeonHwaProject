package com.dongyang.yeonhwaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.FindMainAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.DetailActivity.SettingDetailLocationActivity;
import com.dongyang.yeonhwaproject.GPS.GPSInfo;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by JongHwa on 2018-06-16.
 */

public class SearchActivity extends AppCompatActivity{

    ImageButton cancel_btn;
    EditText name;
    Spinner spinner;
    ListView listView;
    FindMainAdapter adapter;
    LottieAnimationView loadingAnim;

    int spinner_selected = 0, pageNo = 0;
    boolean isLastItemVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_hosphar_main);

        cancel_btn = findViewById(R.id.search_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        name = findViewById(R.id.search_edittext);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        if(name.length() != 0){
                            getList(v);
                        }
                }
                return true;
            }
        });

        spinner = findViewById(R.id.search_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView = findViewById(R.id.search_listview);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isLastItemVisible){
                    getList(view);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
        loadingAnim = findViewById(R.id.search_loading);
    }

    private void getList(final View view){
        loadingAnim.setVisibility(View.VISIBLE);
        loadingAnim.playAnimation();
        loadingAnim.loop(true);

        pageNo++;

        System.out.println(pageNo);
        AQuery aQuery = new AQuery(SearchActivity.this);
        String find_hospital_url = GlobalInfo.SERVER_URL + "find/search";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("page", String.valueOf(pageNo));
        params.put("name", name.getText().toString());
        params.put("hosphar", String.valueOf(spinner_selected));

        aQuery.ajax(find_hospital_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Snackbar.make(view, "기관명 전달 오류", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0002")){
                        Snackbar.make(view, "HOSPHAR 전달 오류", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0003")){
                        Snackbar.make(view, "페이지 전달 오류", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        ArrayList<FindPOJO> arrays = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(jsonObject.get("list").toString());

                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jObject = jsonArray.getJSONObject(i);
                            FindPOJO data = new FindPOJO();
                            data.setHpid(jObject.getString("hpid"));
                            data.setDistance(null);
                            data.setAddress(jObject.getString("dutyAddr"));
                            data.setName(jObject.getString("dutyName"));
                            data.setTel(jObject.getString("dutyTel1"));
                            data.setAvg_point(jObject.getString("avg_point"));
                            data.setReview_count(jObject.getString("review_count"));
                            if(spinner_selected == 0)   data.setIsHosPhar("hos");
                            else                        data.setIsHosPhar("phar");

                            arrays.add(data);
                        }

                        if(pageNo == 1){
                            adapter = new FindMainAdapter(arrays);
                            listView.setAdapter(adapter);
                        }else{
                            adapter.getArItem().addAll(arrays);
                            adapter.notifyDataSetChanged();
                        }

                        loadingAnim.setVisibility(View.GONE);
                        loadingAnim.pauseAnimation();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
