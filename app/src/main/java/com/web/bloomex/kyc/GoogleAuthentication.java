package com.web.bloomex.kyc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
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

public class GoogleAuthentication extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_authentication);
        getSupportActionBar().hide();
        initiateObj();
        init();
    }

    private void init()
    {
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EditText ed_google_code = findViewById(R.id.ed_google_code);

        TextView login_submit = findViewById(R.id.login_submit);
        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(ed_google_code) == 0) {
                    alertDialogs.alertDialog(GoogleAuthentication.this, getResources().getString(R.string.app_name), "Enter Google authentication code", "ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }

                withdrawWithAuth(ed_google_code.getText().toString());

            }
            });

    }

    private void withdrawWithAuth(String auth)
    {
        Map<String, String> map = new HashMap<>();
        map.put("token", savePreferences.reterivePreference(GoogleAuthentication.this, DefaultConstants.token) + "");
        map.put("currency", getIntent().getStringExtra("currency"));
        map.put("amount", getIntent().getStringExtra("amount"));
        map.put("DeviceToken", getDeviceToken());

        if(getIntent().getStringExtra("destination_tag").length()>0) {
            map.put("destination_tag", getIntent().getStringExtra("destination_tag"));
        }
        map.put("address",getIntent().getStringExtra("address"));
        map.put("auth_code",auth);



        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", getNewRToken() + "");


        new ServerHandler().sendToServer(GoogleAuthentication.this, getApiUrl() + "proceed-withdraw", map, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status"))
                    {

                        alertDialogs.alertDialog(GoogleAuthentication.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed)
                            {
                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                intent.putExtra("data","true");
                                finish();
                            }
                        });
                    }
                    else {
                        alertDialogs.alertDialog(GoogleAuthentication.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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
    }
}