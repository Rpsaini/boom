package com.web.bloomex.kyc;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.R;
import com.web.bloomex.kyc.adapter.SelectCategorySubCategoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class VerifyCompleteSubmitKycScreen extends BaseActivity {
    public static VerifyCompleteSubmitKycScreen mVerifyCompleteSubmitKycScreen;
    private ImageView backIc=null;
    private TextView completeKycBt,selectCountryTV,select_typeTV;
    RecyclerView select_category_recycle;
    private String countryID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_complete_kyc_screen);
        mVerifyCompleteSubmitKycScreen=this;
        initiateObj();
        initView();
        setOnClickListener();
    }

    private void initView(){
        backIc=findViewById(R.id.backIC);
        completeKycBt=findViewById(R.id.complet_kycBT);
        selectCountryTV=findViewById(R.id.select_countryTV);
        select_typeTV=findViewById(R.id.select_typeTV);
    }
    private void setOnClickListener(){
        backIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        completeKycBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectCountryTV.getText().toString().equals(getString(R.string.select_coun))){
//                    Intent intent=new Intent(VerifyCompleteSubmitKycScreen.this,VerifyKycForPersonalInfoScreen.class);
//                    intent.putExtra("country_name",selectCountryTV.getText().toString());
//                    intent.putExtra("kyc_type",select_typeTV.getText().toString());
//                    intent.putExtra("_id",countryID);
//                    startActivity(intent);
                }
                else {
                    alertDialogs.alertDialog(VerifyCompleteSubmitKycScreen.this, getResources().getString(R.string.Required), getString(R.string.country_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed)
                        {

                        }
                    });
                }

            }
        });
        selectCountryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(VerifyCompleteSubmitKycScreen.this,SelectCountryScreen.class);
                startActivityForResult(intent,101);
            }
        });
        select_typeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case 101://selectCountry
                    if (data != null) {
                        String countryName=  data.getExtras().getString("_name");
                        countryID=data.getExtras().getString("_id");
                        selectCountryTV.setText(countryName);
                    }
                  break;
            }
        }
    }
    private void selectTypeDialog() {
        try {

            hideKeyboard(this);
            SimpleDialog simpleDialog = new SimpleDialog();
            final Dialog selectCategoryDialog = simpleDialog.simpleDailog(VerifyCompleteSubmitKycScreen.this, R.layout.select_category_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            select_category_recycle = selectCategoryDialog.findViewById(R.id.select_category_recycler);
            ImageView img_hideview = selectCategoryDialog.findViewById(R.id.img_hideview);
            final RelativeLayout ll_relativelayout = selectCategoryDialog.findViewById(R.id.ll_relativelayout);
            final TextView select_title = selectCategoryDialog.findViewById(R.id.select_title);
            final TextView select_sub_title = selectCategoryDialog.findViewById(R.id.select_sub_title);
            final TextView tv_done = selectCategoryDialog.findViewById(R.id.tv_done);
            animateUp(ll_relativelayout);
            select_title.setText(getResources().getString(R.string.type));
            select_sub_title.setText("");
            JSONArray buisnessTypeAr = new JSONArray();
            JSONObject type1 = new JSONObject();
            type1.put("name", "personal");

            JSONObject type2 = new JSONObject();
            type2.put("name", "company");

            buisnessTypeAr.put(type1);
            buisnessTypeAr.put(type2);


            initHomeCategory(buisnessTypeAr);



            img_hideview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);
                }
            });

            tv_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setKycType(String s){
        if(!s.isEmpty()) {
            select_typeTV.setText(s);
        }
        else {
            select_typeTV.setText(getString(R.string.personal));

        }

    }
    private void initHomeCategory(JSONArray dataAr) {
        select_category_recycle.setNestedScrollingEnabled(false);
        select_category_recycle.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        select_category_recycle.setHasFixedSize(true);
        select_category_recycle.setItemAnimator(new DefaultItemAnimator());
        SelectCategorySubCategoryAdapter horizontalCategoriesAdapter = new SelectCategorySubCategoryAdapter(dataAr, this);
        select_category_recycle.setAdapter(horizontalCategoriesAdapter);


    }

}
