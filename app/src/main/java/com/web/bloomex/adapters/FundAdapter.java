package com.web.bloomex.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.fragments.FundFragment;
import com.web.bloomex.fund_withdrawal.WithdrawalFundScreen;
import com.web.bloomex.kyc.BalanceFulledetails;
//import com.web.bloomex.kyc.PersonalDetails;

import org.json.JSONObject;

import java.util.ArrayList;


public class FundAdapter extends RecyclerView.Adapter<FundAdapter.MyViewHolder> {
    private MainActivity ira1;
    private ArrayList<JSONObject> moviesList;
    private FundFragment fundFragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_currency_name, tv_balance;

        LinearLayout ll_fund_list_row;
        ImageView img_currencyicon;
        ImageView ic_more;

        public MyViewHolder(View view) {
            super(view);


            txt_currency_name = view.findViewById(R.id.txt_currency_name);
            tv_balance = view.findViewById(R.id.tv_balance);
            ll_fund_list_row = view.findViewById(R.id.ll_fund_list_row);
            img_currencyicon = view.findViewById(R.id.img_currencyicon);
            ic_more = view.findViewById(R.id.ic_more);



        }
    }


    public FundAdapter(ArrayList<JSONObject> moviesList, MainActivity mainActivity, FundFragment fundFragment) {
        this.moviesList = moviesList;
        this.ira1 = mainActivity;
        this.fundFragment = fundFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fund_adapter_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            JSONObject dataObj = moviesList.get(position);

            holder.txt_currency_name.setText(dataObj.getString("symbol"));
            holder.tv_balance.setText(dataObj.getString("available_balance"));
            showImage(dataObj.getString("icon"), holder.img_currencyicon);

            holder.ic_more.setTag(dataObj);
            holder.ic_more.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    try {
                        PopupWindow popupwindow_obj = popupDisplay(v.getTag().toString());
                        popupwindow_obj.setBackgroundDrawable(new ColorDrawable(
                                android.graphics.Color.TRANSPARENT));
                        popupwindow_obj.showAsDropDown(v, -10, 5);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
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
            public void run()
            {
                Glide.with(ira1)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                        .into(header_img);
            }
        });
    }
    public PopupWindow popupDisplay(String data)
    {

        final PopupWindow popupWindow = new PopupWindow(ira1);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) ira1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.fund_item_popup_menu, null);

        LinearLayout depositLLItem = view.findViewById(R.id.depositLL);
        LinearLayout withdrawalLLItem = view.findViewById(R.id.withdrawalLL);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setContentView(view);

        withdrawalLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ira1, WithdrawalFundScreen.class);
                intent.putExtra("data", data);
                ira1.startActivityForResult(intent,1003);
                popupWindow.dismiss();
            }
        });

        depositLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ira1, BalanceFulledetails.class);
                intent.putExtra("data", data);
                ira1.startActivity(intent);
                popupWindow.dismiss();
            }
        });


        return popupWindow;
    }
}