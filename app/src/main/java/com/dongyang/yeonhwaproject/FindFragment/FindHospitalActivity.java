package com.dongyang.yeonhwaproject.FindFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dongyang.yeonhwaproject.Adapter.FindMainAdapter;
import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

/**
 * Created by Kim Jong-Hwa on 2018-05-26.
 */

public class FindHospitalActivity extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_hospital_content, container, false);

        ListView listView = view.findViewById(R.id.find_hospital_listview);
        FindMainAdapter adapter = new FindMainAdapter();

        adapter.temp_data(temp_data());
        listView.setAdapter(adapter);
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

    private ArrayList<FindPOJO> temp_data(){
        ArrayList<FindPOJO> list = new ArrayList<>();

        FindPOJO pojo = new FindPOJO();
        pojo.setName("참 좋은 병원");
        pojo.setReview_count("47");
        pojo.setDistance("570m");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("덜 좋은 병원");
        pojo.setReview_count("10");
        pojo.setDistance("1.2km");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("덜 나쁜 병원");
        pojo.setReview_count("");
        pojo.setDistance("832m");
        pojo.setIs_review_in(false);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("참 나쁜 병원");
        pojo.setReview_count("482");
        pojo.setDistance("3.2km");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("참 좋은 병원");
        pojo.setReview_count("47");
        pojo.setDistance("570m");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("덜 좋은 병원");
        pojo.setReview_count("10");
        pojo.setDistance("1.2km");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("덜 나쁜 병원");
        pojo.setReview_count("");
        pojo.setDistance("832m");
        pojo.setIs_review_in(false);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("참 나쁜 병원");
        pojo.setReview_count("482");
        pojo.setDistance("3.2km");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("참 좋은 병원");
        pojo.setReview_count("47");
        pojo.setDistance("570m");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("덜 좋은 병원");
        pojo.setReview_count("10");
        pojo.setDistance("1.2km");
        pojo.setIs_review_in(true);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("덜 나쁜 병원");
        pojo.setReview_count("");
        pojo.setDistance("832m");
        pojo.setIs_review_in(false);
        list.add(pojo);

        pojo = new FindPOJO();
        pojo.setName("참 나쁜 병원");
        pojo.setReview_count("482");
        pojo.setDistance("3.2km");
        pojo.setIs_review_in(true);
        list.add(pojo);

        return list;
    }
}
