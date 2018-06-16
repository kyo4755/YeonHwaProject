package com.dongyang.yeonhwaproject.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dongyang.yeonhwaproject.POJO.FindDrugsPOJO;
import com.dongyang.yeonhwaproject.R;

import java.util.ArrayList;

/**
 * Created by JongHwa on 2018-06-15.
 */

public class FindDetailAdapter extends BaseAdapter {
    private ArrayList<FindDrugsPOJO> list;

    public FindDetailAdapter(){
        list = new ArrayList<>();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        FindDrugsPOJO pojo = list.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.find_drugs_prefab, parent, false);
        }

        TextView explain = convertView.findViewById(R.id.find_drugs_prefab_explain);
        TextView msg = convertView.findViewById(R.id.find_drugs_prefab_msg);

        explain.setText(pojo.getExplain());
        if(pojo.getExplain().equals("운영 시간")){
            msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        msg.setText(pojo.getMsg());

        return convertView;
    }

    public void setList(ArrayList<FindDrugsPOJO> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
