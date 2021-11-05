package com.web.bloomex.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.web.bloomex.fragments.BTCFragment;
import com.web.bloomex.fragments.CommonFragment;

public class CurrencyPagerAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;
    public CurrencyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;


     }
    @Override
    public Fragment getItem(int position) {

        BTCFragment tab1 = new BTCFragment();
        Bundle bundle=new Bundle();
        bundle.putString("pos",position+"");
        tab1.setArguments(bundle);
        return tab1;


    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}