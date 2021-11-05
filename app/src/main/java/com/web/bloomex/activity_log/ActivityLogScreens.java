package com.web.bloomex.activity_log;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.BaseActivity;
import com.web.bloomex.R;
import com.web.bloomex.activity_log.adapter.ActivityLogsAdapter;

public class ActivityLogScreens extends BaseActivity {
    private ImageView backIC;
    private RecyclerView activityLogsRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_screen);
        initView();
        setOnClickListener();
        setAdapterData();
    }

    private void initView(){
        backIC=findViewById(R.id.backIC);
        activityLogsRV=findViewById(R.id.activity_logs_rv);
    }
    private void setAdapterData(){
        ActivityLogsAdapter mAdapter = new ActivityLogsAdapter(ActivityLogScreens.this);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(ActivityLogScreens.this, LinearLayoutManager.VERTICAL, false);
        activityLogsRV.setLayoutManager(horizontalLayoutManager);
        activityLogsRV.setItemAnimator(new DefaultItemAnimator());
        activityLogsRV.setAdapter(mAdapter);
    }
    private void setOnClickListener(){
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
