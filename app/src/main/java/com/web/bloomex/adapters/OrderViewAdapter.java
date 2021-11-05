package com.web.bloomex.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.web.bloomex.orderpackage.HistoryFragment;
import com.web.bloomex.orderpackage.OpenOrderFragment;

public class OrderViewAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public OrderViewAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
     }
    @Override
    public Fragment getItem(int position) {
        switch (position)
          {
            case 0:
                OpenOrderFragment tab1 = new OpenOrderFragment();
                return tab1;
            case 1:
                HistoryFragment tab2 = new HistoryFragment();
                return tab2;

            default:
                return null;
          }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}