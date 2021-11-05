package com.web.bloomex.orderpackage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.OrderViewAdapter;
import com.web.bloomex.utilpackage.UtilClass;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrderFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String filteredCurrency="";

    public MyOrderFragment()
    {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_my_order, container, false);
        mainActivity=(MainActivity)getActivity();
        filteredCurrency= getArguments().getString(DefaultConstants.CurrencyName);
        tablayout();
        getAllOrders();
        return view;
    }


    void tablayout()
    {
        tabLayout = (TabLayout)view. findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("OPEN"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORY"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
         viewPager = (ViewPager) view.findViewById(R.id.pager);

        mainActivity.findViewById(R.id.img_search).setVisibility(View.INVISIBLE);
    }


    private void getAllOrders()
    {

        DefaultConstants.open_orders.clear();
        DefaultConstants.orders_history.clear();

        Map<String, String> m=new HashMap<>();
        m.put("side", "");
        m.put("type", "");
        m.put("pair_id", "");
        m.put("token",mainActivity.savePreferences.reterivePreference(mainActivity, DefaultConstants.token)+"");
        m.put("DeviceToken",mainActivity.getDeviceToken()+"");



        Map<String,String> headerMap=new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", mainActivity.getNewRToken()+"");



        mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl()+"get-all-orders", m, 0,headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        try
                        {

                           JSONArray openAr=  obj.getJSONArray("open_orders");
                            JSONArray orderHistory=obj.getJSONArray("orders_history");
                            for(int x=0;x<openAr.length();x++)
                            {
                                DefaultConstants.open_orders.add(openAr.getJSONObject(x));
                            }
                            for(int x=0;x<orderHistory.length();x++)
                            {
                                DefaultConstants.orders_history.add(orderHistory.getJSONObject(x));
                            }


                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    } else {mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed)
                        {
                            mainActivity.unauthorizedAccess(obj);
                        }
                    });

                    }

                    callHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void callHistory()
    {
        OrderViewAdapter adapter = new OrderViewAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }





}