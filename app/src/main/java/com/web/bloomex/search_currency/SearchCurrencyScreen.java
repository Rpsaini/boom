package com.web.bloomex.search_currency;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.fragments.HomeFragment;
import com.web.bloomex.search_currency.adapter.SearchCurrencyAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SearchCurrencyScreen extends BaseActivity {
    private ImageView backIC;
    private RecyclerView activityLogsRV;
    private EditText tvTitle;
    private  ArrayList<JSONObject> filteredAr=new ArrayList<>();
    private ArrayList<JSONObject> allDataAr=new ArrayList<>();
//

    SearchCurrencyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_currency_screen);
        initView();
        setAdapterData();
        setOnClickListener();
    }

    private void initView() {
        backIC = findViewById(R.id.backIC);
        activityLogsRV = findViewById(R.id.currency_search_rv);
        tvTitle = findViewById(R.id.tvTitle);

    }

    private void setAdapterData()
    {
        try
        {
        allDataAr = new ArrayList<>();
        if(getIntent().getStringExtra(DefaultConstants.callfrom).equalsIgnoreCase(DefaultConstants.home))
        {
            for (Map.Entry<String, JSONArray> m : HomeFragment.commonMap.entrySet())
            {
                JSONArray currencyAr = m.getValue();
                for (int x = 0; x < currencyAr.length(); x++)
                {
                    allDataAr.add(currencyAr.getJSONObject(x));
                    filteredAr.add(currencyAr.getJSONObject(x));
                }
            }
        }
        else if(getIntent().getStringExtra(DefaultConstants.callfrom).equalsIgnoreCase(DefaultConstants.fund))
        {


        }




            mAdapter = new SearchCurrencyAdapter(SearchCurrencyScreen.this, filteredAr);
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(SearchCurrencyScreen.this, LinearLayoutManager.VERTICAL, false);
            activityLogsRV.setLayoutManager(horizontalLayoutManager);
            activityLogsRV.setItemAnimator(new DefaultItemAnimator());
            activityLogsRV.setAdapter(mAdapter);
            searchCurrency();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void searchCurrency()
    {
        tvTitle.addTextChangedListener(new TextWatcher() {
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
                            if (dataObj.getString("symbol").toLowerCase().contains(s.toString().toLowerCase()) || dataObj.getString("name").toLowerCase().contains(s.toString().toLowerCase()))
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


    private void setOnClickListener() {
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
