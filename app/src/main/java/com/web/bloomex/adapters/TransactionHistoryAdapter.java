package com.web.bloomex.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.web.bloomex.R;

import org.json.JSONObject;
import java.util.ArrayList;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder>
  {
    private AppCompatActivity ira1;
    private ArrayList<JSONObject> moviesList;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View view)
        {
            super(view);

        }
    }


    public TransactionHistoryAdapter(ArrayList<JSONObject> moviesList, AppCompatActivity mainActivity)
      {
        this.moviesList = moviesList;
        ira1=mainActivity;

      }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        try
        {

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

