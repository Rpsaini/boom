package com.web.bloomex;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.web.bloomex.fragments.BTCFragment;
import com.web.bloomex.fragments.HomeFragment;
import com.web.bloomex.fragments.MainFundFragment;
import com.web.bloomex.fragments.QuickBuyFragment;
import com.web.bloomex.orderpackage.MyOrderFragment;
import com.web.bloomex.search_currency.SearchCurrencyScreen;
import com.web.bloomex.setting_profile.SettingProfileScreen;
import com.web.bloomex.utilpackage.UtilClass;

public class MainActivity extends BaseActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateObj();
        getSupportActionBar().hide();
        MarketFragment();
        bottomNavigation();
        init();
        savePreferences.savePreferencesData(MainActivity.this,"true", UtilClass.isLogin);
      }

    private void init()
    {
        findViewById(R.id.img_somoreoptions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // showMoreOptions(v);
            }
        });



        findViewById(R.id.img_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this, SearchCurrencyScreen.class);
                intent.putExtra(DefaultConstants.callfrom,DefaultConstants.home);
                startActivity(intent);
            }
        });


        findViewById(R.id.profileIC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this, SettingProfileScreen.class);
                startActivity(intent);
            }
        });

    }


    private void MarketFragment()
    {

        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        homeFragment.setArguments(args);
        replaceMainFragment(homeFragment,"home");
    }

    private void orderFragment()
    {
        MyOrderFragment myOrderFrg = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putString(DefaultConstants.CurrencyName,"");
        myOrderFrg.setArguments(args);
        replaceMainFragment(myOrderFrg,"order");
    }

    private void fundFragment()
    {
        MainFundFragment fundFragment = new MainFundFragment();
        Bundle args = new Bundle();
        fundFragment.setArguments(args);
        replaceMainFragment(fundFragment,"fundfrg");
    }

    private void loadQuickBuyFragment()
    {
        QuickBuyFragment fundFragment = new QuickBuyFragment();
        Bundle args = new Bundle();
        fundFragment.setArguments(args);
        replaceMainFragment(fundFragment,"quick");
    }


    private void replaceMainFragment(Fragment upcoming, String tag)
    {
        upcoming = new BTCFragment();
        FragmentTransaction ft_main = getSupportFragmentManager().beginTransaction();
        ft_main.replace(R.id.fragment_container, upcoming);
        //ft_main.addToBackStack(tag);
        ft_main.commit();


//  todo      if(tag.equalsIgnoreCase("home"))
//        {
//            findViewById(R.id.img_search).setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            findViewById(R.id.img_search).setVisibility(View.INVISIBLE);
//        }
    }


    private void bottomNavigation()
    {
       final TextView txt_market =findViewById(R.id.txt_market);
       final TextView txt_order =findViewById(R.id.text_order);
       final TextView txt_fund =findViewById(R.id.txt_fund);
       final TextView txt_quicknuy =findViewById(R.id.txt_quicknuy);
       if(txt_market!=null)
        {
           txt_market.setTextColor(getResources().getColor(R.color.black));
           txt_order.setTextColor(getResources().getColor(R.color.grey_dark));
           txt_fund.setTextColor(getResources().getColor(R.color.grey_dark));
            txt_quicknuy.setTextColor(getResources().getColor(R.color.grey_dark));

           txt_market.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   txt_market.setTextColor(getResources().getColor(R.color.black));
                   txt_order.setTextColor(getResources().getColor(R.color.grey_dark));
                   txt_fund.setTextColor(getResources().getColor(R.color.grey_dark));
                   txt_quicknuy.setTextColor(getResources().getColor(R.color.grey_dark));
                   MarketFragment();
               }
           });

           txt_order.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   txt_market.setTextColor(getResources().getColor(R.color.grey_dark));
                   txt_order.setTextColor(getResources().getColor(R.color.black));
                   txt_fund.setTextColor(getResources().getColor(R.color.grey_dark));
                   txt_quicknuy.setTextColor(getResources().getColor(R.color.grey_dark));
                   orderFragment();
               }
           });

           txt_fund.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   txt_market.setTextColor(getResources().getColor(R.color.grey_dark));
                   txt_order.setTextColor(getResources().getColor(R.color.grey_dark));
                   txt_fund.setTextColor(getResources().getColor(R.color.black));
                   txt_quicknuy.setTextColor(getResources().getColor(R.color.grey_dark));
                   fundFragment();
               }
           });

            txt_quicknuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_market.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_order.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_fund.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_quicknuy.setTextColor(getResources().getColor(R.color.black));
                    loadQuickBuyFragment();
                }
            });

       }

    }



    int exitCount=1;
    @Override
    public void onBackPressed() {
        if(exitCount>=2)
        {
            finishAffinity();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Tap again to exit "+getResources().getString(R.string.app_name)+" app",Toast.LENGTH_LONG).show();
            exitCount++;



            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitCount=1;
                }
            }, 3000);


        }

    }




}