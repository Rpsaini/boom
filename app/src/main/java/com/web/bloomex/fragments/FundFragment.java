package com.web.bloomex.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.FiatCurrenciesAdapter;
import com.web.bloomex.adapters.FundAdapter;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FundFragment extends Fragment {
private View view;
private MainActivity mainActivity;
private TextView txt_totalbalance;
private int pagecount=1;
private Switch txt_switch;
private TextView txt_total_fund_value;

private ArrayList<JSONObject> fiatCurrencyAr=new ArrayList<>();
private ArrayList<JSONObject> cryptoeAr=new ArrayList<>();

   public FundFragment()
      {
      }

    public static FundFragment newInstance(String param1, String param2)
    {
        FundFragment fragment = new FundFragment();
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
        view=inflater.inflate(R.layout.fragment_fund, container, false);
        mainActivity=(MainActivity)getActivity();
        init();
        calCulateOrder();
        return view;
    }

    private void init()
    {
        txt_totalbalance=view.findViewById(R.id.txt_totalbalance);
        txt_switch=view.findViewById(R.id.txt_switch);
        txt_total_fund_value=view.findViewById(R.id.txt_total_fund_value);
        final TextView txt_hide_show_balance=view.findViewById(R.id.txt_hide_show_balance);

        txt_switch.setOnClickListener(new View.OnClickListener() {    @Override
            public void onClick(View v)
            {
                try {
                Switch s=(Switch)v;

                if(s.isChecked())
                {
                    txt_hide_show_balance.setText("Show Zero Balance");
                    ArrayList<JSONObject> filterFiat=new ArrayList<>();
                    for(int x=0;x<fiatCurrencyAr.size();x++)
                    {
                        JSONObject jsonObject=fiatCurrencyAr.get(x);
                        double balance=Double.parseDouble(jsonObject.getString("available_balance"));
                        if(balance>0)
                        {
                            filterFiat.add(jsonObject);
                        }

                    }

                    //=================

                    ArrayList<JSONObject> cryptoAr=new ArrayList<>();
                    for(int x=0;x<cryptoeAr.size();x++)
                    {
                        JSONObject jsonObject=cryptoeAr.get(x);
                        double balance=Double.parseDouble(jsonObject.getString("available_balance"));
                        if(balance>0)
                        {
                            cryptoAr.add(jsonObject);
                        }

                    }


                    init(cryptoAr);
                    fiatCurrencies(filterFiat);

                }
                else
                {
                    txt_hide_show_balance.setText("Hide Zero Balance");
                    init(cryptoeAr);
                    fiatCurrencies(fiatCurrencyAr);
                }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            });




    }

    private void fiatCurrencies(ArrayList<JSONObject> fiatAr)
    {
        RecyclerView fiat_balance_recyclerview=view.findViewById(R.id.fiat_balance_recyclerview);
        FiatCurrenciesAdapter mAdapter = new FiatCurrenciesAdapter(fiatAr,(MainActivity) getActivity(),this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fiat_balance_recyclerview.setLayoutManager(horizontalLayoutManagaer);
        fiat_balance_recyclerview.setItemAnimator(new DefaultItemAnimator());
        fiat_balance_recyclerview.setAdapter(mAdapter);

    }

    private void init(ArrayList<JSONObject> dataAr)
    {
        RelativeLayout relativeLayout =view.findViewById(R.id.rr_nodata_view);
        RecyclerView balance_recyclerview =view.findViewById(R.id.balance_recyclerview);
        if(dataAr.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            balance_recyclerview.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            balance_recyclerview.setVisibility(View.VISIBLE);
        }
        FundAdapter mAdapter = new FundAdapter(dataAr,(MainActivity) getActivity(),this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        balance_recyclerview.setLayoutManager(horizontalLayoutManagaer);
        balance_recyclerview.setItemAnimator(new DefaultItemAnimator());
        balance_recyclerview.setAdapter(mAdapter);
    }

    private void calCulateOrder()
    {

        Map<String, String> m=new HashMap<>();
        m.put("currency", "BTC");
        m.put("page", pagecount+"");
        m.put("token",mainActivity.savePreferences.reterivePreference(mainActivity, DefaultConstants.token)+"");
        m.put("DeviceToken",mainActivity.getDeviceToken()+"");

        Map<String,String> headerMap=new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", mainActivity.getNewRToken()+"");


        new ServerHandler().sendToServer(mainActivity, mainActivity.getApiUrl()+"get-balances-call", m, 0,headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    Log.d("Fait",obj+"");
                    if (obj.getBoolean("status")) {
                        try
                        {
                            if(obj.has("token"))
                            {
                                mainActivity.savePreferences.savePreferencesData(mainActivity,obj.getString("token"),DefaultConstants.token);
                                mainActivity.savePreferences.savePreferencesData(mainActivity,obj.getString("r_token"),DefaultConstants.r_token);
                            }

                            txt_totalbalance.setText((obj.getString("sum_available_bal")));
                            txt_total_fund_value.setText(obj.getString("sum_available_bal")+"USDT");
                            JSONArray balances=obj.getJSONArray("balances");

                            for(int x=0;x<balances.length();x++)
                            {
                                String type=balances.getJSONObject(x).getString("type");
                                if(type.equalsIgnoreCase("fiat"))
                                {
                                    fiatCurrencyAr.add(balances.getJSONObject(x));
                                }
                                else
                                {
                                    cryptoeAr.add(balances.getJSONObject(x));

                                }
                            }

                             init(cryptoeAr);
                             fiatCurrencies(fiatCurrencyAr);

                          }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    } else {mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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





//    private void showImage(final String url, final ImageView header_img) {
//
//        Glide.with(mainActivity)
//                .load(url)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
//                .into(header_img);
//    }


}