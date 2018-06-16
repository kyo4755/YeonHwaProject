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

import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindMainAdapter extends BaseAdapter {

    private ArrayList<FindPOJO> list;

    private class FindMainViewHolder{
        ImageView star_img;
        TextView star_result;
        TextView name;
        TextView review_count;
        TextView distance;
        TextView address;
        ConstraintLayout prefab;
    }

    public FindMainAdapter(ArrayList<FindPOJO> data) {
        this.list = data;
    }
    public ArrayList<FindPOJO> getArItem(){return list;}

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
        FindMainViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.find_prefab, parent, false);

            holder = new FindMainViewHolder();
            holder.name = convertView.findViewById(R.id.prefab_name);
            holder.star_result = convertView.findViewById(R.id.prefab_star_result);
            holder.star_img = convertView.findViewById(R.id.prefab_star_image);
            holder.review_count = convertView.findViewById(R.id.prefab_review_count);
            holder.distance = convertView.findViewById(R.id.prefab_distance);
            holder.prefab  = convertView.findViewById(R.id.prefab);
            holder.address = convertView.findViewById(R.id.prefab_address);

            convertView.setTag(holder);
        }
        else {
            holder = (FindMainViewHolder) convertView.getTag();
        }

        final FindPOJO pojo = list.get(position);

        holder.prefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FindDetailActivity.class);
                intent.putExtra("hpid", pojo.getHpid());
                intent.putExtra("prefab_name", pojo.getName());
                intent.putExtra("ishosphar", pojo.getIsHosPhar());
                intent.putExtra("prefab_tel", pojo.getTel());
                context.startActivity(intent);
            }
        });

        holder.name.setText(pojo.getName());
        holder.review_count.setText(pojo.getReview_count());
        holder.address.setText(pojo.getAddress());

        if(pojo.getDistance() != null){
            float distanceFloat = Float.parseFloat(pojo.getDistance());
            String distanceStr;
            if(distanceFloat < 1)   distanceStr = String.valueOf((int)(distanceFloat * 1000)) + "m";
            else                    distanceStr = distanceFloat + "km";
            holder.distance.setText(distanceStr);
        } else {
            holder.distance.setVisibility(View.GONE);
        }

        Drawable star_img;

        if(Float.parseFloat(pojo.getAvg_point()) != 0.0f){
            star_img = context.getResources().getDrawable(R.drawable.star_color);
        }else{
            star_img = context.getResources().getDrawable(R.drawable.star_noncolor);
        }

        holder.star_img.setImageDrawable(star_img);
        holder.star_result.setText(pojo.getAvg_point());

        return convertView;
    }
}
