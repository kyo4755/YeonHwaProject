package com.dongyang.yeonhwaproject.FindFragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.DetailReviewAdapter;
import com.dongyang.yeonhwaproject.Adapter.FindDetailTabPagerAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.POJO.ReviewPOJO;
import com.dongyang.yeonhwaproject.R;
import com.dongyang.yeonhwaproject.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class FindDetailReviewActivity extends Fragment{

    TextView pointAvg, review_count;
    ImageView star1, star2, star3, star4, star5;

    ListView listview;
    DetailReviewAdapter adapter;

    Drawable nonStar, star;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_find_review, container, false);

        nonStar = getContext().getResources().getDrawable(R.drawable.star_noncolor);
        star = getContext().getResources().getDrawable(R.drawable.star_color);

        pointAvg = view.findViewById(R.id.detail_review_avg);
        review_count = view.findViewById(R.id.detail_review_count);

        star1 = view.findViewById(R.id.detail_review_star_img_1);
        star2 = view.findViewById(R.id.detail_review_star_img_2);
        star3 = view.findViewById(R.id.detail_review_star_img_3);
        star4 = view.findViewById(R.id.detail_review_star_img_4);
        star5 = view.findViewById(R.id.detail_review_star_img_5);

        listview = view.findViewById(R.id.detail_review_listview);

        getPoint();
        createPOJO();

        return view;
    }

    private void getPoint(){
        AQuery aQuery = new AQuery(getContext());
        String register_url = GlobalInfo.SERVER_URL + "review/getPointCount";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("hpid", getActivity().getIntent().getStringExtra("hpid"));

        aQuery.ajax(register_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Toast.makeText(getContext(), "HPID 값이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        review_count.setText(jsonObject.get("count").toString());

                        float point = Float.parseFloat(jsonObject.get("point").toString());
                        String pointStr = String.format("%.2f", point);
                        pointAvg.setText(pointStr);

                        setStarImage(point);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPOJO(){
        AQuery aQuery = new AQuery(getContext());
        String register_url = GlobalInfo.SERVER_URL + "review/getList";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("hpid", getActivity().getIntent().getStringExtra("hpid"));

        aQuery.ajax(register_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Toast.makeText(getContext(), "HPID 값이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else if(result_code.equals("0010")){
                        Toast.makeText(getContext(), "해당 장소의 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<ReviewPOJO> list = new ArrayList<>();

                        JSONArray jsonArray = new JSONArray(jsonObject.get("review_list").toString());

                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject jObject = jsonArray.getJSONObject(i);

                            ReviewPOJO pojo = new ReviewPOJO();

                            SimpleDateFormat origin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date origin_date = origin.parse(jObject.get("date").toString());
                            String new_date = parse.format(origin_date);
                            pojo.setDate(new_date);

                            pojo.setName(jObject.get("name").toString());
                            pojo.setPoint(jObject.get("point").toString());
                            pojo.setMsg(jObject.get("msg").toString());
                            pojo.setImg(jObject.get("image").toString());

                            list.add(pojo);
                        }

                        adapter = new DetailReviewAdapter(list, getContext());
                        listview.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setStarImage(float point){
        if(point < 1.0f){
            star1.setImageDrawable(nonStar);
            star2.setImageDrawable(nonStar);
            star3.setImageDrawable(nonStar);
            star4.setImageDrawable(nonStar);
            star5.setImageDrawable(nonStar);
        }
        else if (point >= 1.0f && point < 2.0f) {
            star1.setImageDrawable(star);
            star2.setImageDrawable(nonStar);
            star3.setImageDrawable(nonStar);
            star4.setImageDrawable(nonStar);
            star5.setImageDrawable(nonStar);
        }
        else if (point >= 2.0f && point < 3.0f) {
            star1.setImageDrawable(star);
            star2.setImageDrawable(star);
            star3.setImageDrawable(nonStar);
            star4.setImageDrawable(nonStar);
            star5.setImageDrawable(nonStar);
        }
        else if (point >= 3.0f && point < 4.0f) {
            star1.setImageDrawable(star);
            star2.setImageDrawable(star);
            star3.setImageDrawable(star);
            star4.setImageDrawable(nonStar);
            star5.setImageDrawable(nonStar);
        }
        else if (point >= 4.0f && point < 5.0f) {
            star1.setImageDrawable(star);
            star2.setImageDrawable(star);
            star3.setImageDrawable(star);
            star4.setImageDrawable(star);
            star5.setImageDrawable(nonStar);
        }
        else if (point >= 5.0f) {
            star1.setImageDrawable(star);
            star2.setImageDrawable(star);
            star3.setImageDrawable(star);
            star4.setImageDrawable(star);
            star5.setImageDrawable(star);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPoint();
        createPOJO();
    }

}