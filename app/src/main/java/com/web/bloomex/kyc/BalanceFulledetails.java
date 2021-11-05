package com.web.bloomex.kyc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalanceFulledetails extends BaseActivity {

    private ImageView qrImageView;
    private TextView copytext, txt_destinationtag;
    private String isKycStatus = "";
    private boolean authenticator = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposti_coin_dialog);
        getSupportActionBar().hide();
        initiateObj();
        init();

    }

    private void init() {
        try {
            JSONObject dataObj = new JSONObject(getIntent().getStringExtra("data"));
            String symbol = dataObj.getString("symbol");
            //String availableBalance = dataObj.getString("available_balance");
            //final String icon = dataObj.getString("icon");

//          ImageView txt_currency_image = findViewById(R.id.scanImgIC);
            txt_destinationtag = findViewById(R.id.txt_destinationtag);
            qrImageView = findViewById(R.id.qrCodeIC);

            copytext = findViewById(R.id.copyTV);

            findViewById(R.id.closeIC).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
            TextView tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText("Deposit "+symbol);
            TextView notText=findViewById(R.id.pleas_cross);
            String noteMsg="Please deposit only "+ symbol+" to this address. If you deposit any other coins, it will be lost forever.";
            notText.setText(noteMsg);
//          TextView txt_title = findViewById(R.id.txt_title);

//          TextView total_balance = findViewById(R.id.total_balance);
            //TextView txt_share = findViewById(R.id.txt_share);

//          total_balance.setText(availableBalance);

//            txt_title.setText("PLEASE PAY ATTENTION! THE COIN YOU SELECTED IS : " + symbol);
            //showImage(icon, txt_currency_image);
//            findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });

//            txt_withdraw.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!isKycStatus.equalsIgnoreCase("1"))
//                    {
//
//                            Intent intent = new Intent(BalanceFulledetails.this, WithdrawMoney.class);
//                            intent.putExtra("isGoogleAuth",authenticator);
//                            intent.putExtra("currency",symbol);
//                            intent.putExtra("balance",availableBalance);
//                            intent.putExtra("icon",icon);
//                            startActivityForResult(intent, 1001);
//
//
//                    } else {
//                        alertDialogs.alertDialog(BalanceFulledetails.this, getResources().getString(R.string.app_name), "For money withdraw you need to verify KYC.", "OK", "CANCEL", new DialogCallBacks() {
//                            @Override
//                            public void getDialogEvent(String buttonPressed) {
//                                if (buttonPressed.equalsIgnoreCase("ok")) {
//                                    openExternalUrls("http://unitedexchange.io/");
//                                }
//                            }
//                        });
//                    }
//
//
//                }
//            });

//            txt_share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Sharedialogs shareDialog = new Sharedialogs();
//                    shareDialog.shareDialog(BalanceFulledetails.this, copytext.getText().toString(), "");
//
//                }
//            });

            copytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    copyCode(copytext.getText().toString());
                }
            });


            getMarketTickers(symbol);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMarketTickers(String symbol) {
        try
         {
            final Map<String, String> m = new HashMap<>();
            m.put("token", savePreferences.reterivePreference(BalanceFulledetails.this, DefaultConstants.token) + "");
            m.put("DeviceToken", getDeviceToken() + "");
            m.put("currency", symbol);


            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", getXapiKey());
            obj.put("Rtoken", getNewRToken()+"");


            new ServerHandler().sendToServer(BalanceFulledetails.this, getApiUrl() + "get-deposit-address", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons)
                {
                       try
                        {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {
                                isKycStatus = jsonObject.getString("kyc_status");
                                authenticator = jsonObject.getBoolean("authenticator");
                                if(jsonObject.getString("destination_tag").length() > 0) {
                                    txt_destinationtag.setText("Destination Tag : " + jsonObject.getString("destination_tag"));
                                    txt_destinationtag.setVisibility(View.VISIBLE);
                                }
                                copytext.setText(jsonObject.getString("address"));
                                Bitmap bitmap = TextToImageEncode(jsonObject.getString("address"));
                                qrImageView.setImageBitmap(bitmap);

                              }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else {
                            alertDialogs.alertDialog(BalanceFulledetails.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                    unauthorizedAccess(jsonObject);
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

    private void showImage(final String url, final ImageView header_img) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(BalanceFulledetails.this)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                        .into(header_img);
            }
        });
    }




}