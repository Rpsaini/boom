package com.web.bloomex.kyc;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.ScanQrCode;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WithdrawMoney extends BaseActivity {
    private EditText txt_address, txt_amount, ed_destinationtag;
    String comparisionSymbol = "XRP";
    private double availableBal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        getSupportActionBar().hide();
        initiateObj();
        init();
    }

    private void init() {
        txt_address = findViewById(R.id.txt_address);
        txt_amount = findViewById(R.id.txt_amount);

        ImageView txt_currency_image1 = findViewById(R.id.txt_currency_image1);
        TextView total_balance1 = findViewById(R.id.total_balance1);

         availableBal= Double.parseDouble(getIntent().getStringExtra("balance"));
        total_balance1.setText(availableBal+"");
        showImage(getIntent().getStringExtra("icon"), txt_currency_image1);


        String symbol = getIntent().getStringExtra("currency");
        ed_destinationtag = findViewById(R.id.txt_destinationtag);
        if(symbol.equalsIgnoreCase(comparisionSymbol))
        {
            findViewById(R.id.txt_label_destinationtag).setVisibility(View.VISIBLE);

            ed_destinationtag.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.scanqrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(WithdrawMoney.this, ScanQrCode.class);
                intent.putExtra("title", "Scan Qr code");
                startActivityForResult(intent, 1001);
            }
        });


        TextView submit=findViewById(R.id.submit);
        if(getIntent().getBooleanExtra("isGoogleAuth",false))
        {
            submit.setText("Next");
        }
        else
        {
            submit.setText("Confirm Withdraw");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationRule.checkEmptyString(txt_address) == 0) {
                    alertDialogs.alertDialog(WithdrawMoney.this, getResources().getString(R.string.Response), "Enter Address", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(txt_amount) == 0) {
                    alertDialogs.alertDialog(WithdrawMoney.this, getResources().getString(R.string.Response), "Enter withdraw amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                if (symbol.equalsIgnoreCase(comparisionSymbol)) {
                    if (validationRule.checkEmptyString(ed_destinationtag) == 0) {
                        alertDialogs.alertDialog(WithdrawMoney.this, getResources().getString(R.string.Response), "Enter destination tag.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                            }
                        });
                        return;
                    }
                }
                if(availableBal<Double.parseDouble(txt_amount.getText().toString()))
                {
                    alertDialogs.alertDialog(WithdrawMoney.this, getResources().getString(R.string.Response), "Withdraw amount should be less than available amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed)
                        {
                        }
                    });
                    return;
                }

                if(getIntent().getBooleanExtra("isGoogleAuth",false))
                {
                    Intent intent = new Intent(WithdrawMoney.this, GoogleAuthentication.class);
                    intent.putExtra("currency", symbol);
                    intent.putExtra("amount", txt_amount.getText().toString());
                    intent.putExtra("address", txt_address.getText().toString());
                    intent.putExtra("destination_tag", ed_destinationtag.getText().toString());
                    startActivityForResult(intent, 1002);

                }
                else
                    {
                    withdrawWithoutAuth(symbol,txt_amount.getText().toString(),txt_address.getText().toString(),ed_destinationtag.getText().toString());
                    }
            }
          });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001)
        {
            if (data != null) {
                String qrcode = data.getStringExtra("code");
                //if (qrcode.contains("-")) {
                   // String ar[] = qrcode.split("-");
                    txt_address.setText(qrcode);
                //}

            }
        }
        else if(requestCode==1002)
        {
            if(data!=null)
            {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                intent.putExtra("data","data");
                finish();
            }

        }
    }

    private void showImage(final String url, final ImageView header_img)
    {
        Glide.with(WithdrawMoney.this)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                .into(header_img);
    }


    private void withdrawWithoutAuth(String symbol,String amount,String address,String tag)
        {
        Map<String, String> map = new HashMap<>();
        map.put("token", savePreferences.reterivePreference(WithdrawMoney.this, DefaultConstants.token) + "");
        map.put("currency", symbol);
        map.put("amount", amount);
        map.put("DeviceToken", getDeviceToken());
        if(tag.length()>0) {
            map.put("destination_tag", tag);
         }
        map.put("address",address);


        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", getNewRToken() + "");


        new ServerHandler().sendToServer(WithdrawMoney.this, getApiUrl() + "proceed-withdraw", map, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status"))
                    {
                        alertDialogs.alertDialog(WithdrawMoney.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed)
                            {

                                finish();
                            }
                        });
                    } else {
                        alertDialogs.alertDialog(WithdrawMoney.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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





}