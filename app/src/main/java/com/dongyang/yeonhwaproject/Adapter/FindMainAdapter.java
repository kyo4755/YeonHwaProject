package com.dongyang.yeonhwaproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.DetailActivity.FindDetailActivity;
import com.dongyang.yeonhwaproject.POJO.FindPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindMainAdapter extends BaseAdapter {

    private ArrayList<FindPOJO> listViewItemList;
    private Activity activity;

    public FindMainAdapter(ArrayList<FindPOJO> data) {
        this.listViewItemList = data;
    }
    public ArrayList<FindPOJO> getArItem(){return listViewItemList;}

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.find_prefab, parent, false);
        }


        ConstraintLayout prefab  = (ConstraintLayout) convertView.findViewById(R.id.prefab);
        prefab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FindDetailActivity.class);
                FindPOJO listViewItem = listViewItemList.get(position);

                intent.putExtra("prefab_name", listViewItem.getName());
                intent.putExtra("prefab_address", listViewItem.getAddress());
                intent.putExtra("prefab_tel", listViewItem.getTel());
                intent.putExtra("x_lat", listViewItem.getLat());
                intent.putExtra("y_lon", listViewItem.getLon());
                context.startActivity(intent);

            }
        });


        TextView name = convertView.findViewById(R.id.prefab_name);

        FindPOJO listViewItem = listViewItemList.get(position);

        name.setText(listViewItem.getName());


/*        Context context = parent.getContext();
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
        holder.distance.setText(pojo.getDistance());

        Drawable star_img;
        if(pojo.getIs_review_in())
            star_img = context.getResources().getDrawable(R.drawable.star_color);
        else
            star_img = context.getResources().getDrawable(R.drawable.star_noncolor);
        holder.star_img.setImageDrawable(star_img);
        */
        return convertView;
    }

}
