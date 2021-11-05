package com.web.bloomex.fund_withdrawal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmWithdrawalFundScreen extends BaseActivity
{
    private ImageView backIC=null;
    private TextView confirmBT=null;
    private Dialog confirmDialog;
    String destinationAddressET,currenyAmount,feeapplicable,total,remarks,symbol;
    private String destinationTag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.witdraw_confrim_screen);
        initiateObj();
        initView();
        setOnClickListener();
    }

    private void initView()
    {
        backIC=findViewById(R.id.backIC);
        confirmBT=findViewById(R.id.confirmBT);


         destinationAddressET= getIntent().getStringExtra(DefaultConstants.destinationAddressET);
         currenyAmount= getIntent().getStringExtra(DefaultConstants.currenyAmount);
         feeapplicable=getIntent().getStringExtra(DefaultConstants.feeapplicable);
         total=getIntent().getStringExtra(DefaultConstants.total);
         remarks=getIntent().getStringExtra(DefaultConstants.remarks);
         symbol=getIntent().getStringExtra(DefaultConstants.symbol);

        TextView destinationAddressValueTV=findViewById(R.id.destinationAddressValueTV);
        TextView BTCAmountValueTV=findViewById(R.id.BTCAmountValueTV);
        TextView withdrawalFeesValueTV=findViewById(R.id.withdrawalFeesValueTV);
        TextView finalAmountTV=findViewById(R.id.finalAmountTV);
        TextView remarksValueTV=findViewById(R.id.remarksValueTV);
        LinearLayout ll_destination_tag=findViewById(R.id.ll_destination_tag);
        TextView destination_tag_txt=findViewById(R.id.destination_tag_txt);
        TextView BTCAmountTV=findViewById(R.id.BTCAmountTV);
        TextView tvTitle=findViewById(R.id.tvTitle);
        BTCAmountTV.setText(symbol+" Amount");
        tvTitle.setText("Confirm "+symbol+" Withdraw");

        destinationAddressValueTV.setText(destinationAddressET);
        BTCAmountValueTV.setText(currenyAmount);
        withdrawalFeesValueTV.setText(feeapplicable);
        finalAmountTV.setText(total);
        remarksValueTV.setText(remarks);

        destinationTag=getIntent().getStringExtra(DefaultConstants.destinationtag);
        if(destinationTag.length()>0)
        {
            destination_tag_txt.setText(destinationTag);
            ll_destination_tag.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_destination_tag.setVisibility(View.GONE);
        }


    }

    private void setOnClickListener(){
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawWithoutAuth();
            }
        });
    }
    private void confirmWithdrawFundDialog(String message) {
        try
        { SimpleDialog simpleDialog = new SimpleDialog();
            confirmDialog = simpleDialog.simpleDailog(this, R.layout.confirm_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);

            TextView txtConfirm = confirmDialog.findViewById(R.id.txtConfirm);
            TextView tvMessage = confirmDialog.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            confirmDialog.setCancelable(true);

            txtConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    confirmDialog.dismiss();
                    Intent intent=new Intent();
                    intent.putExtra("data","");
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void withdrawWithoutAuth()
    {
        Map<String, String> map = new HashMap<>();
        map.put("token", savePreferences.reterivePreference(this, DefaultConstants.token) + "");
        map.put("currency", symbol);
        map.put("amount", currenyAmount);
        map.put("address", destinationAddressET);
        if(destinationTag.length()>0) {
            map.put("destination_tag", destinationTag);
        }
        map.put("DeviceToken", getDeviceToken());

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", getNewRToken() + "");




        new ServerHandler().sendToServer(ConfirmWithdrawalFundScreen.this, getApiUrl() + "proceed-withdraw", map, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);

                    if (obj.getBoolean("status"))
                    {

                                confirmWithdrawFundDialog(obj.getString("msg"));

                    } else {
                        alertDialogs.alertDialog(ConfirmWithdrawalFundScreen.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                                unauthorizedAccess(obj);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
