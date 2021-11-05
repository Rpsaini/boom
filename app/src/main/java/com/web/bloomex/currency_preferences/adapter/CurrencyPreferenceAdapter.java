package com.web.bloomex.currency_preferences.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.R;
import com.web.bloomex.currency_preferences.CurrencyPreferencesScreen;

import org.json.JSONArray;


public class CurrencyPreferenceAdapter extends RecyclerView.Adapter<CurrencyPreferenceAdapter.MyViewHolder> {
    private CurrencyPreferencesScreen ira1;
    private JSONArray moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private  TextView txt_currency_name;
        private ImageView img_currencyicon,selectIC;
        private LinearLayout ll_list_row;
        private View line;

        public MyViewHolder(View view)
        {            super(view);
            txt_currency_name = view.findViewById(R.id.txt_currency_name);
            img_currencyicon = view.findViewById(R.id.img_currencyicon);

            selectIC = view.findViewById(R.id.selectIC);

            ll_list_row = view.findViewById(R.id.ll_fund_list_row);
            line = view.findViewById(R.id.line);
        }
    }


    public CurrencyPreferenceAdapter(CurrencyPreferencesScreen showFiatCurrencyDepositWithdraw,JSONArray dataAr) {
        this.moviesList = dataAr;
        this.ira1 = showFiatCurrencyDepositWithdraw;

    }
    public CurrencyPreferenceAdapter(CurrencyPreferencesScreen showFiatCurrencyDepositWithdraw) {
        this.ira1 = showFiatCurrencyDepositWithdraw;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_preference_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try
        {
          /*  JSONObject dataObj = moviesList.getJSONObject(position);
            dataObj.getString("bank_branch");
            dataObj.getString("branch_number");
            dataObj.getString("account_type");
            dataObj.getString("id");

      */
           if (position==9){
               holder.line.setVisibility(View.GONE);
           }
           else {
               holder.line.setVisibility(View.VISIBLE);
           }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}