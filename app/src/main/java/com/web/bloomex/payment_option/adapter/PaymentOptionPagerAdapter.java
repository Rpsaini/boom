package com.web.bloomex.payment_option.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.web.bloomex.R;
import com.web.bloomex.payment_option.fragment.BankAccountDetailsFragment;
import com.web.bloomex.payment_option.fragment.UPIPaymentOptionFragment;

public class PaymentOptionPagerAdapter extends FragmentPagerAdapter {
   Context context;
    public PaymentOptionPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new BankAccountDetailsFragment();
        }
        else if (position == 1)
        {
            fragment = new UPIPaymentOptionFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = context.getString(R.string.bank_accoun);
        }
        else if (position == 1)
        {
            title = context.getString(R.string.upi);
        }

        return title;
    }
}
