package com.web.bloomex.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.wallet.retrofitapi.api.ApiProduction;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.LoginActivity;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.VerifyOtp;
import com.web.bloomex.adapters.CurrencyPagerAdapter;
import com.web.bloomex.adapters.MarketAdapter;
import com.web.bloomex.apimodel.ApiModel;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Communication.BuildRequestParms;
import Communication.CallBackHandler;
import Communication.RetrofitCommunication;
import io.reactivex.Observable;
import retrofit2.Response;


public class BTCFragment extends Fragment {
    private View view;
    private MainActivity mainActivity;

    public static MarketAdapter btcAdapter;

    public BTCFragment() {
        // Required empty public constructor
    }


    public static BTCFragment newInstance(String param1, String param2) {
        BTCFragment fragment = new BTCFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_b_t_c, container, false);
        mainActivity = (MainActivity) getActivity();
        init();
        getDashboardData();

        return view;
    }

    private TextView txt_referrallink,total_earned,txt_total_referral,txt_signup_bonus,txt_ref_bonus,txt_total_signup,txt_uerid;
    private void init()
    {
        txt_referrallink= view.findViewById(R.id.txt_referrallink);
        total_earned=view.findViewById(R.id.total_earned);
        txt_total_referral=view.findViewById(R.id.txt_total_referral);
        txt_signup_bonus=view.findViewById(R.id.txt_signup_bonus);
        txt_ref_bonus= view.findViewById(R.id.txt_ref_bonus);
        txt_total_signup=view.findViewById(R.id.txt_total_signup);
        txt_uerid=        mainActivity.findViewById(R.id.txt_uerid);

    }


    private void getDashboardData() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("token", mainActivity.savePreferences.reterivePreference(mainActivity, DefaultConstants.token) + "");
            m.put("device_token", mainActivity.getDeviceToken() + "");
            m.put("platform", "android");
            m.put("version", mainActivity.getAppVersion());

            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", mainActivity.getXapiKey());
            obj.put("Content-Type", "application/form-data");

            System.out.println("Before====" + m + "===" + obj + "====" + mainActivity.getApiUrl() + "app-dashboard");


            BuildRequestParms buildRequestParms = new BuildRequestParms();
            RetrofitCommunication retrofitCommunication = new RetrofitCommunication();
            ApiModel apiParamsInterface = ApiProduction.getInstance(mainActivity, mainActivity.getApiUrl()).provideService(ApiModel.class);


            Observable<Response<Object>> observable = apiParamsInterface.getDashboardData(
                    buildRequestParms.getRequestBody(mainActivity.savePreferences.reterivePreference(mainActivity, DefaultConstants.token) + ""),
                    buildRequestParms.getRequestBody(mainActivity.getAppVersion()),
                    buildRequestParms.getRequestBody("android"),
                    buildRequestParms.getRequestBody(mainActivity.getDeviceToken()),
                    mainActivity.getXapiKey()
            );


            retrofitCommunication.sendToServer(observable, mainActivity, R.layout.progressbar, R.layout.progressbar, true, new CallBackHandler() {
                @Override
                public void getResponseBack(JSONObject obj, ArrayList<Object> arrayList) {

                    try {

                        System.out.println("data back-====" + obj);

                        if (obj.getString("status").equalsIgnoreCase("true")) {
                            txt_referrallink.setText(obj.getString("referral_link"));
                            total_earned.setText(obj.getString("total_earned")+" "+obj.getString("token_symbol")+" = "+obj.getString("usdt_worth")+"USDT");
                            txt_total_referral.setText(obj.getString("total_referral"));
                            txt_signup_bonus.setText("Sign Up Bonus - "+obj.getString("signup_bonus")+obj.getString("token_symbol"));
                            txt_signup_bonus.setText("Referral Bonus - "+obj.getString("referral_bonus")+obj.getString("token_symbol"));
                            txt_total_signup.setText(obj.getString("total_signups"));
                            txt_uerid.setText(obj.getString("user_id"));



                        } else {
                            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.app_name), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
