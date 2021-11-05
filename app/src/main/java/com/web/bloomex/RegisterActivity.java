package com.web.bloomex;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.geetest.sdk.GT3ConfigBean;
import com.geetest.sdk.GT3ErrorBean;
import com.geetest.sdk.GT3GeetestUtils;
import com.geetest.sdk.GT3Listener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hbb20.CountryCodePicker;
import com.wallet.retrofitapi.api.ApiProduction;
import com.web.bloomex.apimodel.ApiModel;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Communication.BuildRequestParms;
import Communication.CallBackHandler;
import Communication.RetrofitCommunication;
import io.reactivex.Observable;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    private EditText txt_fname, txt_email, txt_phonenumber, txt_username, txt_password, txt_conf_password, txt_referral_code;
    private TextView privacyPolicy, termsandcondtion, login_register;
    private ImageView img_back;
    private CheckBox check_captcha;
    private CheckBox checkbox_tems;
    private GT3GeetestUtils gt3GeetestUtils;
    private GT3ConfigBean gt3ConfigBean;
    private String countrycode = "91";
    //for captcha
    String TAG = RegisterActivity.class.getSimpleName();
    String SITE_KEY = "6LeFIn4bAAAAADeh2C7Bq0KciRNCGgXf__Kw5wHC";
    String SECRET_KEY = "6LeFIn4bAAAAAMgHXifb3uN4vlU7W_DMkYgBu8dB";
    RequestQueue queue;
    CountryCodePicker ccp;
    //end of captcha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getSupportActionBar().hide();
        initiateObj();
        gt3GeetestUtils = new GT3GeetestUtils(this);
        init();
        actions();
        setHtmlCode();
    }

    private void init() {
        txt_fname = findViewById(R.id.txt_fname);
//        txt_lastname = findViewById(R.id.txt_lastname);
        txt_email = findViewById(R.id.txt_email);
        txt_phonenumber = findViewById(R.id.txt_phonenumber);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        txt_conf_password = findViewById(R.id.txt_conf_password);
        txt_referral_code = findViewById(R.id.txt_referral_code);
        termsandcondtion = findViewById(R.id.termsandcondtion);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        login_register = findViewById(R.id.login_register);
        img_back = findViewById(R.id.img_back);
        check_captcha = findViewById(R.id.check_captcha);
        checkbox_tems = findViewById(R.id.checkbox_tems);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        countrycode = ccp.getDefaultCountryCode();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                countrycode = ccp.getSelectedCountryCode();
//                Toast.makeText(getContext(), "Updated " + ccp.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setHtmlCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            termsandcondtion.setText(Html.fromHtml("Accept  <u><b>Term of Service</b></u> and ", Html.FROM_HTML_MODE_COMPACT));

        } else {
            termsandcondtion.setText(Html.fromHtml("Accept <u><b>Term of Service</b></u> and "));

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            privacyPolicy.setText(Html.fromHtml("<u><b>Privacy Policy</b></u>", Html.FROM_HTML_MODE_COMPACT));

        } else {
            privacyPolicy.setText(Html.fromHtml("<u><b>Privacy Policy</b></u>"));

        }
    }

    private void actions() {
        login_register.setEnabled(true);
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_captcha.setChecked(true);
                if (validationRule.checkEmptyString(txt_fname) == 0) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter First Name", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
//                if (validationRule.checkEmptyString(txt_lastname) == 0) {
//                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter Last Name", "Ok", "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
                if (validationRule.checkEmptyString(txt_email) == 0) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter Email Address", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmail(txt_email) == 0) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter Valid Email Address", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                }
                if (validationRule.checkEmptyString(txt_phonenumber) == 0) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter Phone Number", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (txt_phonenumber.getText().toString().length() <= 8) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter valid mobile number", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                if (validationRule.checkEmptyString(txt_username) == 0) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter Username", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(txt_password) == 0) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Enter Password", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (!validatePassword(txt_password.getText().toString())) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Use atleast 1 uppercase, 1 lowercase 1 numeric & atleast upto 8 characters", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (!txt_password.getText().toString().equalsIgnoreCase(txt_conf_password.getText().toString())) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Confirm password is not valid.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }

                if (!checkbox_tems.isChecked()) {
                    alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), "Please accept terms and privacy policy.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {
                        }
                    });
                    return;
                }

                doRegistration();
                //startCaptchaDialog();
            }
        });

        termsandcondtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalUrl(getApiUrl() + "/terms-and-conditions");
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalUrl(getApiUrl() + "/privacy-policy");
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void doRegistration() {
        try {
//            Map<String, String> m = new LinkedHashMap<>();
//            m.put("name", txt_fname.getText().toString());
////            m.put("lname", txt_lastname.getText().toString());
//            m.put("email", txt_email.getText().toString());
//            m.put("password", txt_password.getText().toString());
//            m.put("conf_password", txt_conf_password.getText().toString());
//            m.put("mobile", countrycode+txt_phonenumber.getText().toString());
//            m.put("sponsor", txt_referral_code.getText().toString());
//            m.put("username", txt_username.getText().toString());
//
//            m.put("version", getAppVersion());
//            m.put("platform", "android");
//            m.put("Timestamp", System.currentTimeMillis() + "");
//            m.put("device_token", getDeviceToken() + "");

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("X-API-KEY", getXapiKey());


//            System.out.println("before to send==="+m+"===="+headerMap);


            BuildRequestParms buildRequestParms = new BuildRequestParms();
            RetrofitCommunication retrofitCommunication = new RetrofitCommunication();
            ApiModel apiParamsInterface = ApiProduction.getInstance(RegisterActivity.this, getApiUrl()).provideService(ApiModel.class);


            Observable<Response<Object>> observable = apiParamsInterface.signUp(
                    buildRequestParms.getRequestBody(txt_fname.getText().toString()),
                    buildRequestParms.getRequestBody(txt_email.getText().toString()),
                    buildRequestParms.getRequestBody(txt_password.getText().toString()),
                    buildRequestParms.getRequestBody(txt_conf_password.getText().toString()),
                    buildRequestParms.getRequestBody(txt_phonenumber.getText().toString()),
                    buildRequestParms.getRequestBody(countrycode),
                    buildRequestParms.getRequestBody(txt_referral_code.getText().toString()),
                    buildRequestParms.getRequestBody(txt_username.getText().toString()),
                    buildRequestParms.getRequestBody(getAppVersion()),
                    buildRequestParms.getRequestBody("android"),
                    buildRequestParms.getRequestBody(getDeviceToken()),
                    getXapiKey()
            );

            System.out.println("xpai key===" + getXapiKey());

            retrofitCommunication.sendToServer(observable, RegisterActivity.this, R.layout.progressbar, R.layout.progressbar, true, new CallBackHandler() {
                @Override
                public void getResponseBack(JSONObject obj, ArrayList<Object> arrayList) {

                    try {

                        if (obj.getString("status").equalsIgnoreCase("true")) {
                            System.out.println("data return===" + obj);

                            savePreferences.savePreferencesData(RegisterActivity.this, obj.getString("token"), DefaultConstants.token);
                            Intent intent = new Intent(RegisterActivity.this, VerifyOtp.class);
                            intent.putExtra("url", "validate-signup");
                            startActivity(intent);

                        } else {
                            alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.app_name), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


        } catch (Exception e) {
            e.printStackTrace();
        }


    }





//            new ServerHandler().sendToServer(RegisterActivity.this, getApiUrl() + "app-signup", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try {
//                        JSONObject obj = new JSONObject(dta);
//                        if (obj.getBoolean("status")) {
//                            try {
//                                alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.Response), "OTP has been sent to your email id", getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                    @Override
//                                    public void getDialogEvent(String buttonPressed) {
//                                        try {
//                                            savePreferences.savePreferencesData(RegisterActivity.this, obj.getString("token"), DefaultConstants.token);
////                                            savePreferences.savePreferencesData(RegisterActivity.this, obj.getString("r_token"), DefaultConstants.r_token);
//
//
//                                               Intent intent = new Intent(RegisterActivity.this, VerifyOtp.class);
//                                               intent.putExtra("url","validate-signup");
//                                               startActivity(intent);
//
//
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            alertDialogs.alertDialog(RegisterActivity.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                @Override
//                                public void getDialogEvent(String buttonPressed) {
//                                    unauthorizedAccess(obj);
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });


//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    //}

    public static boolean validatePassword(final String password) {
        String PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    //generate captch====

    private void generateCaptcha() {
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                         //   handleSiteVerify(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

//    protected void handleSiteVerify(final String responseToken) {
//        //it is google recaptcha siteverify server
//        //you can place your server url
//        String url = "https://www.google.com/recaptcha/api/siteverify";
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            if (jsonObject.getBoolean("success")) {
//                                check_captcha.setChecked(true);
//                                login_register.setEnabled(true);
//                                login_register.setAlpha(1f);
//                                // {"success":true,"challenge_ts":"2021-07-07T06:33:55Z","apk_package_name":"com.web
//                            }
//                            else {
//                                check_captcha.setChecked(false);
//                                login_register.setEnabled(false);
//                                login_register.setAlpha(.7f);
//                                Toast.makeText(getApplicationContext(), String.valueOf(jsonObject.getString("error-codes")), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (Exception ex) {
//                            Log.d(TAG, "JSON exception: " + ex.getMessage());
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        check_captcha.setChecked(false);
//                        login_register.setEnabled(false);
//                        login_register.setAlpha(.7f);
//                        Log.d(TAG, "Error message: " + error.getMessage());
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("secret", SECRET_KEY);
//                params.put("response", responseToken);
//                return params;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(request);
//    }
//
//    //end of captcha=====
//    private void requestAPI1(){
//        String url = "https://unitedexchange.io/generate-challenge";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e(TAG, "RequestAPI2-->onPostExecute: " + response);
//                gt3ConfigBean.setApi1Json(response);
//                gt3GeetestUtils.getGeetest();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//
//    }

    private void requestAPI2(){

        Map<String, String> m = new LinkedHashMap<>();

        Map<String, String> headerMap = new HashMap<>();

        String baseurl="https://unitedexchange.io/";

        new ServerHandler().sendToServer(RegisterActivity.this, baseurl + "validate-challenge", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    Log.i(TAG, "RequestAPI2-->onPostExecute: " + dta);
                    if (!TextUtils.isEmpty(dta)) {
                        try {
                            JSONObject jsonObject = new JSONObject(dta);

                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                gt3GeetestUtils.showSuccessDialog();
                            } else {
                                gt3GeetestUtils.showFailedDialog();
                            }
                        } catch (Exception e) {
                            gt3GeetestUtils.showFailedDialog();
                            e.printStackTrace();
                        }
                    } else {
                        gt3GeetestUtils.showFailedDialog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void startCaptchaDialog(){
        // Configure the bean file
        gt3ConfigBean = new GT3ConfigBean();
       // Set how captcha is presented，1：bind，2：unbind
        gt3ConfigBean.setPattern(1);
       // The default is false
        gt3ConfigBean.setCanceledOnTouchOutside(false);
       // Set language. Use system default language if null
        gt3ConfigBean.setLang(null);
       // Set the timeout for loading webview static files
        gt3ConfigBean.setTimeout(10000);
       // Set the timeout for webview request after user finishing the CAPTCHA verification. The default is 10,000
        gt3ConfigBean.setWebviewTimeout(10000);
        // Set callback listener
        gt3ConfigBean.setListener(new GT3Listener() {

            /**
             * CAPTCHA loading is completed
             * @param duration Loading duration and version info，in JSON format
             */
            @Override
            public void onDialogReady(String duration) {
                Log.e(TAG, "GT3BaseListener-->onDialogReady-->" + duration);
            }

            /**
             * Verification result callback
             * @param code 1:success, 0:fail
             */
            @Override
            public void onReceiveCaptchaCode(int code) {
                Log.e(TAG, "GT3BaseListener-->onReceiveCaptchaCode-->" + code);
            }

            /**
             * api2 custom call
             * @param result
             */
            @Override
            public void onDialogResult(String result) {
                Log.e(TAG, "GT3BaseListener-->onDialogResult-->" + result);
                // Start api2 workflow
            //    new RequestAPI2().execute(result);
                 doRegistration();
            }

            /**
             * Statistic info.
             * @param result
             */
            @Override
            public void onStatistics(String result) {
                Log.e(TAG, "GT3BaseListener-->onStatistics-->" + result);
            }

            /**
             * Close the CAPTCHA
             * @param num 1 Click the close button to close the CAPTCHA, 2 Click anyplace on screen to close the CAPTCHA, 3 Click return button the close
             */
            @Override
            public void onClosed(int num) {
                Log.e(TAG, "GT3BaseListener-->onClosed-->" + num);
            }

            /**
             * Verfication succeeds
             * @param result
             */
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "GT3BaseListener-->onSuccess-->" + result);
            }

            /**
             * Verification fails
             * @param errorBean Version info, error code & description, etc.
             */
            @Override
            public void onFailed(GT3ErrorBean errorBean) {
                Log.e(TAG, "GT3BaseListener-->onFailed-->" + errorBean.toString());
            }

            /**
             * api1 custom call
             */
            @Override
            public void onButtonClick() {
              //  requestAPI1();
            }

        });
        gt3GeetestUtils.init(gt3ConfigBean);
// Start CAPTCHA verification
        gt3GeetestUtils.startCustomFlow();
    }


}

