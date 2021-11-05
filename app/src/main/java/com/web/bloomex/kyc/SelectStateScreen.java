package com.web.bloomex.kyc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.dialogsnpickers.DialogCallBacks;
import com.app.preferences.SavePreferences;
import com.app.validation.ValidationRule;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.kyc.adapter.SelectStateAdapter;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import animationpackage.AnimationForViews;

public class SelectStateScreen extends BaseActivity {
    private static String TAG=SelectCountryScreen.class.getSimpleName();
    private ImageView backIC;
    private RecyclerView countryRV;
    private SelectStateScreen mSelectCountryScreen;
    private RelativeLayout relativeLayout;
    private SelectStateAdapter mAdapter;
    private ArrayList<JSONObject> filteredAr=new ArrayList<>();
    private ArrayList<JSONObject> allDataAr=new ArrayList<>();
    private EditText etSearchCountry;
    private String countryID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_country_screen);
        mSelectCountryScreen=this;
        initiateObj();
        initView();
        setOnClickListener();
        if(countryID!=null)
        getStateByCountryID(countryID);
    }

    public void initiateObj()
    {
        savePreferences = new SavePreferences();
        serverHandler = new ServerHandler();
        alertDialogs = new AlertDialogs();
        validationRule = new ValidationRule();
        animationForViews = new AnimationForViews();
        changestatusBarColor(0);
    }
    private void initView(){
        backIC=findViewById(R.id.backIC);
        countryRV=findViewById(R.id.country_rv);
        etSearchCountry=findViewById(R.id.etSearchCountry);
        relativeLayout =findViewById(R.id.rr_nodata_view);

        Intent intent =getIntent();

        if(intent.hasExtra("_id")){
            countryID=intent.getExtras().getString("_id");
        }
    }

    private void setOnClickListener(){
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getStateByCountryID(String country_id)
    {

        DefaultConstants.open_orders.clear();
        DefaultConstants.orders_history.clear();

        Map<String, String> m=new HashMap<>();
        m.put("country_id",country_id);

        m.put("token",savePreferences.reterivePreference(mSelectCountryScreen, DefaultConstants.token)+"");
        m.put("DeviceToken",getDeviceToken()+"");



        Map<String,String> headerMap=new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", getNewRToken()+"");



        serverHandler.sendToServer(mSelectCountryScreen, getApiUrl()+"get-state-by-country", m, 0,headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    Log.e(TAG,"Response Data:"+dta);

                    //  allDataAr=dta;
                    JSONObject obj = new JSONObject(dta);

                    if (obj.getBoolean("status")) {
                        JSONArray openAr = obj.getJSONArray("states");
                        for(int x=0;x<openAr.length();x++)
                        {
                            allDataAr.add(openAr.getJSONObject(x));
                            filteredAr.add(openAr.getJSONObject(x));
                        }
                    }
                    else {
                        alertDialogs.alertDialog(mSelectCountryScreen, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed)
                            {
                                unauthorizedAccess(obj);
                            }
                        });

                    }

                   /*

          {"status":true,"states":[{"id":"1","name":"Andaman and Nicobar Islands"}
               */

                    setAdapterData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void setAdapterData()
    {
        try
        {

            mAdapter = new SelectStateAdapter(SelectStateScreen.this, filteredAr);
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(SelectStateScreen.this, LinearLayoutManager.VERTICAL, false);
            countryRV.setLayoutManager(horizontalLayoutManager);
            countryRV.setItemAnimator(new DefaultItemAnimator());
            countryRV.setAdapter(mAdapter);
            searchCurrency();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void searchCurrency()
    {
        etSearchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try
                {
                    if (s.length() > 0)
                    {
                        filteredAr.clear();
                        for (int x = 0; x < allDataAr.size(); x++)
                        {
                            JSONObject dataObj = allDataAr.get(x);
                            if (dataObj.getString("name").toLowerCase().contains(s.toString().toLowerCase()))
                            {
                                filteredAr.add(dataObj);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        filteredAr.clear();
                        for(int x=0;x<allDataAr.size();x++)
                        {
                            filteredAr.add(allDataAr.get(x));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}

