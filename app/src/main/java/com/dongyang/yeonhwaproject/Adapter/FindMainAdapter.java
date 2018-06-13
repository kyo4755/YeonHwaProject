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
        ImageView img;
        ImageView star_img;
        TextView name;
        TextView review_count;
        TextView distance;
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
            holder.img = convertView.findViewById(R.id.prefab_find_image);
            holder.name = convertView.findViewById(R.id.prefab_name);
            holder.star_img = convertView.findViewById(R.id.prefab_star_image);
            holder.review_count = convertView.findViewById(R.id.prefab_review_count);
            holder.distance = convertView.findViewById(R.id.prefab_distance);
            holder.prefab  = convertView.findViewById(R.id.prefab);

            convertView.setTag(holder);
        }
        else {
            holder = (FindMainViewHolder) convertView.getTag();
        }

        final FindPOJO pojo = list.get(position);
        System.out.println("=====================" + pojo.getDistance());

        holder.prefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FindDetailActivity.class);

                intent.putExtra("prefab_name", pojo.getName());
                intent.putExtra("prefab_address", pojo.getAddress());
                intent.putExtra("prefab_tel", pojo.getTel());
                intent.putExtra("x_lat", pojo.getLat());
                intent.putExtra("y_lon", pojo.getLon());
                context.startActivity(intent);
            }
        });

        holder.name.setText(pojo.getName());
        holder.review_count.setText(pojo.getReview_count());

        float distanceFloat = Float.parseFloat(pojo.getDistance());
        String distanceStr;
        if(distanceFloat < 1)   distanceStr = String.valueOf((int)(distanceFloat * 1000)) + "m";
        else                    distanceStr = distanceFloat + "km";
        holder.distance.setText(distanceStr);

        Drawable star_img;
        if(pojo.getIs_review_in())
            star_img = context.getResources().getDrawable(R.drawable.star_color);
        else
            star_img = context.getResources().getDrawable(R.drawable.star_noncolor);
        holder.star_img.setImageDrawable(star_img);

        return convertView;
    }
}
