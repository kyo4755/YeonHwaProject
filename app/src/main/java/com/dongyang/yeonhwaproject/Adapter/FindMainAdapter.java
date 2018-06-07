package com.dongyang.yeonhwaproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindMainAdapter extends BaseAdapter {

    //private ArrayList<FindPOJO> listViewItemList;
    private ArrayList<FindPOJO> list;

    private class FindMainViewHolder{
        ImageView img;
        ImageView star_img;
        TextView name;
        TextView review_count;
        TextView distance;
        ConstraintLayout prefab
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
//        final int pos = position;
//        final Context context = parent.getContext();
//
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.find_prefab, parent, false);
//        }
//        TextView name = convertView.findViewById(R.id.prefab_name);
//
//        FindPOJO listViewItem = listViewItemList.get(position);
//
//        name.setText(listViewItem.getName());
        final Context context = parent.getContext();
        final int pos = position;
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


        holder.prefab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FindDetailActivity.class);
                FindPOJO listViewItem = list.get(pos);

                intent.putExtra("prefab_name", listViewItem.getName());
                intent.putExtra("prefab_address", listViewItem.getAddress());
                intent.putExtra("prefab_tel", listViewItem.getTel());
                intent.putExtra("x_lat", listViewItem.getLat());
                intent.putExtra("y_lon", listViewItem.getLon());
                context.startActivity(intent);

            }
        });

        FindPOJO pojo = list.get(position);

        holder.name.setText(pojo.getName());
        holder.review_count.setText(pojo.getReview_count());

        Drawable star_img;
        if(pojo.getIs_review_in())
            star_img = context.getResources().getDrawable(R.drawable.star_color);
        else
            star_img = context.getResources().getDrawable(R.drawable.star_noncolor);
        holder.star_img.setImageDrawable(star_img);

        return convertView;
    }
}
