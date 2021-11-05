package com.web.bloomex.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;


import com.web.bloomex.R;
import com.web.bloomex.pairdetailfragments.PairDetailView;

import java.util.ArrayList;

public class PriceVolumeAdapter extends RecyclerView.Adapter<PriceVolumeAdapter.MyViewHolder> {
    private PairDetailView ira1;
    private ArrayList<JSONObject> moviesList;
    private String type = "";
    private Context context = null;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_volume, txt_price;
        LinearLayout ll_orders_layout;
        private RecyclerView buy_sell_lines_recycler;
        RelativeLayout rr_buysell;


        public MyViewHolder(View view) {
            super(view);
            txt_volume = view.findViewById(R.id.txt_volume);
            txt_price = view.findViewById(R.id.txt_price);
            ll_orders_layout = view.findViewById(R.id.ll_orders_layout);
            buy_sell_lines_recycler = view.findViewById(R.id.buy_sell_lines_recycler);
            rr_buysell = view.findViewById(R.id.rr_buysell);

        }
    }


    public PriceVolumeAdapter(Context context, ArrayList<JSONObject> moviesList, PairDetailView mainActivity, String type) {
        this.moviesList = moviesList;
        this.type = type;
        ira1 = mainActivity;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.price_volume_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {


            JSONObject object = moviesList.get(position);

//            double buyVolSum=0,listSum=0;
//
//                buyVolSum=Double.parseDouble(object.getString("volume").replace(",",""))+buyVolSum;
//               listSum=Double.parseDouble(object.getString("price").replace(",",""))+listSum;
//
//
//            double percentage=(buyVolSum*100)/listSum;
//            System.out.println("percentage==="+percentage);


            if(type.equalsIgnoreCase("buy"))
              {
                holder.txt_price.setText(object.getString("price"));
                holder.txt_volume.setText(object.getString("volume"));
                holder.txt_price.setTextColor(ira1.getResources().getColor(R.color.greencolor));
                holder.txt_volume.setTextColor(ira1.getResources().getColor(R.color.grey_dark));
                holder.txt_price.setTypeface(Typeface.DEFAULT_BOLD);
                holder.txt_volume.setTypeface(Typeface.DEFAULT);


              }
            else
                {
                holder.txt_volume.setText(object.getString("price"));
                holder.txt_price.setText(object.getString("volume"));
                holder.txt_volume.setTextColor(ira1.getResources().getColor(R.color.darkRed));
                holder.txt_price.setTextColor(ira1.getResources().getColor(R.color.grey_dark));
                holder.txt_price.setTypeface(Typeface.DEFAULT);
                holder.txt_volume.setTypeface(Typeface.DEFAULT_BOLD);
                }

            holder.rr_buysell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ira1.buysellDialog();
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


    private void setBuSellHorizonalLines(RecyclerView buy_sell_lines_recycler, int count,String type)
    {
        BuySellHillsAdapter linesViewAdapter = new BuySellHillsAdapter(ira1, count,type);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ira1, LinearLayoutManager.HORIZONTAL, false);
        buy_sell_lines_recycler.setLayoutManager(horizontalLayoutManagaer);
        buy_sell_lines_recycler.setItemAnimator(new DefaultItemAnimator());
        if(type.equalsIgnoreCase("buy")) {
            horizontalLayoutManagaer.setReverseLayout(true);
        }
        buy_sell_lines_recycler.setAdapter(linesViewAdapter);
    }


}