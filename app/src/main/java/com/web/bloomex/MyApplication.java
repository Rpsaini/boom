package com.web.bloomex;

import androidx.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.androidnetworking.AndroidNetworking;

import fontspackageForTextView.DefineYourAppFont;

public class MyApplication extends MultiDexApplication {

    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;



    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
      /*
        DefineYourAppFont.fontNameRegular="fonts/OpenSans-Regular.ttf";
        DefineYourAppFont.fontNameBold="fonts/OpenSans-Bold.ttf";
        DefineYourAppFont.fontNameBoldExtra="fonts/OpenSans-ExtraBold.ttf";
        DefineYourAppFont.fontNameItalic="fonts/OpenSans-Italic.ttf";
        DefineYourAppFont.fontNameBoldItalic="OpenSans-BoldItalic.ttf";
        DefineYourAppFont.fontNameLiteItalic="fonts/OpenSans-LightItalic.ttf";
        DefineYourAppFont.fontNameBoldMedium="fonts/OpenSans-SemiBold.ttf";
        */


        DefineYourAppFont.fontNameRegular="aeonik_font/Aeonik-Regular.otf";
        DefineYourAppFont.fontNameBold="aeonik_font/Aeonik-Bold.otf";
        DefineYourAppFont.fontNameBoldExtra="aeonik_font/Aeonik-Black.otf";
        DefineYourAppFont.fontNameItalic="aeonik_font/Aeonik-RegularItalic.otf";
        DefineYourAppFont.fontNameBoldItalic="aeonik_font/Aeonik-BoldItalic.otf";
        DefineYourAppFont.fontNameLiteItalic="aeonik_font/Aeonik-LightItalic.otf";
        DefineYourAppFont.fontNameBoldMedium="aeonik_font/Aeonik-Medium.otf";
        mInstance = this;

    }





}