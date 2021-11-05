package com.web.bloomex.currency_preferences;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.BaseActivity;
import com.web.bloomex.R;
import com.web.bloomex.currency_preferences.adapter.CurrencyPreferenceAdapter;

public class CurrencyPreferencesScreen extends BaseActivity {

    private RecyclerView currencyPrefRV;
    private ImageView backIC;
    private TextView resetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_preferences_screen);

        initView();
        setAdapterData();
        setOnClickListener();
    }

    private void initView(){
        currencyPrefRV=findViewById(R.id.currency_pr_rv);
        backIC=findViewById(R.id.backIC);
        resetTV=findViewById(R.id.resetTV);
    }

    private void setAdapterData(){
        CurrencyPreferenceAdapter mAdapter = new CurrencyPreferenceAdapter(CurrencyPreferencesScreen.this);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(CurrencyPreferencesScreen.this, LinearLayoutManager.VERTICAL, false);
        currencyPrefRV.setLayoutManager(horizontalLayoutManager);
        currencyPrefRV.setItemAnimator(new DefaultItemAnimator());
        currencyPrefRV.setAdapter(mAdapter);
    }

    private void setOnClickListener(){
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
