package com.dongyang.yeonhwaproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dongyang.yeonhwaproject.FindFragment.FindHospitalActivity;
import com.dongyang.yeonhwaproject.FindFragment.FindPharmacyActivity;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindTabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public FindTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FindHospitalActivity();
            case 1:
                return new FindPharmacyActivity();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
