package com.web.bloomex.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.R;
import com.web.bloomex.orderpackage.OpenOrderFragment;
import com.web.bloomex.pairdetailfragments.PairOpenOrderFragment;


public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
    private AppCompatActivity ira1;
    private ArrayList<JSONObject> moviesList;
    private String type="";
    private Fragment fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_cancel,txt_amount,txt_price,txt_date,txt_side;
        LinearLayout ll_open_order_list_row;
        private TextView txt_subpair,txt_mainpar,txt_order_status;

        public MyViewHolder(View view)
        {
            super(view);

            ll_open_order_list_row=view.findViewById(R.id.ll_open_order_list_row);
            txt_cancel=view.findViewById(R.id.txt_cancel);
            txt_subpair=view.findViewById(R.id.txt_subpair);
            txt_mainpar=view.findViewById(R.id.txt_mainpar);
            txt_amount=view.findViewById(R.id.txt_amount);
            txt_price=view.findViewById(R.id.txt_price);
            txt_date=view.findViewById(R.id.txt_date);
            txt_cancel=view.findViewById(R.id.txt_cancel);
            txt_side=view.findViewById(R.id.txt_side);
            txt_order_status=view.findViewById(R.id.txt_order_status);
        }
    }


    public OrderDetailsAdapter(ArrayList<JSONObject> moviesList, AppCompatActivity mainActivity,String type,Fragment fragment)
    {
        this.moviesList = moviesList;
        ira1=mainActivity;
        this.type=type;
        this.fragment=fragment;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        try
        {

            JSONObject jsonData=moviesList.get(position);

            if(type.equalsIgnoreCase("history"))
            {
                holder.txt_cancel.setVisibility(View.GONE);
                holder.txt_order_status.setText(jsonData.getString("order_state"));
                holder.txt_order_status.setVisibility(View.VISIBLE);
                if(jsonData.getString("order_state").equalsIgnoreCase("cancel"))
                {
                    holder.txt_order_status.setTextColor(ira1.getResources().getColor(R.color.darkRed));
                }
                else
                {
                    holder.txt_order_status.setTextColor(ira1.getResources().getColor(R.color.greencolor));
                }

            }

            String ar[]=jsonData.getString("pair").split("\\/");

            holder.txt_subpair.setText(ar[0]);
            holder.txt_mainpar.setText("/"+ar[1]);

            holder.txt_amount.setText(jsonData.getString("volume"));
            holder.txt_price.setText("("+jsonData.getString("price")+")");
            holder.txt_date.setText(jsonData.getString("placed_on"));
            holder.txt_side.setText(jsonData.getString("side"));


            if(jsonData.getString("side").equalsIgnoreCase("buy"))
            {
               // holder.txt_side.setTextColor(ira1.getResources().getColor(R.color.greencolor));
                holder.txt_amount.setTextColor(ira1.getResources().getColor(R.color.greencolor));
            }
            else
            {
              //  holder.txt_side.setTextColor(ira1.getResources().getColor(R.color.darkRed));
                holder.txt_amount.setTextColor(ira1.getResources().getColor(R.color.darkRed));
            }

           holder.txt_cancel.setTag(position);
           holder.txt_cancel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {
                 if(fragment instanceof OpenOrderFragment)
                 {
                     OpenOrderFragment openOrderFragment=(OpenOrderFragment)fragment;
                     openOrderFragment.cancelOrder(Integer.parseInt(v.getTag()+""));
                 }
                 else if(fragment instanceof PairOpenOrderFragment)
                 {
                     PairOpenOrderFragment openOrderFragment=(PairOpenOrderFragment)fragment;
                     openOrderFragment.cancelOrder(Integer.parseInt(v.getTag()+""));
                 }

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



}

