package com.web.bloomex.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.fiatdepositwithdraw.ShowFiatCurrencyDepositWithdraw;
import com.web.bloomex.fragments.FundFragment;
import com.web.bloomex.fund_withdrawal.WithdrawalFundScreen;

import org.json.JSONObject;

import java.util.ArrayList;


public class FiatCurrenciesAdapter extends RecyclerView.Adapter<FiatCurrenciesAdapter.MyViewHolder> {
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
            ic_more.setVisibility(View.INVISIBLE);


        }
    }


    public FiatCurrenciesAdapter(ArrayList<JSONObject> moviesList, MainActivity mainActivity, FundFragment fundFragment)
    {
        this.moviesList = moviesList;
        this.ira1=mainActivity;
        this.fundFragment=fundFragment;
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

            JSONObject dataObj=moviesList.get(position);

            holder.txt_currency_name.setText(dataObj.getString("symbol"));
            holder.tv_balance.setText(dataObj.getString("available_balance"));

            showImage(dataObj.getString("icon"),holder.img_currencyicon);
            holder.ll_fund_list_row.setTag(dataObj);
            holder.ll_fund_list_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                 {
                    try
                    {
                        JSONObject data = new JSONObject(v.getTag().toString());
                        String symbol=data.getString("symbol");
                        if(symbol.equalsIgnoreCase("INR"))
                         {
                            Intent intent = new Intent(ira1, ShowFiatCurrencyDepositWithdraw.class);
                            intent.putExtra("data", v.getTag() + "");
                            ira1.startActivity(intent);
                         }
                        else if(symbol.equalsIgnoreCase("USD"))
                         {
                             ira1.alertDialogs.alertDialog(ira1, ira1.getResources().getString(R.string.Response), "Currently USD Deposit and Withdraw is disabled. ", ira1.getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                 @Override
                                 public void getDialogEvent(String buttonPressed) {
                                 }
                             });
                         }
                      }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

//            holder.ic_more.setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("ResourceType")
//                @Override
//                public void onClick(View v) {
//                    PopupWindow popupwindow_obj = popupDisplay();
//                    popupwindow_obj.setBackgroundDrawable(new ColorDrawable(
//                            android.graphics.Color.TRANSPARENT));
//                    popupwindow_obj.showAsDropDown(v, -10, 5);
//                }
//            });

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
            public void run() {
                Glide.with(ira1)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(3)))
                        .into(header_img);
            }
        });
    }
    public PopupWindow popupDisplay()
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

      /*  withdrawalLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ira1, WithdrawalFundScreen.class);
                ira1.startActivity(intent);
                popupWindow.dismiss();
            }
        });
     */

        withdrawalLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ira1, WithdrawalFundScreen.class);
                ira1.startActivity(intent);
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }
}