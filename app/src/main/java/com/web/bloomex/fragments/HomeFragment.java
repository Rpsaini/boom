package com.web.bloomex.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.CurrencyPagerAdapter;
import com.web.bloomex.adapters.MarketAdapter;
import com.web.bloomex.communication.SocketHandlers;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.emitter.Emitter;

public class HomeFragment extends Fragment {
    private View view;
    private MainActivity mainActivity;


    public static Map<String, JSONArray> commonMap = new HashMap<>();
    public static ArrayList<JSONObject> tabsHeaderKeys = new ArrayList<>();
    public static Map<Integer, MarketAdapter> marketAdapterMap = new HashMap<>();
    int pagerselectedPos = 0;


    CurrencyPagerAdapter adapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();
        getMarketTickers();

        return view;
    }


    TabLayout tabLayout;
    ViewPager viewPager;

    private void initView() {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#13B351"));
        for (int x = 0; x < tabsHeaderKeys.size(); x++) {
            try {
                JSONObject datObj = tabsHeaderKeys.get(x);
                tabLayout.addTab(tabLayout.newTab().setText(datObj.getString("pair_name")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) view.findViewById(R.id.pager);


    }

    void tablayout() {
        if (tabLayout != null) {
            adapter = new CurrencyPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    pagerselectedPos = tab.getPosition();
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




    private void getMarketTickers() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("token", mainActivity.savePreferences.reterivePreference(mainActivity, DefaultConstants.token) + "");
            m.put("DeviceToken", mainActivity.getDeviceToken() + "");

             final Map<String, String> obj = new HashMap<>();
             obj.put("X-API-KEY", mainActivity.getXapiKey());
             obj.put("Rtoken", mainActivity.getNewRToken() + "");




             new ServerHandler().sendToServer(mainActivity, mainActivity.getApiUrl() + "get-home-data", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        JSONArray termArray=jsonObject.getJSONArray("term_array");
                        tabsHeaderKeys=new ArrayList<>();
                        for(int x=0;x<termArray.length();x++)
                        {
                            JSONObject headerObj=new JSONObject();
                            headerObj.put("pair_name",termArray.getJSONObject(x).getString("symbol").toLowerCase());
                            headerObj.put("pair_id",termArray.getJSONObject(x).getString("id"));
                            tabsHeaderKeys.add(headerObj);
                        }
                        initView();
                        if(jsonObject.getBoolean("status")) {
                            try {
                                mainActivity.savePreferences.savePreferencesData(mainActivity, jsonObject.getString("kyc_status"), DefaultConstants.kyc_status);

                                String appversion = jsonObject.getString("app_version");
                                if(jsonObject.has("token"))
                                 {
                                    mainActivity.savePreferences.savePreferencesData(mainActivity, jsonObject.getString("token"), DefaultConstants.token);
                                    mainActivity.savePreferences.savePreferencesData(mainActivity, jsonObject.getString("r_token"), DefaultConstants.r_token);
                                 }

                                for(int x=0;x<tabsHeaderKeys.size();x++)
                                 {
                                    JSONObject dataObj=tabsHeaderKeys.get(x);
                                     String pairName=dataObj.getString("pair_name");
                                     commonMap.put(pairName, jsonObject.getJSONArray(pairName));

                                 }
                                if(mainActivity.getAppVersion().equalsIgnoreCase(appversion))
                                  {
                                    mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.app_name), "Please update app to new version.", "Ok", "", new DialogCallBacks() {
                                        @Override
                                        public void getDialogEvent(String buttonPressed) {
                                            if(buttonPressed.equalsIgnoreCase("ok")) {
                                                mainActivity.launchPlayStore(mainActivity, mainActivity.getPackageName());
                                            }
                                        }
                                    });
                                 }
                                tablayout();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                    mainActivity.unauthorizedAccess(jsonObject);
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        socketHandlers.socket.off("broadcast_sent_client");
    }

    SocketHandlers socketHandlers;

    private void getDataOfPairs()
    {
        socketHandlers = new SocketHandlers();
        socketHandlers.createConnection();
        socketHandlers.socket.off("broadcast_sent_client");
        socketHandlers.socket.on("broadcast_sent_client", new Emitter.Listener() {
            @Override
            public void call(Object... args) {


                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                             {
                                JSONObject jsonObject = new JSONObject(args[0] + "");
                                if(jsonObject.has("price_stats_app"))
                                    {

                                    String pair_id = jsonObject.getString("pair_id");
                                    JSONObject price_stats = new JSONObject(jsonObject.getString("price_stats_app"));

                                    final String change = price_stats.getString("change");
                                    final String lastprice = price_stats.getString("lastprice");
                                    final String volume = price_stats.getString("volume");

                                    if(commonMap.size()>0)
                                      {
                                          JSONObject data=tabsHeaderKeys.get(pagerselectedPos);
                                         JSONArray dataAr = commonMap.get(data.getString("pair_name"));
                                         System.out.println("Data Ar=="+dataAr);
                                         for(int x = 0; x < dataAr.length(); x++)
                                            {
                                             JSONObject dataObj = dataAr.getJSONObject(x);
                                             if(dataObj.getString("pair_id").equalsIgnoreCase(pair_id))
                                               {
                                                   dataObj.remove("price");
                                                   dataObj.remove("volume");
                                                   dataObj.remove("change");

                                                   dataObj.put("price", lastprice);
                                                   dataObj.put("volume", volume);
                                                   dataObj.put("change", change);

                                                   dataAr.put(x, dataObj);
                                               }
                                            }
                                         commonMap.put(data.getString("pair_name"), dataAr);
                                         marketAdapterMap.get(pagerselectedPos).notifyDataSetChanged();

                                     }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataOfPairs();
    }


}