package com.web.bloomex.pairdetailfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.dialogsnpickers.DialogCallBacks;
import com.app.preferences.SavePreferences;
import com.app.vollycommunicationlib.CallBack;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.OrderDetailsAdapter;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PairOpenOrderFragment extends Fragment {

private View view;
private PairDetailView mainActivity;

private  OrderDetailsAdapter mAdapter;
    public PairOpenOrderFragment()
    {
        // Required empty public constructor
    }


    public static PairOpenOrderFragment newInstance(String param1, String param2) {
        PairOpenOrderFragment fragment = new PairOpenOrderFragment();
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
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_open_order, container, false);

            mainActivity=(PairDetailView) getActivity();

         init();
        return view;
    }


    private void init()
    {
        RecyclerView recycler_view_market =view.findViewById(R.id.order_list_recycler);
        RelativeLayout relativeLayout =view.findViewById(R.id.rr_nodata_view);
        if(DefaultConstants.open_orders.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            recycler_view_market.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            recycler_view_market.setVisibility(View.VISIBLE);
        }
        AppCompatActivity appCompatActivity=null;
        if(getActivity() instanceof  MainActivity)
        {
            appCompatActivity=(MainActivity)getActivity();
        }
        else if(getActivity() instanceof PairDetailView)
        {
            appCompatActivity=(PairDetailView)getActivity();
        }
        mAdapter = new OrderDetailsAdapter(DefaultConstants.open_orders, appCompatActivity,"open",this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_view_market.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_market.setItemAnimator(new DefaultItemAnimator());
        recycler_view_market.setAdapter(mAdapter);

    }


    public void cancelOrder(int position)
    {
        new AlertDialogs().alertDialog(mainActivity, "Cancel Order", "Would you like to cancel this order?","Yes", "No", new DialogCallBacks() {
            @Override
            public void getDialogEvent(String buttonPressed)
            {
                if(buttonPressed.equalsIgnoreCase("Yes"))
                {
                    try {
                        JSONObject dataObj = DefaultConstants.open_orders.get(position);

                        Map<String, String> m = new HashMap<>();
                        m.put("order_id", dataObj.getString("order_id"));
                        m.put("pair_id", dataObj.getString("pair_id"));
                        m.put("token", new SavePreferences().reterivePreference(mainActivity, DefaultConstants.token) + "");
                        m.put("DeviceToken",mainActivity.getDeviceToken()+"");


                        Map<String, String> headerMap = new HashMap<>();
                        headerMap.put("X-API-KEY", UtilClass.xApiKey);
                        headerMap.put("Rtoken", mainActivity.getNewRToken() + "");



                       mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "cancel-order", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
                            @Override
                            public void getRespone(String dta, ArrayList<Object> respons) {
                                try {

                                    JSONObject obj = new JSONObject(dta);
                                    if (obj.getBoolean("status")) {
                                        if(obj.has("token"))
                                        {
                                            mainActivity.savePreferences.savePreferencesData(mainActivity,obj.getString("token"),DefaultConstants.token);
                                            mainActivity.savePreferences.savePreferencesData(mainActivity,obj.getString("r_token"),DefaultConstants.r_token);

                                        }
                                        mainActivity.sendBroadCastWhaenOrderPlaced(m.get("pair_id"));
                                        DefaultConstants.open_orders.remove(position);
                                        mAdapter.notifyDataSetChanged();

                                    } else {
                                        mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                            @Override
                                            public void getDialogEvent(String buttonPressed) {
                                                mainActivity.unauthorizedAccess(obj);
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}