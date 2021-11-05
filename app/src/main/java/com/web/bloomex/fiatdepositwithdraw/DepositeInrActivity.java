package com.web.bloomex.fiatdepositwithdraw;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DepositeInrActivity extends BaseActivity {

    private TextView txt_important_note, txt_deposit_code, submit;
    private ImageView img_back;
    private EditText ed_amount, ed_remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_inr);
        getSupportActionBar().hide();
        initiateObj();
        init();
        actions();
        setData();
        generateSixDigitRandomNumber();

    }

    private void init() {
        txt_important_note = findViewById(R.id.txt_important_note);

        txt_deposit_code = findViewById(R.id.txt_deposite_code);
        submit = findViewById(R.id.submit);
        img_back = findViewById(R.id.img_back);
        ed_remarks = findViewById(R.id.ed_remarks);
        ed_amount = findViewById(R.id.ed_amount);


    }

    private void actions() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(ed_amount) == 0) {
                    alertDialogs.alertDialog(DepositeInrActivity.this, getResources().getString(R.string.app_name), "Enter Deposit Amount.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(ed_remarks) == 0) {
                    alertDialogs.alertDialog(DepositeInrActivity.this, getResources().getString(R.string.app_name), "Enter Remark.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }
                sendForDeposit();

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_deposit_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_deposit_code.getText().toString());
            }
        });

    }

    private void setData() {
        String content = "<p><strong>Important Notes</strong></p>\n" +
                "<p>Please Provide your Deposit Code in the bank slip - remarks field to recognize your Deposit.</p>\n" +
                "<p>When you add your deposit code belonging to your account to the description section  while making a transfer, your investments are automatically transferred to your account.</p>\n" +
                "<p>You must make transfers from your individual deposit accounts opened in your name. Investments made from accounts belonging to a different person are not accepted.</p>\n" +
                "<p>All transfers (with / without card) using ATM will not be accepted as it is not possible to confirm the sender information.</p>\n" +
                "<p>The amount you send will be automatically reflected in your account after the security checks.</p>\n" +
                "<p>No other action from you is needed.During the transfer, you must write the deposit code determined specifically for you in the description field.Transfers made without deposit code will be refunded by deducting the transaction fee.</p>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txt_important_note.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            txt_important_note.setText(Html.fromHtml(content));
        }
    }

    private void generateSixDigitRandomNumber() {
        Random r = new Random();
        int numbers = 100000 + (int) (r.nextFloat() * 899900);
        txt_deposit_code.setText(numbers + "");
    }


    private void sendForDeposit() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("token", savePreferences.reterivePreference(DepositeInrActivity.this, DefaultConstants.token) + "");
            m.put("DeviceToken", getDeviceToken() + "");
            m.put("currency", "INR");
            m.put("amount", ed_amount.getText().toString());
            m.put("reference", txt_deposit_code.getText().toString());
            m.put("remarks", ed_remarks.getText().toString());


            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", getXapiKey());
            obj.put("Rtoken", getNewRToken() + "");


            new ServerHandler().sendToServer(DepositeInrActivity.this, getApiUrl() + "fiat-deposit", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            alertDialogs.alertDialog(DepositeInrActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                    finish();
                                }
                            });
                        } else {
                            alertDialogs.alertDialog(DepositeInrActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


}