package com.web.bloomex.kyc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.bloomex.R;
import com.web.bloomex.kyc.SelectStateScreen;

import org.json.JSONObject;

import java.util.ArrayList;


public class SelectStateAdapter extends RecyclerView.Adapter<SelectStateAdapter.MyViewHolder> {
    private SelectStateScreen ira1;
    private ArrayList<JSONObject> moviesList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Name;
        ImageView img_countryIcon;
        LinearLayout ll_market_order_list_row;
        CheckBox selectCountryCB;

        public MyViewHolder(View view)
        {
            super(view);
            img_countryIcon = view.findViewById(R.id.img_countryIcon);
            txt_Name = view.findViewById(R.id.txt_Name);

            ll_market_order_list_row = view.findViewById(R.id.ll_market_order_list_row);
            selectCountryCB = view.findViewById(R.id.selectCountryCB);

        }
    }


    public SelectStateAdapter(SelectStateScreen showFiatCurrencyDepositWithdraw, ArrayList<JSONObject> dataAr) {
        this.moviesList = dataAr;
        this.ira1 = showFiatCurrencyDepositWithdraw;

    }
    public SelectStateAdapter(SelectStateScreen showFiatCurrencyDepositWithdraw) {
        this.ira1 = showFiatCurrencyDepositWithdraw;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_country_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       try {
           JSONObject object = moviesList.get(position);
           String countryName=object.getString("name");
                   holder.txt_Name.setText(countryName);
       holder.selectCountryCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked){
                  Intent returnIntent = new Intent();
                  returnIntent.putExtra("_name",countryName);
                  ira1.setResult(Activity.RESULT_OK,returnIntent);
                  ira1.finish();
              }
          }
      });
        holder.ll_market_order_list_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("_name",countryName);
                ira1.setResult(Activity.RESULT_OK,returnIntent);
                ira1.finish();
            }
        });
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }


    }

    @Override
    public int getItemCount()
    {
      return moviesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void showImage(final String url, final ImageView header_img) {
        ira1.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(ira1)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                        .into(header_img);
            }
        });
    }

}