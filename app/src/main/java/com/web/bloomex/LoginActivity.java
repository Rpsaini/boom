package com.web.bloomex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;

import com.wallet.retrofitapi.api.ApiProduction;
import com.web.bloomex.apimodel.ApiModel;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Communication.BuildRequestParms;
import Communication.CallBackHandler;
import Communication.RetrofitCommunication;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private EditText publicKey, sceretKey;
    private TextView txt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initiateObj();
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        findViewById(R.id.login_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView loginBtn = findViewById(R.id.login_btn);
        publicKey = findViewById(R.id.login_username);
        sceretKey = findViewById(R.id.login_password);
        txt_register = findViewById(R.id.txt_register);
        final ImageView scanqrcode = findViewById(R.id.scanqrcode);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationRule.checkEmptyString(publicKey) == 0) {
                    alertDialogs.alertDialog(LoginActivity.this, getResources().getString(R.string.app_name), "Enter User name.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (validationRule.checkEmptyString(sceretKey) == 0) {
                    alertDialogs.alertDialog(LoginActivity.this, getResources().getString(R.string.app_name), "Enter Password.", "Ok", "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                try {
                    BuildRequestParms buildRequestParms=new BuildRequestParms();
                    RetrofitCommunication retrofitCommunication=new RetrofitCommunication();
                    ApiModel apiParamsInterface = ApiProduction.getInstance(LoginActivity.this,getApiUrl()).provideService(ApiModel.class);



                    Observable<Response<Object>> observable =apiParamsInterface.loginApi(
                            buildRequestParms.getRequestBody(publicKey.getText().toString().trim()),
                            buildRequestParms.getRequestBody(sceretKey.getText().toString().trim()),
                            buildRequestParms.getRequestBody(getAppVersion()),
                            buildRequestParms.getRequestBody("android"),
                            buildRequestParms.getRequestBody(getDeviceToken()),
                            getXapiKey()
                    );

                    System.out.println("xpai key==="+getXapiKey());

                    retrofitCommunication.sendToServer(observable, LoginActivity.this, R.layout.progressbar, R.layout.progressbar, true, new CallBackHandler() {
                        @Override
                        public void getResponseBack(JSONObject obj, ArrayList<Object> arrayList) {

                            try {

                                if (obj.getString("status").equalsIgnoreCase("true"))
                                {
                                     System.out.println("data return===" + obj);
                                     savePreferences.savePreferencesData(LoginActivity.this, obj.getString("token"), DefaultConstants.token);
                                     savePreferences.savePreferencesData(LoginActivity.this, obj + "", DefaultConstants.login_detail);
                                        if (obj.getString("isOtpverified").equalsIgnoreCase("true")) {

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, VerifyOtp.class);
                                            intent.putExtra("url", "validate-signup");
                                            startActivity(intent);
                                        }
                                }
                                else {
                                    alertDialogs.alertDialog(LoginActivity.this, getResources().getString(R.string.app_name), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


        });


        scanqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectAndGotonext();
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.get_keys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GetKeysActivity.class);
                startActivity(intent);
            }
        });


    }

    public void redirectAndGotonext() {
        if (checkAndRequestPermissions() == 0) {
            Intent intent = new Intent(LoginActivity.this, ScanQrCode.class);
            intent.putExtra("title", "Scan Qr code to login");
            startActivityForResult(intent, 1001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                String qrcode = data.getStringExtra("code");
                if (qrcode.contains("-")) {
                    String ar[] = qrcode.split("-");
                    String publickey = ar[0];
                    String scretkey = ar[1];

                    publicKey.setText(publickey);
                    sceretKey.setText(scretkey);
                }

            }
        }
    }

    private int checkAndRequestPermissions() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return 1;
        }

        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        redirectAndGotonext();
    }
}