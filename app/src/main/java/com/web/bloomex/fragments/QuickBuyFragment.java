package com.web.bloomex.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.MainActivity;
import com.web.bloomex.R;
import com.web.bloomex.adapters.QuickBuyAdapter;
import com.web.bloomex.communication.SocketHandlers;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuickBuyFragment extends Fragment {
   private View view;
   private MainActivity mainActivity;
   public QuickBuyFragment() {
   }
    public static QuickBuyFragment newInstance(String param1, String param2)
     {
        QuickBuyFragment fragment = new QuickBuyFragment();
        return fragment;
     }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_quick_buy, container, false);
        mainActivity= (MainActivity) getActivity();
    //  getQuickData();
        getQuickCurrency();
        return view;
    }


    private void showQuickBuyCurrency(JSONArray quickAr)
    {
        RecyclerView fiat_balance_recyclerview=view.findViewById(R.id.quick_buy_coin_recycler);
        RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);
        if (quickAr.length() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            fiat_balance_recyclerview.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            fiat_balance_recyclerview.setVisibility(View.VISIBLE);
        }


        QuickBuyAdapter mAdapter = new QuickBuyAdapter(quickAr,(MainActivity) getActivity(),this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fiat_balance_recyclerview.setLayoutManager(horizontalLayoutManagaer);
        fiat_balance_recyclerview.setItemAnimator(new DefaultItemAnimator());
        fiat_balance_recyclerview.setAdapter(mAdapter);
    }


    private void getQuickCurrency() {

        Map<String, String> m = new HashMap<>();
        m.put("token", mainActivity.savePreferences.reterivePreference(mainActivity, DefaultConstants.token) + "");
        m.put("DeviceToken", mainActivity.getDeviceToken() + "");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", mainActivity.getNewRToken() + "");
        new ServerHandler().sendToServer(mainActivity, mainActivity.getApiUrl() + "get-quick-buy-sell", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status"))
                    {
                        try {
                            if (obj.has("token"))
                            {
                                mainActivity.savePreferences.savePreferencesData(mainActivity, obj.getString("token"), DefaultConstants.token);
                                mainActivity.savePreferences.savePreferencesData(mainActivity, obj.getString("r_token"), DefaultConstants.r_token);
                            }
                            JSONArray pairs_info=obj.getJSONArray("pairs_info");
                            showQuickBuyCurrency(pairs_info);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                                mainActivity.unauthorizedAccess(obj);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    ConstraintLayout constraint_buysell;
    String str_side="buy";
    Dialog buySellDialog;
    private EditText ed_amount;
    String buy_fiat="";

    private ArrayList<TextView> percentageAR;
    private TextView inrValueTV;
    public NumberFormat formatter = new DecimalFormat("#0.0000");
    double buyBalancefiat=0,sellBalanceMainPair=0;
    String mainPair="",sub_pair="";
    private String pair_id="";
    String buy_price,sell_price;
    public void buysellDialog(JSONObject data) {
        try
        {
            buy_fiat=data.getString("term");
            str_side = "buy";
            mainPair=data.getString("base");
            sub_pair=data.getString("term");
            pair_id=data.getString("pair_id");
            buyBalancefiat=Double.parseDouble(data.getString("term_balance"));
            sellBalanceMainPair=Double.parseDouble(data.getString("base_balance"));


            buy_price=data.getString("buy_price");
            sell_price=data.getString("sell_price");




            SimpleDialog simpleDialog = new SimpleDialog();
            buySellDialog = simpleDialog.simpleDailog(mainActivity, R.layout.quick_buy_sell, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);

            constraint_buysell = buySellDialog.findViewById(R.id.constraint_buysell);
            buySellDialog.setCancelable(true);

            mainActivity.animateUp(constraint_buysell);
            ed_amount=buySellDialog.findViewById(R.id.ed_amount);


            LinearLayout bindingbuyLL = buySellDialog.findViewById(R.id.buy_LL);
            LinearLayout sell_LL = buySellDialog.findViewById(R.id.sell_LL);
            ImageView buy_topBar = buySellDialog.findViewById(R.id.buy_topBar);
            ImageView sell_topBar = buySellDialog.findViewById(R.id.sell_topBar);
            TextView buyBTCBT = buySellDialog.findViewById(R.id.buyBTCBT);
            TextView coinTV = buySellDialog.findViewById(R.id.coinTV);
            TextView txt_currencyname = buySellDialog.findViewById(R.id.txt_currencyname);
            TextView txt_longname = buySellDialog.findViewById(R.id.txt_longname);
            coinTV.setText(buy_fiat);
            ed_amount.setText(buyBalancefiat+"");
            buyBTCBT.setText("Buy "+mainPair);
            txt_currencyname.setText(mainPair);
            txt_longname.setText("("+data.getString("base_name")+")");


            bindingbuyLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    str_side = "buy";
                    ed_amount.setHint("0");
                    ed_amount.setText(buyBalancefiat+"");
                    coinTV.setText(buy_fiat);
                    buy_topBar.setBackgroundResource(R.drawable.ic_select_buy);
                    bindingbuyLL.setBackgroundColor(getResources().getColor(R.color.white));
                    sell_topBar.setVisibility(View.GONE);
                    sell_LL.setBackgroundResource(R.drawable.ic_disellect_buy_sell);
                    buy_topBar.setVisibility(View.VISIBLE);
                    bindingbuyLL.setVisibility(View.VISIBLE);
                    buyBTCBT.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity, R.color.greencolor));
                    buyBTCBT.setText("Buy "+mainPair);
                    clearFields();
                }
            });


            sell_LL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str_side = "sell";
                    ed_amount.setHint("0");
                    ed_amount.setText(sellBalanceMainPair+"");
                    coinTV.setText(mainPair);
                    sell_topBar.setBackgroundResource(R.drawable.ic_select_sell);
                    sell_LL.setBackgroundColor(getResources().getColor(R.color.white));
                    buy_topBar.setVisibility(View.INVISIBLE);
                    sell_topBar.setVisibility(View.VISIBLE);
                    sell_topBar.setBackgroundResource(R.drawable.ic_select_sell);
                    bindingbuyLL.setBackgroundResource(R.drawable.ic_disellect_buy_sell);
                    buyBTCBT.setText("Sell " + mainPair);
                    buyBTCBT.setBackgroundTintList(ContextCompat.getColorStateList(mainActivity, R.color.darkRed));
                    clearFields();
                }
            });

            buyBTCBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculateOrderPrice();
                }
            });

            buySellDialog.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.downSourceDestinationView(constraint_buysell,buySellDialog);
                }
            });


            percentageCalculation();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void percentageCalculation()
    {
        inrValueTV= buySellDialog.findViewById(R.id.inrValueTV);
        percentageAR=new ArrayList<>();
        percentageAR.add(buySellDialog.findViewById(R.id.tv25));
        percentageAR.add(buySellDialog.findViewById(R.id.tv50));
        percentageAR.add(buySellDialog.findViewById(R.id.tv75));
        percentageAR.add(buySellDialog.findViewById(R.id.tv100));

        if(str_side.equalsIgnoreCase("buy"))
        {
            inrValueTV.setText(buyBalancefiat+" "+buy_fiat);

        }
        else
        {
            inrValueTV.setText(sellBalanceMainPair +mainPair);

        }

        for(int x=0;x<percentageAR.size();x++)
        {
            percentageAR.get(x).setTag(x);
            percentageAR.get(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    performCalculation(Integer.parseInt(v.getTag().toString()));
                }
            });
        }

    }

    private void performCalculation(int percentange)
    {
        if(str_side.equalsIgnoreCase("buy"))
        {

            for(int x=0;x<percentageAR.size();x++)
            {
                percentageAR.get(x).setTextColor(getResources().getColor(R.color.text_hint_color));
                if(percentange==x)
                {
                    percentageAR.get(x).setTextColor(getResources().getColor(R.color.green));
                    if(x==0)
                    {
                        //inrValueTV.setText(formatter.format(buyBalancefiat)+" "+mainPair);
                        double amountPercentage=buyBalancefiat*25/100;
                        ed_amount.setText(formatter.format(amountPercentage));

                    }
                    else if(x==1)
                    {
                       // inrValueTV.setText(formatter.format(buyBalancefiat)+" "+mainPair);
                        double amountPercentage=buyBalancefiat*50/100;
                        ed_amount.setText(formatter.format(amountPercentage));

                    }
                    else if(x==2)
                    {
                        //inrValueTV.setText(formatter.format(buyBalancefiat)+" "+mainPair);
                        double amountPercentage=buyBalancefiat*75/100;
                        ed_amount.setText(formatter.format(amountPercentage));

                    }
                    else
                    {
                      //  inrValueTV.setText(formatter.format(buyBalancefiat)+" "+buy_fiat);
                        double amountPercentage=buyBalancefiat*100/100;
                        ed_amount.setText(formatter.format(amountPercentage));

                    }
                }
            }
        }
        else
        {
            for(int x=0;x<percentageAR.size();x++)
            {
                percentageAR.get(x).setTextColor(getResources().getColor(R.color.text_hint_color));
                if(percentange==x)
                {
                    percentageAR.get(x).setTextColor(getResources().getColor(R.color.darkRed));
                    if(x==0)
                    {

                        ed_amount.setText(formatter.format(sellBalanceMainPair*25/100));
                    }
                    else if(x==1)
                    {

                        ed_amount.setText(formatter.format(sellBalanceMainPair*50/100));
                    }
                    else if(x==2)
                    {

                        ed_amount.setText(formatter.format(sellBalanceMainPair*75/100));
                    }
                    else
                    {

                        ed_amount.setText(formatter.format(sellBalanceMainPair*100/100));
                    }
                }
            }
        }
    }

    private void clearFields()
    {
        if(str_side.equalsIgnoreCase("buy"))
        {
            inrValueTV.setText(formatter.format(buyBalancefiat)+""+buy_fiat);
        }
        else
        {
            inrValueTV.setText(formatter.format(sellBalanceMainPair)+" "+mainPair);
        }

        for(int x=0;x<percentageAR.size();x++)
        {
            percentageAR.get(x).setTextColor(getResources().getColor(R.color.text_hint_color));
        }
    }






    private void calculateOrderPrice() {
        if (mainActivity.validationRule.checkEmptyString(ed_amount) == 0) {
            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), "Enter " + mainPair + " amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {
                    //placeorder_slider.resetSlider();
                }
            });
            return;
        }
        if (Double.parseDouble(ed_amount.getText().toString()) == 0)
        {
            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), "Enter " + mainPair + " amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {
                    //placeorder_slider.resetSlider();
                }
            });
            return;
        }


        Map<String, String> m = new HashMap<>();
        if(str_side.equalsIgnoreCase("buy"))
        {


            String buy=formatter.format(Double.parseDouble(ed_amount.getText().toString())/Double.parseDouble(sell_price));
            m.put("price", buy_price);
            m.put("amount", buy);
        }
        else
        {
            m.put("price", sell_price);
            m.put("amount", ed_amount.getText().toString());
        }
        m.put("isherder", "true");
        m.put("order_type", "market");
        m.put("pair_id", pair_id);
        m.put("side", str_side);

        m.put("market_type", "exchange");
        m.put("Version", mainActivity.getAppVersion());
        m.put("PlatForm", "android");
        m.put("Timestamp", System.currentTimeMillis() + "");
        m.put(DefaultConstants.r_token, mainActivity.getNewRToken() + "");
        m.put("stop_limit", "");



        calCulateOrder(m);
    }

    private void calCulateOrder(Map map) {

        map.put("token", mainActivity.savePreferences.reterivePreference( mainActivity, DefaultConstants.token));
        map.put("DeviceToken",  mainActivity.getDeviceToken() + "");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken",  mainActivity.getNewRToken() + "");


        new ServerHandler().sendToServer(mainActivity,  mainActivity.getApiUrl() + "calculate-order-price", map, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        try {
                            if (obj.has("token")) {
                                mainActivity.savePreferences.savePreferencesData( mainActivity, obj.getString("token"), DefaultConstants.token);
                                mainActivity.savePreferences.savePreferencesData( mainActivity, obj.getString("r_token"), DefaultConstants.r_token);
                            }
                            shoOrderEstimation(obj, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //    placeorder_slider.resetSlider();
                        mainActivity.alertDialogs.alertDialog( mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed)
                            {
                                mainActivity.unauthorizedAccess(obj);
                            }
                        });

                    }
                } catch (Exception e) {
                    //   placeorder_slider.resetSlider();
                    e.printStackTrace();
                }

            }
        });

    }

    Dialog showOrderPlacedConfirmDia;
    TextView orderConfirm;
    private void shoOrderEstimation(JSONObject obj, final Map<String, String> m) {
        try
        {
            showOrderPlacedConfirmDia = new Dialog( mainActivity);
            showOrderPlacedConfirmDia.setContentView(R.layout.show_order_place_dialog);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = showOrderPlacedConfirmDia.getWindow();
            lp.copyFrom(window.getAttributes());
            showOrderPlacedConfirmDia.setCancelable(true);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            showOrderPlacedConfirmDia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            showOrderPlacedConfirmDia.show();

            TextView orderTotalamount = showOrderPlacedConfirmDia.findViewById(R.id.orderTotalamount);
            TextView orderType = showOrderPlacedConfirmDia.findViewById(R.id.orderType);
            TextView orderPrice = showOrderPlacedConfirmDia.findViewById(R.id.orderPrice);
            TextView orderVolume = showOrderPlacedConfirmDia.findViewById(R.id.orderVolume);
            TextView orderSide = showOrderPlacedConfirmDia.findViewById(R.id.orderSide);


             orderConfirm = showOrderPlacedConfirmDia.findViewById(R.id.orderConfirm);
            TextView orderCancel = showOrderPlacedConfirmDia.findViewById(R.id.orderCancel);


            orderTotalamount.setText(obj.getString("total_price"));
            orderType.setText(obj.getString("type"));
            orderPrice.setText(obj.getString("price"));
            orderVolume.setText(obj.getString("volume"));
            orderSide.setText(obj.getString("side"));



            orderConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderConfirm.setEnabled(false);
                    placeBuySellOrder(m);
                }
            });

            orderCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // placeorder_slider.resetSlider();
                    showOrderPlacedConfirmDia.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void placeBuySellOrder(Map<String, String> m) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", mainActivity.getNewRToken() + "");

        new ServerHandler().sendToServer(mainActivity, mainActivity.getApiUrl() + "place-order", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        sendBroadCastWhaenOrderPlaced(m.get("pair_id"));
                        if (obj.has("token")) {
                            mainActivity.savePreferences.savePreferencesData(mainActivity, obj.getString("token"), DefaultConstants.token);
                            mainActivity.savePreferences.savePreferencesData(mainActivity, obj.getString("r_token"), DefaultConstants.r_token);
                          }
                        mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                                showOrderPlacedConfirmDia.dismiss();
                                mainActivity.downSourceDestinationView(constraint_buysell, buySellDialog);
                            }
                        });
                        getQuickCurrency();

                    } else {
                        orderConfirm.setEnabled(true);
                        mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                                mainActivity.unauthorizedAccess(obj);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void sendBroadCastWhaenOrderPlaced(String pairId) {
        try {
            JSONObject map = new JSONObject();
            map.put("pair_id", pairId);
            map.put("type", "app");

            new SocketHandlers().socket.emit("broadcast_sent_server", map + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}