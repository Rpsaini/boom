package com.web.bloomex.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.R;
import com.web.bloomex.fiatdepositwithdraw.ShowFiatCurrencyDepositWithdraw;

import org.json.JSONArray;
import org.json.JSONObject;


public class BankDetailsAdapters extends RecyclerView.Adapter<BankDetailsAdapters.MyViewHolder> {
    private ShowFiatCurrencyDepositWithdraw ira1;
    private JSONArray moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private  TextView txt_account_holder_name,txt_bank_name,txt_account_number,txt_ifsc;
        private LinearLayout ll_deposit;


        public MyViewHolder(View view)
        {
            super(view);
            txt_account_holder_name = view.findViewById(R.id.txt_account_holder_name);
            txt_bank_name = view.findViewById(R.id.txt_bank_name);
            txt_account_number = view.findViewById(R.id.txt_account_number);
            txt_ifsc = view.findViewById(R.id.txt_ifsc);
            ll_deposit = view.findViewById(R.id.ll_deposit);
        }
    }


    public BankDetailsAdapters(ShowFiatCurrencyDepositWithdraw showFiatCurrencyDepositWithdraw,JSONArray dataAr) {
        this.moviesList = dataAr;
        this.ira1 = showFiatCurrencyDepositWithdraw;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try
         {
            JSONObject dataObj = moviesList.getJSONObject(position);
            dataObj.getString("bank_branch");
            dataObj.getString("branch_number");
            dataObj.getString("account_type");
            dataObj.getString("id");

            holder.txt_account_holder_name.setText(dataObj.getString("account_name"));
            holder.txt_account_number.setText(dataObj.getString("account_no"));
            holder.txt_bank_name.setText(dataObj.getString("bank_name"));
            holder.txt_ifsc.setText(dataObj.getString("ifsc_code"));
         }
        catch(Exception e)
        {
            e.printStackTrace();
           }
         }

    @Override
    public int getItemCount() {
        return moviesList.length();
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