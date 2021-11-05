package com.web.bloomex.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.web.bloomex.fragments.FundFragment;
import com.web.bloomex.fragments.TransactionHistoryFragment;


public class FundPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public FundPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position)
     {
        switch (position) {
            case 0:
                FundFragment tab1 = new FundFragment();
                return tab1;

            case 1:
                TransactionHistoryFragment tab2 = new TransactionHistoryFragment();
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