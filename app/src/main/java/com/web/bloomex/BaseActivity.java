package com.web.bloomex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.dialogsnpickers.AlertDialogs;
import com.app.preferences.SavePreferences;
import com.app.validation.ValidationRule;
import com.app.vollycommunicationlib.ServerHandler;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import animationpackage.AnimationForViews;
import animationpackage.IsAnimationEndedCallback;

public class BaseActivity extends AppCompatActivity {
    public SavePreferences savePreferences;
    public ServerHandler serverHandler;
    public AlertDialogs alertDialogs;
    public ValidationRule validationRule;
    public AnimationForViews animationForViews;
    private int screenHeight, screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


    }

    public void initiateObj()
    {
        savePreferences = new SavePreferences();
        serverHandler = new ServerHandler();
        alertDialogs = new AlertDialogs();
        validationRule = new ValidationRule();
        animationForViews = new AnimationForViews();
        changestatusBarColor(0);
     }

    public void removeActionBar() {
        getSupportActionBar().hide();
    }


    public void changestatusBarColor(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200));
        }
    }


    public String getAppVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";
    }

    public String getDeviceToken()
    {
//       return savePreferences.reterivePreference(BaseActivity.this, UtilClass.device_Token) + "";
        String deviceToken=savePreferences.reterivePreference(BaseActivity.this, UtilClass.device_Token) + "";
        if(deviceToken.length()==0)
        {
            final int min = 20;
            final int max = 800;
            final int random = new Random().nextInt((max - min) + 1) + min;
            savePreferences.savePreferencesData(BaseActivity.this,"devicetoken"+random, UtilClass.device_Token);
            return  "devicetoken"+random;

        }
        else
        {
            return deviceToken;
        }


    }


    public String getRestParamsName(String keyname) {
        try
         {
            String data=savePreferences.reterivePreference(this, DefaultConstants.login_detail)+"";

            JSONObject dataObj = new JSONObject(data);

            if (keyname.equalsIgnoreCase(UtilClass.token)) {
                return dataObj.getString(keyname);
            } else {
                return dataObj.getString(keyname);
            }
         }
         catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }


    //Also change qr code validation url
    public String getApiUrl() {
        return "https://bloomex.io/dev/";
    }

    public String getXapiKey() {
        return "Q4GxNgqgKV9XJyoKHNgsdfFERFS";
    }


    public void addClickEventEffet(final View viwView) {
        viwView.setAlpha(.5f);
//        Handler hnd = new Handler();
//        hnd.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                viwView.setAlpha(1f);
//            }
//        }, 100);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viwView.setAlpha(1f);
            }
        }, 100);

    }



    public void animateUp(View sourcedestinationcontainer) {
        getScreenHeight();
        animationForViews.handleAnimation(this, sourcedestinationcontainer, 500, screenHeight, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {


                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }


    public void downSourceDestinationView(View sourcedestinationcontainer, final Dialog dialog) {
        animationForViews.handleAnimation(this, sourcedestinationcontainer, 500, 00, screenHeight, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public long getNewRToken() {
        String r_token = savePreferences.reterivePreference(this, "r_token").toString();

        if (r_token.length() > 0) {
            String[] rtokenSplitted = r_token.split("-");
            long rFastHalf = Long.parseLong(rtokenSplitted[0]);
            String rSenHalf = rtokenSplitted[1];


            long calCulatedResult = 0;
            for (int i = 0; i < rSenHalf.length(); i++) {

                char alpha = rSenHalf.charAt(i);
                int value = alpha;

                if (i == 0) {
                    calCulatedResult = rFastHalf * value;
                } else if (i == 1) {
                    calCulatedResult = calCulatedResult - value;
                } else if (i == 2) {
                    calCulatedResult = calCulatedResult + value;
                }
            }

            return calCulatedResult;
        }

        return 0;
    }

    public void openExternalUrls(String url) {
        try {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void launchPlayStore(Context context, String packageName) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public Bitmap TextToImageEncode(String Value) throws WriterException {

        int black = 0xFF000000;
        int white = 0xFFFFFFFF;

        int width = 400;
        int height = 400;

        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(Value,
                    BarcodeFormat.QR_CODE, height, width, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? black : white;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//Use the same value of the height and width respectively
            bitmap.setPixels(pixels, 0, 400, 0, 0, 400, 400);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public RequestOptions roundedCorners(RequestOptions options, @NonNull Context context, int cornerRadius) {
        int px = Math.round(cornerRadius * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return options.transforms(new RoundedCorners(px));
    }

    public void copyCode(String code)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("qrcode", code);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Code copied", Toast.LENGTH_LONG).show();


    }
    public void openExternalUrl(String url)
    {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            System.out.println("Exception opening in url--" + e.getMessage());
        }
    }


   public ArrayList<Integer> ghetHeightandWithOfScreen()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        ArrayList<Integer> heightWidthAr=new ArrayList<>();
        heightWidthAr.add(displayMetrics.heightPixels);
        heightWidthAr.add(displayMetrics.widthPixels);

        return heightWidthAr;
    }

    public void logout()
    {
        savePreferences.savePreferencesData(this, "0",DefaultConstants.login_detail);
        savePreferences.savePreferencesData(BaseActivity.this,"false", UtilClass.isLogin);
        Intent intent = new Intent(BaseActivity.this, SplashScreen.class);
        startActivity(intent);
        finishAffinity();

    }


    public  void unauthorizedAccess(JSONObject data)
    {
        try {
            if(data.getString("code").equalsIgnoreCase("410"))
            {
                logout();

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}