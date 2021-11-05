package com.web.bloomex.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.MarketAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommonFragment extends Fragment {

    private View view;
    public CommonFragment() {
        // Required empty public constructor
    }


    public static CommonFragment newInstance(String param1, String param2) {
        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_common, container, false);
        loadData();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {

        }
    }

    private void loadData()
    {
       if(getArguments()!=null)
       {
           try {
               if(HomeFragment.tabsHeaderKeys.size()>0) {
                   int pos = Integer.parseInt(getArguments().getString("pos"));
                   JSONObject headerData = HomeFragment.tabsHeaderKeys.get(pos);
                   String pairName = headerData.getString("pair_name");

                   JSONArray dataAr = HomeFragment.commonMap.get(pairName);
                   init(dataAr, pos);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }

    private void init(JSONArray dataObj,int pos)
    {
        if(dataObj!=null)
        {
            RecyclerView recycler_view_market = view.findViewById(R.id.recycler_view_market);
            RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);
            if (dataObj.length() == 0) {
                relativeLayout.setVisibility(View.VISIBLE);
                recycler_view_market.setVisibility(View.GONE);
            } else {
                relativeLayout.setVisibility(View.GONE);
                recycler_view_market.setVisibility(View.VISIBLE);
            }
            MarketAdapter commonAdapter = new MarketAdapter(dataObj, (MainActivity) getActivity());
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recycler_view_market.setLayoutManager(horizontalLayoutManagaer);
            recycler_view_market.setItemAnimator(new DefaultItemAnimator());
            recycler_view_market.setAdapter(commonAdapter);
            HomeFragment.marketAdapterMap.put(pos, commonAdapter);
        }
    }
}