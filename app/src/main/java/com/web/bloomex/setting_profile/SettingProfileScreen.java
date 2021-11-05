package com.web.bloomex.setting_profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.activity_log.ActivityLogScreens;
import com.web.bloomex.currency_preferences.CurrencyPreferencesScreen;
import com.web.bloomex.kyc.VerifyKycAccountDetailsScreen;
import com.web.bloomex.payment_option.PaymentOptionsScreen;
import com.web.bloomex.two_factor_auth.TwoFactorAuthScreen;

import org.json.JSONObject;

public class SettingProfileScreen extends BaseActivity {
    private ImageView backIC=null;
    private RelativeLayout verify_kyc_layout=null;
    private Dialog mobRegDialog;
    private Dialog mobRegOtpDialog;
    private Dialog mobRegSuccessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_profile_screen);
        initiateObj();
        initView();
        setOnClickListener();
        setData();
    }

    private void initView(){
        backIC =findViewById(R.id.backIC);
        verify_kyc_layout=findViewById(R.id.verify_kyc_layout);
    }

    private void setOnClickListener(){
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verify_kyc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SettingProfileScreen.this, VerifyKycAccountDetailsScreen.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.mobileValueTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showMobileRegDialog();
            }
        });
        findViewById(R.id.currency_pr_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(SettingProfileScreen.this, CurrencyPreferencesScreen.class);
               startActivity(intent);
            }
        });
        findViewById(R.id.two_factor_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingProfileScreen.this, TwoFactorAuthScreen.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.activity_lo_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingProfileScreen.this, ActivityLogScreens.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.payment_opt_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingProfileScreen.this, PaymentOptionsScreen.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.privacyPolicy_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalUrls(getApiUrl()+"privacy-policy");
            }
        });
        findViewById(R.id.contactUs_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalUrls(getApiUrl()+"contactus");
            }
        });
        findViewById(R.id.logout_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogs alertDialogs=new AlertDialogs();
                alertDialogs.alertDialog(SettingProfileScreen.this, "Logout", "Would you like to logout ?", "Yes", "No", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {

                        if(buttonPressed.equalsIgnoreCase("Yes"))
                        {
                            logout();
                        }
                    }
                });
            }
        });

    }
    private void showMobileRegDialog() {
        try
        {
            SimpleDialog simpleDialog = new SimpleDialog();
            mobRegDialog = simpleDialog.simpleDailog(this, R.layout.mobile_reg_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            ConstraintLayout root_layout = mobRegDialog.findViewById(R.id.root_layout);
            TextView send_otpTV = mobRegDialog.findViewById(R.id.send_otpTV);
            ImageView closeIC = mobRegDialog.findViewById(R.id.closeIC);

            mobRegDialog.setCancelable(true);
            closeIC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobRegDialog.dismiss();
                }
            });
            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobRegDialog.dismiss();
                }
            });
            send_otpTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mobRegDialog.dismiss();
                    showMobileOtpRegDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showMobileOtpRegDialog() {
        try
        {
            SimpleDialog simpleDialog = new SimpleDialog();
            mobRegOtpDialog = simpleDialog.simpleDailog(this, R.layout.mobile_reg_otp_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            ConstraintLayout root_layout = mobRegOtpDialog.findViewById(R.id.root_layout);
            TextView authenticateTV = mobRegOtpDialog.findViewById(R.id.authenticatTV);
            ImageView closeIC = mobRegOtpDialog.findViewById(R.id.closeIC);

            mobRegOtpDialog.setCancelable(true);
            closeIC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobRegOtpDialog.dismiss();
                }
            });
            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobRegOtpDialog.dismiss();
                }
            });
            authenticateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mobRegOtpDialog.dismiss();
                    showMobileRegSuccessDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void showMobileRegSuccessDialog() {
        try
        {
            SimpleDialog simpleDialog = new SimpleDialog();
            mobRegSuccessDialog = simpleDialog.simpleDailog(this, R.layout.mobile_reg_otp_successfully_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            ConstraintLayout root_layout = mobRegSuccessDialog.findViewById(R.id.root_layout);
            TextView continueTV = mobRegSuccessDialog.findViewById(R.id.continueTV);
            ImageView closeIC = mobRegSuccessDialog.findViewById(R.id.closeIC);

            mobRegSuccessDialog.setCancelable(true);
            closeIC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobRegSuccessDialog.dismiss();
                }
            });
            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobRegSuccessDialog.dismiss();
                }
            });
            continueTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mobRegSuccessDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setData()
    {
        try {
            TextView nameValueTV  = findViewById(R.id.nameValueTV);
            TextView emailValueTV =  findViewById(R.id.emailValueTV);
            TextView mobileValueTV = findViewById(R.id.mobileValueTV);

            JSONObject data=new JSONObject(savePreferences.reterivePreference(this, DefaultConstants.login_detail).toString());
            nameValueTV.setText(data.getString("name"));
            emailValueTV.setText(data.getString("email"));
            mobileValueTV.setText(data.getString("mobile"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
