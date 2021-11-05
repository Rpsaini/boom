package com.web.bloomex.pairdetailfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.web.bloomex.DefaultConstants;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.OrderDetailsAdapter;


public class PairHistoryFragment extends Fragment {

private  View view;
private PairDetailView mainActivity;
    public PairHistoryFragment() {
        // Required empty public constructor
    }


    public static PairHistoryFragment newInstance(String param1, String param2) {
        PairHistoryFragment fragment = new PairHistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_history, container, false);
      mainActivity=  (PairDetailView) getActivity();
        init();
        return view;
    }

    private void init()
    {
        RecyclerView recycler_view_market =view.findViewById(R.id.order_history_list_recycler);
        RelativeLayout relativeLayout =view.findViewById(R.id.rr_nodata_view);
        view.findViewById(R.id.txt_view_right).setVisibility(View.GONE);
        if(DefaultConstants.orders_history.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            recycler_view_market.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            recycler_view_market.setVisibility(View.VISIBLE);
        }
        AppCompatActivity appCompatActivity=null;
        if(getActivity() instanceof  MainActivity)
        {
            appCompatActivity=(MainActivity)getActivity();
        }
        else if(getActivity() instanceof PairDetailView)
        {
            appCompatActivity=(PairDetailView)getActivity();
        }
        OrderDetailsAdapter mAdapter = new OrderDetailsAdapter(DefaultConstants.orders_history,appCompatActivity,"history",this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_view_market.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_market.setItemAnimator(new DefaultItemAnimator());
        recycler_view_market.setAdapter(mAdapter);

    }
}