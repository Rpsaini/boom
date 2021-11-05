package com.web.bloomex.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.R;
import com.web.bloomex.pairdetailfragments.PairDetailView;
//import com.web.bloomex.kyc.PersonalDetails;


public class BuySellHillsAdapter extends RecyclerView.Adapter<BuySellHillsAdapter.MyViewHolder> {
    private PairDetailView ira1;
    private int count=0;
    private String type="";


    public class MyViewHolder extends RecyclerView.ViewHolder {

       private View view_line;
        public MyViewHolder(View view)
           {
            super(view);
            view_line=view.findViewById(R.id.view_line);
           }
    }


    public BuySellHillsAdapter(PairDetailView mainActivity,int count,String type) {
        this.count=count;
        this.ira1 = mainActivity;
        this.type=type;
      }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buysell_hills_lines, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            if(type.equalsIgnoreCase("sell"))
            {
               holder.view_line.setBackgroundColor(ira1.getResources().getColor(R.color.sell_line_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return count;
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