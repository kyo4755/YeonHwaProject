package com.dongyang.yeonhwaproject.FindFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongyang.yeonhwaproject.R;

/**
 * Created by Kim Jong-Hwa on 2018-05-26.
 */

public class FindPharmacyActivity extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_pharmacy_content, container, false);

        return view;
    }
}
