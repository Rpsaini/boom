package com.web.bloomex;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GetKeysActivity extends BaseActivity {
EditText ed_email;
private ImageView img_back;
private TextView btn_getkeys;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_keys);
        getSupportActionBar().hide();
        initiateObj();
        init();
        actions();
    }
    private void init()
    {
        ed_email=findViewById(R.id.ed_email);
        btn_getkeys=findViewById(R.id.btn_getkeys);
        img_back=findViewById(R.id.img_back);

    }
    private void actions()
    {
        btn_getkeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationRule.checkEmptyString(ed_email)==0)
                {
                    if(validationRule.checkEmptyString(ed_email) == 0) {
                        alertDialogs.alertDialog(GetKeysActivity.this, getResources().getString(R.string.app_name), "Enter Email", "Ok", "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {

                            }
                        });
                        return;
                    }
                }
                if(validationRule.checkEmail(ed_email) == 0)
                {
                    alertDialogs.alertDialog(GetKeysActivity.this, getResources().getString(R.string.app_name), "Enter valid Email", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                sendRequest();

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void sendRequest()
    {
        try {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("email", ed_email.getText().toString());
            m.put("Version", getAppVersion());
            m.put("PlatForm", "android");
            m.put("Timestamp", System.currentTimeMillis() + "");
            m.put("DeviceToken", getDeviceToken() + "");

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("X-API-KEY", UtilClass.xApiKey);

            new ServerHandler().sendToServer(GetKeysActivity.this, getApiUrl() + "generate-keys", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject obj = new JSONObject(dta);
                        if (obj.getBoolean("status")) {
                            try {
                                alertDialogs.alertDialog(GetKeysActivity.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                    @Override
                                    public void getDialogEvent(String buttonPressed) {
                                        finish();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(GetKeysActivity.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed)
                                {
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}