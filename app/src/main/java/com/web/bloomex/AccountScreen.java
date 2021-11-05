package com.web.bloomex;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.web.bloomex.utilpackage.UtilClass;

public class AccountScreen extends BaseActivity {
private TextView txt_sceret_key,txt_public_key;
private String public_seeret_key="";
private ImageView qr_image;
private TextView txt_public_email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);
        getSupportActionBar().hide();
        initiateObj();
        init();
        actions();
    }

    private void init()
    {
        txt_sceret_key=findViewById(R.id.txt_sceret_key);
        txt_public_key=findViewById(R.id.txt_public_key);
        txt_public_email=findViewById(R.id.txt_public_email);
        qr_image=findViewById(R.id.qr_image);
        setData();
    }
    private void setData() {
        try
          {
            public_seeret_key = savePreferences.reterivePreference(AccountScreen.this, UtilClass.secretkey).toString() + "-" + savePreferences.reterivePreference(AccountScreen.this, UtilClass.publickey).toString();
            txt_public_key.setText(savePreferences.reterivePreference(AccountScreen.this, UtilClass.publickey).toString());
            txt_sceret_key.setText(savePreferences.reterivePreference(AccountScreen.this, UtilClass.secretkey).toString());
            qr_image.setImageBitmap(TextToImageEncode(public_seeret_key));
            txt_public_email.setText(getRestParamsName(UtilClass.email));
           }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void actions()
    {
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_sceret_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_sceret_key.getText().toString());
            }
        });
        txt_public_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_public_key.getText().toString());
            }
        });
        txt_public_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_public_email.getText().toString());
            }
        });

    }
}