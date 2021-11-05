package com.web.bloomex;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;

import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerifyOtp extends BaseActivity {
    private String concateOtp;
    private EditText ed_one, ed_two, ed_three, ed_four;
    private TextView resendOTp;
    private ArrayList<EditText> otpArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getSupportActionBar().hide();
        initiateObj();
        init();
        clearOTP();
    }


    private void init() {
        resendOTp = findViewById(R.id.resend_otp);
        resendOTp.setTag("0");


        handleResendButton();

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ed_one = findViewById(R.id.ed_otp1);
        ed_two = findViewById(R.id.ed_otp2);
        ed_three = findViewById(R.id.ed_otp3);
        ed_four = findViewById(R.id.ed_otp4);


        ed_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_two.requestFocus();
                }

            }
        });

        ed_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_three.requestFocus();
                }
            }
        });

        ed_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_four.requestFocus();
                }

            }
        });

        ed_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_four.requestFocus();
                }

            }
        });


        TextView btn_button = findViewById(R.id.btn_button);
        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                concateOtp = ed_one.getText() + "" + ed_two.getText() + "" + ed_three.getText() + "" + ed_four.getText() + "";
                if (concateOtp.length() > 3) {
                    activateAccountApi(concateOtp);
                } else {

                    alertDialogs.alertDialog(VerifyOtp.this, getResources().getString(R.string.Response), "Entered OTP is invalid.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });

                }


            }
        });


        resendOTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendOTp.getTag().toString().equalsIgnoreCase("0")) {
//                    getPhoneNumber("+" + isd_code + phone);
                    handleResendButton();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }


    private void validateOtp() {

        // verifyPhoneNumberWithCode(mVerificationId, concateOtp);

    }

    int counter = 60;
    Handler handler;

    private void handleResendButton() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                counter--;
                if (counter > 0) {
                    resendOTp.setTag("1");
                    resendOTp.setText("Resend OTP in : " + counter + " Seconds");
                    handler.postDelayed(this, 1000);
                } else {
                    counter = 60;
                    resendOTp.setText("Resend");
                    resendOTp.setTag("0");
                    handler.removeCallbacks(this);

                }

            }
        }, 1000);


    }


    private void clearOTP() {
        otpArray.clear();
        otpArray.add(ed_one);
        otpArray.add(ed_two);
        otpArray.add(ed_three);
        otpArray.add(ed_four);

        ed_one.setTag("0");
        ed_two.setTag("1");
        ed_three.setTag("2");
        ed_four.setTag("3");


        for (int x = 0; x < otpArray.size(); x++) {
            otpArray.get(x).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        int index = Integer.parseInt(v.getTag().toString());
                        if (index > 0) {
                            otpArray.get(index).setText("");
                            otpArray.get(index - 1).requestFocus();
                        }
                    }
                    return false;
                }
            });
        }
    }


    private void activateAccountApi(String otp) {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("otp", otp);
            m.put("token", savePreferences.reterivePreference(this, DefaultConstants.token) + "");
            m.put("version", getAppVersion());
            m.put("platform", "android");
            m.put("Timestamp", System.currentTimeMillis() + "");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("X-API-KEY", getXapiKey());
            System.out.println("Before to send====" + m + "====" + obj);


            serverHandler.sendToServer(VerifyOtp.this, getApiUrl() + getIntent().getStringExtra("url"), m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {

                    try {
                        JSONObject jsonObject = new JSONObject(dta);


                        if (jsonObject.getBoolean("status")) {

                            savePreferences.savePreferencesData(VerifyOtp.this, jsonObject.getString("token"), UtilClass.token);
                            savePreferences.savePreferencesData(VerifyOtp.this, jsonObject + "", DefaultConstants.login_detail);

                            Intent intent = new Intent(VerifyOtp.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            alertDialogs.alertDialog(VerifyOtp.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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



