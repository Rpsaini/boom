package com.web.bloomex.trades;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.adapters.TradesAdapter;
import com.web.bloomex.pairdetailfragments.PairDetailView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TradesFrag extends Fragment {

    private View view;
    private PairDetailView pairDetailView;

     public ArrayList<JSONObject> allTradesOrder = new ArrayList<>();

    public TradesFrag() {
        // Required empty public constructor
    }


    public static TradesFrag newInstance(String param1, String param2) {
        TradesFrag fragment = new TradesFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trades, container, false);
        pairDetailView = (PairDetailView) getActivity();

        getAllOpenOrders();
        return view;



    }

    public void init(ArrayList<JSONObject> dataAr)
    {
        RecyclerView recyclerview_trades = view.findViewById(R.id.recyclerview_trades);
        RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);
        if (dataAr.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerview_trades.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recyclerview_trades.setVisibility(View.VISIBLE);
        }

        TradesAdapter mAdapter = new TradesAdapter(dataAr, pairDetailView);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview_trades.setLayoutManager(horizontalLayoutManagaer);
        recyclerview_trades.setItemAnimator(new DefaultItemAnimator());
        recyclerview_trades.setAdapter(mAdapter);
    }



    private void getAllOpenOrders() {
        try {

            final Map<String, String> m = new HashMap<>();
            m.put("pair_id", pairDetailView.pair_id);
            m.put("token", pairDetailView.savePreferences.reterivePreference(pairDetailView, DefaultConstants.token) + "");
            m.put("DeviceToken", pairDetailView.getDeviceToken() + "");
            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", pairDetailView.getXapiKey());
            obj.put("Rtoken", pairDetailView.getNewRToken() + "");



            new ServerHandler().sendToServer(pairDetailView, pairDetailView.getApiUrl() + "get-trades-order", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
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
                            JSONArray tradesAr = jsonObject.getJSONArray("trades");
                            for (int x = 0; x < tradesAr.length(); x++) {
                                allTradesOrder.add(tradesAr.getJSONObject(x));
                            }
                            init(allTradesOrder);

                        }
                        else
                            {
                            pairDetailView.alertDialogs.alertDialog(pairDetailView, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed)
                                {
                                    pairDetailView.unauthorizedAccess(jsonObject);
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




}