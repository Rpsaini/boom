package com.web.bloomex.allorders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.adapters.PriceVolumeAdapter;
import com.web.bloomex.pairdetailfragments.PairDetailView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AllOrdersFrag extends Fragment {

   private PairDetailView pairDetailView;
   private View view;
   public ArrayList<JSONObject> buyAr=new ArrayList<>();
   public ArrayList<JSONObject> sellAr=new ArrayList<>();
   private View standing_line;
   private LinearLayout ll_orderbook;
   private RelativeLayout rr_nodata_view;


    public AllOrdersFrag() {
        // Required empty public constructor
    }


    public static AllOrdersFrag newInstance(String param1, String param2) {
        AllOrdersFrag fragment = new AllOrdersFrag();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pairDetailView=(PairDetailView)getActivity();
        view= inflater.inflate(R.layout.fragment_all_orders2, container, false);
        standing_line=view.findViewById(R.id.standing_line);
        standing_line.setVisibility(View.GONE);
        ll_orderbook=view.findViewById(R.id.ll_orderbook);
        rr_nodata_view=view.findViewById(R.id.rr_nodata_view);
        getAllOpenOrders();
        return view;
    }


    public void init(ArrayList<JSONObject> buyDataAr,ArrayList<JSONObject> sellDataAr) {
        try
         {
             pairDetailView.topBuyPrice=buyDataAr;
             pairDetailView.topSellPrice=sellDataAr;

             if(buyDataAr.size()==0&&sellDataAr.size()==0)
             {
                 ll_orderbook.setVisibility(View.GONE);
                 rr_nodata_view.setVisibility(View.VISIBLE);
             }
             else
             {
                 ll_orderbook.setVisibility(View.VISIBLE);
                 rr_nodata_view.setVisibility(View.GONE);
             }



           if(sellDataAr.size() > 0 && buyDataAr.size() > 0)
            {
                standing_line.setVisibility(View.VISIBLE);
            }
            RecyclerView recyclerview_price_volume_buy = view.findViewById(R.id.recyclerview_price_volume_buy);
            RecyclerView recyclerview_price_volume_sell = view.findViewById(R.id.recyclerview_price_volume_sell);

            PriceVolumeAdapter priceVolumeAdapter = new PriceVolumeAdapter(getActivity(), buyDataAr, pairDetailView, "buy");
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerview_price_volume_buy.setLayoutManager(horizontalLayoutManagaer);
            recyclerview_price_volume_buy.setItemAnimator(new DefaultItemAnimator());
            recyclerview_price_volume_buy.setAdapter(priceVolumeAdapter);


            PriceVolumeAdapter priceVolumeAdaptersell = new PriceVolumeAdapter(getActivity(), sellDataAr, pairDetailView, "sell");
            LinearLayoutManager horizontalLayoutManagaersell
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerview_price_volume_sell.setLayoutManager(horizontalLayoutManagaersell);
            recyclerview_price_volume_sell.setItemAnimator(new DefaultItemAnimator());
            recyclerview_price_volume_sell.setAdapter(priceVolumeAdaptersell);
         }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getAllOpenOrders()
    {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("pair_id",pairDetailView.pair_id);
            m.put("token",pairDetailView.savePreferences.reterivePreference(pairDetailView, DefaultConstants.token)+"");
            m.put("DeviceToken",pairDetailView.getDeviceToken()+"");
            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", pairDetailView.getXapiKey());
            obj.put("Rtoken", pairDetailView.getNewRToken()+"");

             new ServerHandler().sendToServer(pairDetailView, pairDetailView.getApiUrl() + "get-order-book", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {

                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status"))
                        {

                            if(jsonObject.has("token"))
                            {
                                pairDetailView.savePreferences.savePreferencesData(pairDetailView,jsonObject.getString("token"),DefaultConstants.token);
                                pairDetailView.savePreferences.savePreferencesData(pairDetailView,jsonObject.getString("r_token"),DefaultConstants.r_token);

                            }

                            JSONArray buy=jsonObject.getJSONArray("buy");
                            for(int x=0;x<buy.length();x++)
                            {
                                buyAr.add(buy.getJSONObject(x));
                            }

                          //  double selltotalVolume=0;
                            JSONArray sell=jsonObject.getJSONArray("sell");
                            for(int x=0;x<sell.length();x++)
                            {
                                sellAr.add(sell.getJSONObject(x));

                            }

                            JSONObject balance =jsonObject.getJSONObject("balance");
                            pairDetailView.buyBalance=Double.parseDouble(balance.getString("buy_balance"));
                            pairDetailView.sellBalance =Double.parseDouble(balance.getString("sell_balance"));

                            init(buyAr,sellAr);

                        } else
                            {
                            pairDetailView.alertDialogs.alertDialog(pairDetailView, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed)
                            {
                                pairDetailView.unauthorizedAccess(jsonObject);
                            }
                            });
                            init(new ArrayList<>(),new ArrayList<>());
                            }
                       }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}