package com.dongyang.yeonhwaproject.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindMainAdapter extends BaseAdapter {

    private Location currentLocation;
    //private ArrayList<FindPOJO> listViewItemList;
    private ArrayList<FindPOJO> list;

    private class FindMainViewHolder{
        ImageView img;
        ImageView star_img;
        TextView name;
        TextView review_count;
        TextView distance;
    }

    public FindMainAdapter(ArrayList<FindPOJO> data) {
        this.list = data;
        currentLocation = new Location("currentLocation");
        currentLocation.setLatitude(GlobalInfo.settingLatitude);
        currentLocation.setLongitude(GlobalInfo.settingLongitude);
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
        Context context = parent.getContext();
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

            convertView.setTag(holder);
        }
        else {
            holder = (FindMainViewHolder) convertView.getTag();
        }

        FindPOJO pojo = list.get(position);

        holder.name.setText(pojo.getName());
        holder.review_count.setText(pojo.getReview_count());

        holder.distance.setText(pojo.getDistance() + "km");

        Drawable star_img;
        if(pojo.getIs_review_in())
            star_img = context.getResources().getDrawable(R.drawable.star_color);
        else
            star_img = context.getResources().getDrawable(R.drawable.star_noncolor);
        holder.star_img.setImageDrawable(star_img);

        return convertView;
    }
}
