package com.web.bloomex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.web.bloomex.communication.SocketHandlers;
import com.web.bloomex.utilpackage.UtilClass;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        initiateObj();





        new SocketHandlers().createConnection();
        String loginData=savePreferences.reterivePreference(this, DefaultConstants.login_detail).toString();

        //            new Handler().postDelayed(new Runnable() {
        //                @Override
        //                public void run()
        //                {
        //
        //                    if(savePreferences.reterivePreference(SplashScreen.this, UtilClass.isLogin).toString().equalsIgnoreCase("true"))
        //                    {
        //
        //                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        //                        startActivity(intent);
        //                        finish();
        //
        //                    }
        //                    else
        //                    {
        //                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        //                        startActivity(intent);
        //                        finish();
        //                    }
        //                }
        //            },2000);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(savePreferences.reterivePreference(SplashScreen.this, UtilClass.isLogin).toString().equalsIgnoreCase("true"))
                {

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }




}