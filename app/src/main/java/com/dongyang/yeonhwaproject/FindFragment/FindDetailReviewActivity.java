package com.dongyang.yeonhwaproject.FindFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.Adapter.DetailReviewAdapter;
import com.dongyang.yeonhwaproject.Adapter.FindDetailTabPagerAdapter;
import com.dongyang.yeonhwaproject.POJO.ReviewPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

public class FindDetailReviewActivity extends Fragment{

    TextView pointAvg;
    ImageView star1, star2, star3, star4, star5;

    ListView listview;
    DetailReviewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_find_review, container, false);

        pointAvg = view.findViewById(R.id.detail_review_avg);

        star1 = view.findViewById(R.id.detail_review_star_img_1);
        star2 = view.findViewById(R.id.detail_review_star_img_2);
        star3 = view.findViewById(R.id.detail_review_star_img_3);
        star4 = view.findViewById(R.id.detail_review_star_img_4);
        star5 = view.findViewById(R.id.detail_review_star_img_5);

        listview = view.findViewById(R.id.detail_review_listview);

        createPOJO();

        return view;
    }

    private void createPOJO(){
        ArrayList<ReviewPOJO> list = new ArrayList<>();

        ReviewPOJO pojo = new ReviewPOJO();
        pojo.setName("종화");
        pojo.setDate("2018/06/13 16:32");
        pojo.setPoint("4");
        pojo.setMsg("괜찮았아요.");
        pojo.setImg("null");

        list.add(pojo);

        pojo = new ReviewPOJO();
        pojo.setName("수연");
        pojo.setDate("2018/06/12 08:32");
        pojo.setPoint("3");
        pojo.setMsg("맛있음.");
        pojo.setImg("null");

        list.add(pojo);

        pojo = new ReviewPOJO();
        pojo.setName("정훈");
        pojo.setDate("2018/06/11 11:02");
        pojo.setPoint("5");
        pojo.setMsg("굳");
        pojo.setImg("null");

        list.add(pojo);

        adapter = new DetailReviewAdapter(list, getContext());
        listview.setAdapter(adapter);
    }

}