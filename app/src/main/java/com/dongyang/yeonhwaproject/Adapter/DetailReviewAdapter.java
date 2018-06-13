package com.dongyang.yeonhwaproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.MainActivity;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.POJO.ReviewPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class DetailReviewAdapter extends BaseAdapter {

    Drawable star, nonStar;

    private ArrayList<ReviewPOJO> list;

    private class DetailReviewHolder{
        ImageView img;
        TextView name;
        TextView date;
        TextView msg;

        ImageView star1;
        ImageView star2;
        ImageView star3;
        ImageView star4;
        ImageView star5;
    }

    public DetailReviewAdapter(ArrayList<ReviewPOJO> data, Context context) {
        nonStar = context.getResources().getDrawable(R.drawable.star_noncolor);
        star = context.getResources().getDrawable(R.drawable.star_color);
        this.list = data;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        DetailReviewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.detail_find_review_prefab, parent, false);

            holder = new DetailReviewHolder();
            holder.img = convertView.findViewById(R.id.detail_review_prefab_img);
            holder.name = convertView.findViewById(R.id.detail_review_prefab_name);
            holder.date = convertView.findViewById(R.id.detail_review_prefab_date);
            holder.msg = convertView.findViewById(R.id.detail_review_prefab_msg);

            holder.star1 = convertView.findViewById(R.id.detail_review_prefab_star_img_1);
            holder.star2 = convertView.findViewById(R.id.detail_review_prefab_star_img_2);
            holder.star3 = convertView.findViewById(R.id.detail_review_prefab_star_img_3);
            holder.star4 = convertView.findViewById(R.id.detail_review_prefab_star_img_4);
            holder.star5 = convertView.findViewById(R.id.detail_review_prefab_star_img_5);

            convertView.setTag(holder);
        }
        else {
            holder = (DetailReviewHolder) convertView.getTag();
        }

        ReviewPOJO data = list.get(position);

        if(data.getImg().equals("null")) {
            Glide.with(context)
                    .load(R.drawable.default_user)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder.img);
        } else {
            String url = "" + data.getImg();
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder.img);
        }

        holder.name.setText(data.getName());
        holder.date.setText(data.getDate());
        holder.msg.setText(data.getMsg());

        float avrPoint = Float.parseFloat(data.getPoint());

        if(avrPoint < 1.0f){
            holder.star1.setImageDrawable(nonStar);
            holder.star2.setImageDrawable(nonStar);
            holder.star3.setImageDrawable(nonStar);
            holder.star4.setImageDrawable(nonStar);
            holder.star5.setImageDrawable(nonStar);
        }
        else if (avrPoint >= 1.0f && avrPoint < 2.0f) {
            holder.star1.setImageDrawable(star);
            holder.star2.setImageDrawable(nonStar);
            holder.star3.setImageDrawable(nonStar);
            holder.star4.setImageDrawable(nonStar);
            holder.star5.setImageDrawable(nonStar);
        }
        else if (avrPoint >= 2.0f && avrPoint < 3.0f) {
            holder.star1.setImageDrawable(star);
            holder.star2.setImageDrawable(star);
            holder.star3.setImageDrawable(nonStar);
            holder.star4.setImageDrawable(nonStar);
            holder.star5.setImageDrawable(nonStar);
        }
        else if (avrPoint >= 3.0f && avrPoint < 4.0f) {
            holder.star1.setImageDrawable(star);
            holder.star2.setImageDrawable(star);
            holder.star3.setImageDrawable(star);
            holder.star4.setImageDrawable(nonStar);
            holder.star5.setImageDrawable(nonStar);
        }
        else if (avrPoint >= 4.0f && avrPoint < 5.0f) {
            holder.star1.setImageDrawable(star);
            holder.star2.setImageDrawable(star);
            holder.star3.setImageDrawable(star);
            holder.star4.setImageDrawable(star);
            holder.star5.setImageDrawable(nonStar);
        }
        else if (avrPoint >= 5.0f) {
            holder.star1.setImageDrawable(star);
            holder.star2.setImageDrawable(star);
            holder.star3.setImageDrawable(star);
            holder.star4.setImageDrawable(star);
            holder.star5.setImageDrawable(star);
        }

        return convertView;
    }
}
