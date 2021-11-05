package com.web.bloomex.pairdetailfragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.dialogsnpickers.SimpleDialog;
import com.app.vollycommunicationlib.CallBack;
import com.app.vollycommunicationlib.ServerHandler;
import com.web.bloomex.BaseActivity;
import com.web.bloomex.DefaultConstants;
import com.web.bloomex.R;
import com.web.bloomex.allorders.AllOrdersFrag;
import com.web.bloomex.communication.SocketHandlers;
import com.web.bloomex.trades.TradesFrag;
import com.web.bloomex.utilpackage.UtilClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.emitter.Emitter;

public class PairDetailView extends BaseActivity
{
    //sell_price ,buy_price
    public String pair_id = "", pair_name = "", volume = "", change = "", buy_price = "", sell_price = "";
    public String mainPair = "", sub_pair = "", joinedPair = "";
    public TextView txt_price;
    SocketHandlers socketHandlers;
    private Fragment commonFragent;
    private TextView orderConfirm;

    //Added Pair details
    private TextView txt_main_pair, txt_sub_pair, txt_change;
    RelativeLayout rr_change;
    ImageView img_arrow;
    private String isLimitOrMarket = "2"; //1 for market 2 for limit


    private EditText ed_at_price ,ed_amount,ed_total_amount;
    private TextView at_priceinrTV,at_amountcoinTV,tota_coin2TV;
    public NumberFormat formatter = new DecimalFormat("#0.0000");
    public double buyBalance = 0;
    public double sellBalance = 0;

    private boolean is_ed_at_price_focus=false,is_ed_amount_focus=false,is_ed_total_amount_focus=false;

    public ArrayList<JSONObject> topBuyPrice=new ArrayList<>();
    public ArrayList<JSONObject> topSellPrice=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_detail_view);
        getSupportActionBar().hide();
        socketHandlers = new SocketHandlers();
        socketHandlers.createConnection();

        initiateObj();
        init();
        bottomNavigation();
        allOrders();
    }

    void init()
    {
        try
            {
            txt_main_pair = findViewById(R.id.txt_main_pair);
            txt_sub_pair = findViewById(R.id.txt_sub_pair);
            txt_price = findViewById(R.id.txt_price);
            rr_change = findViewById(R.id.rr_change);
            img_arrow = findViewById(R.id.img_arrow);
            txt_change = findViewById(R.id.txt_change);


            String pairData = getIntent().getStringExtra(DefaultConstants.pair_data);
            JSONObject jsonObject = new JSONObject(pairData);

            pair_id = jsonObject.getString("pair_id");
            pair_name = jsonObject.getString("symbol");

            volume = jsonObject.getString("volume");
            change = jsonObject.getString("change");

            initRate(change, jsonObject.getString("buy_price"), jsonObject.getString("sell_price"));

            findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

           } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bottomNavigation() {
        final TextView txt_allorder = findViewById(R.id.txt_allorder);
        final TextView text_buy_sell = findViewById(R.id.text_buy_sell);
        final TextView txt_chart = findViewById(R.id.txt_chart);
        final TextView txt_openOrders = findViewById(R.id.txt_openOrders);
        final TextView txt_trades = findViewById(R.id.txt_trades);


        if (txt_allorder != null) {
            txt_allorder.setTextColor(getResources().getColor(R.color.grey_dark));
            text_buy_sell.setTextColor(getResources().getColor(R.color.grey_dark));
            txt_chart.setTextColor(getResources().getColor(R.color.grey_dark));
            txt_openOrders.setTextColor(getResources().getColor(R.color.black));
            txt_trades.setTextColor(getResources().getColor(R.color.grey_dark));

            txt_allorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_allorder.setTextColor(getResources().getColor(R.color.black));
                    text_buy_sell.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_chart.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_openOrders.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_trades.setTextColor(getResources().getColor(R.color.grey_dark));
                    myOrders();
                }
            });

            text_buy_sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buysell();
                }
            });

            txt_chart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_allorder.setTextColor(getResources().getColor(R.color.grey_dark));
                    text_buy_sell.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_chart.setTextColor(getResources().getColor(R.color.black));
                    txt_openOrders.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_trades.setTextColor(getResources().getColor(R.color.grey_dark));
                    chartFrag();
                }
            });

            txt_openOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_allorder.setTextColor(getResources().getColor(R.color.grey_dark));
                    text_buy_sell.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_chart.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_openOrders.setTextColor(getResources().getColor(R.color.black));
                    txt_trades.setTextColor(getResources().getColor(R.color.grey_dark));
                    allOrders();
                }
            });

            txt_trades.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_allorder.setTextColor(getResources().getColor(R.color.grey_dark));
                    text_buy_sell.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_chart.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_openOrders.setTextColor(getResources().getColor(R.color.grey_dark));
                    txt_trades.setTextColor(getResources().getColor(R.color.black));
                    trades();
                }
            });
        }
    }

    private void myOrders() {
        PairOrderFragment allorderfrg = new PairOrderFragment();
        Bundle args = new Bundle();
        args.putString(DefaultConstants.pair_id, pair_id);
        allorderfrg.setArguments(args);
        replaceMainFragment(allorderfrg, "allorders");
        commonFragent = allorderfrg;
    }

    private void buysell() {

        buysellDialog();

    }

    private void chartFrag()
       {
        ChartFragment chartFrg = new ChartFragment();
        Bundle args = new Bundle();
        chartFrg.setArguments(args);
        replaceMainFragment(chartFrg, "allorders");
        commonFragent = chartFrg;
       }


    private void allOrders() {
        AllOrdersFrag frg = new AllOrdersFrag();
        Bundle args = new Bundle();
        frg.setArguments(args);
        replaceMainFragment(frg, "allorders");
        commonFragent = frg;
    }

    private void trades() {
        TradesFrag frg = new TradesFrag();
        Bundle args = new Bundle();
        frg.setArguments(args);
        replaceMainFragment(frg, "allorders");
        commonFragent = frg;
    }


    private void replaceMainFragment(Fragment upcoming, String tag) {
        FragmentTransaction ft_main = getSupportFragmentManager().beginTransaction();
        ft_main.replace(R.id.fragment_container, upcoming);
        ft_main.commit();
    }


    public void initRate(String changenew, String currentBuyPrice1, String sellPrice1) {

        buy_price = currentBuyPrice1.replace(",","");
        sell_price = sellPrice1.replace(",","");
        change = changenew;

        //  double dChange = Double.parseDouble(change);
        if (changenew.contains("+")) {
            rr_change.setBackgroundResource(R.drawable.green_drawable);
            img_arrow.setRotation(270);
        } else if (changenew.contains("-")) {
            rr_change.setBackgroundResource(R.drawable.round_corner_drawable);
            img_arrow.setRotation(90);
        } else {
            rr_change.setBackgroundResource(R.drawable.green_drawable);
            img_arrow.setRotation(270);
        }
        txt_change.setText(change);


        String ar[] = pair_name.split("\\/");
        txt_sub_pair.setText("/" + ar[1]);
        txt_main_pair.setText(ar[0]);

        mainPair = ar[0];
        sub_pair = ar[1];

        joinedPair = mainPair + "-" + sub_pair;

        if (str_side.equalsIgnoreCase("buy")) {
            txt_price.setText(sell_price);
            txt_price.setTextColor(getResources().getColor(R.color.greencolor));
        } else {
            txt_price.setText(buy_price);
            txt_price.setTextColor(getResources().getColor(R.color.darkRed));
        }


        if(buySellDialog!=null&&buySellDialog.isShowing())
        {
            at_priceinrTV.setText(sub_pair);
            at_amountcoinTV.setText(mainPair);
            tota_coin2TV.setText(sub_pair);
            if(isLimitOrMarket.equalsIgnoreCase("1"))
             {
                ed_at_price.setEnabled(false);
                ed_total_amount.setEnabled(false);
                setBuySellOrder();
             }
            else
             {
                ed_at_price.setEnabled(true);
                ed_total_amount.setEnabled(true);
            }
        }
    }
    private void setBuySellOrder()
    {

        if (str_side.equalsIgnoreCase("buy"))
        {
            ed_at_price.setText(sell_price.replace(",", ""));
        }
        else
        {
            ed_at_price.setText(buy_price.replace(",", ""));
        }

        //Todo market
        double totalCurrencyNeed=Double.parseDouble(ed_at_price.getText().toString())*Double.parseDouble(ed_amount.getText().toString());
        ed_total_amount.setText(formatter.format(totalCurrencyNeed)+"");
    }



    @Override
    protected void onResume() {
        super.onResume();
        getDataOfPairs();
    }

    private void getDataOfPairs() {
        socketHandlers.socket.off("broadcast_sent_client");
        socketHandlers.socket.on("broadcast_sent_client", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(args[0] + "");
                            if (jsonObject.has("trades_order")) {
                                String pairId = jsonObject.getString("pair_id");
                                if (pair_id.equalsIgnoreCase(pairId)) {
                                    if (commonFragent instanceof TradesFrag)//for trad fragment
                                    {
                                        TradesFrag tradesFrag = (TradesFrag) commonFragent;
                                        tradesFrag.allTradesOrder.clear();
                                        JSONArray da = new JSONArray(jsonObject.getString("trades_order"));
                                        for (int x = 0; x < da.length(); x++) {
                                            JSONArray innerDataAr = da.getJSONArray(x);
                                            String price = innerDataAr.get(0).toString();
                                            String voluem = innerDataAr.get(1).toString();
                                            String date = innerDataAr.get(2).toString();

                                            JSONObject newSocketDataObj = new JSONObject();
                                            newSocketDataObj.put("price", price);
                                            newSocketDataObj.put("modified", date);
                                            newSocketDataObj.put("volume", voluem);

                                            tradesFrag.allTradesOrder.add(newSocketDataObj);

                                        }
                                        tradesFrag.init(tradesFrag.allTradesOrder);
                                    }
                                }
                            }
                            if (commonFragent instanceof AllOrdersFrag) {
                                //for buy/sell orders
                                if (jsonObject.has("buy_data") && jsonObject.has("sell_data")) {
                                    String pairId = jsonObject.getString("pair_id");
                                    if (pair_id.equalsIgnoreCase(pairId)) {
                                        JSONArray buyAr = new JSONArray(jsonObject.getString("buy_data"));
                                        JSONArray sellAr = new JSONArray(jsonObject.getString("sell_data"));


                                        AllOrdersFrag openOrderfrg = (AllOrdersFrag) commonFragent;
                                        openOrderfrg.buyAr.clear();
                                        openOrderfrg.sellAr.clear();

                                        double totalBuyVolume = 0, totalsellvolume = 0;
                                        for (int x = 0; x < buyAr.length(); x++) {
                                            JSONObject buyObj = new JSONObject(buyAr.get(x) + "");
                                            JSONObject newbuyObj = new JSONObject();
                                            newbuyObj.put("price", buyObj.getString("0"));
                                            newbuyObj.put("volume", buyObj.getString("1"));
                                            openOrderfrg.buyAr.add(newbuyObj);
                                            totalBuyVolume = Double.parseDouble(buyObj.getString("1").replace(",", "")) + totalBuyVolume;

                                        }
                                        for (int x = 0; x < sellAr.length(); x++) {
                                            JSONObject sellObj = new JSONObject(sellAr.get(x) + "");
                                            JSONObject newsellObj = new JSONObject();
                                            newsellObj.put("price", sellObj.getString("2"));
                                            newsellObj.put("volume", sellObj.getString("1"));
                                            openOrderfrg.sellAr.add(newsellObj);
                                            totalsellvolume = Double.parseDouble(sellObj.getString("1").replace(",", "")) + totalsellvolume;

                                        }
                                        openOrderfrg.init(openOrderfrg.sellAr, openOrderfrg.buyAr);

                                    }
                                }
                            }
                            //for pairdetail activity
                            if (jsonObject.has("market_data")) {
                                String pairId = jsonObject.getString("pair_id");
                                if (pair_id.equalsIgnoreCase(pairId)) {
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("market_data"));

                                    String buy_market = jsonObject1.getString("buy_market");
                                    String sell_market = jsonObject1.getString("sell_market");
                                    change = jsonObject1.getString("change");
                                    initRate(change, buy_market, sell_market);
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }


    //apply order===

    private String pairid = "";
    public String str_side = "buy";
    private Dialog buySellDialog;
    private EditText ed_at_stop_price;
    private TextView  lowest_priceTV;

    private void initbuysell()
    {
        LinearLayout bindingbuyLL = buySellDialog.findViewById(R.id.buy_LL);
        LinearLayout sell_LL = buySellDialog.findViewById(R.id.sell_LL);
        TextView buyTV = buySellDialog.findViewById(R.id.buyTV);
        ImageView buy_topBar = buySellDialog.findViewById(R.id.buy_topBar);
        ImageView sell_topBar = buySellDialog.findViewById(R.id.sell_topBar);
        TextView sellTV = buySellDialog.findViewById(R.id.sellTV);

        ed_at_price = buySellDialog.findViewById(R.id.ed_at_price);
        ed_amount = buySellDialog.findViewById(R.id.ed_amount);
        ed_total_amount = buySellDialog.findViewById(R.id.ed_total_amount);

        at_priceinrTV = buySellDialog.findViewById(R.id.inrTV);
        at_amountcoinTV = buySellDialog.findViewById(R.id.coinTV);
        tota_coin2TV = buySellDialog.findViewById(R.id.coin2TV);
        tota_coin2TV = buySellDialog.findViewById(R.id.coin2TV);

        ed_at_stop_price=buySellDialog.findViewById(R.id.ed_at_stop_price);
         lowest_priceTV = buySellDialog.findViewById(R.id.lowest_priceTV);
        TextView buyBTCBT = buySellDialog.findViewById(R.id.buyBTCBT);


        buyBTCBT.setText("Buy "+mainPair);
        setBuySellOrder();

        lowest_priceTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        try {
                            animate(lowest_priceTV);
                            JSONObject dataObj=topSellPrice.get(topSellPrice.size()-1);
                            double price=Double.parseDouble(dataObj.getString("price").replace(",",""));
                            ed_at_price.setText(formatter.format(price)+"");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

        bindingbuyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_side = "buy";
                //initRate(change, buy_price, sell_price);
                buy_topBar.setBackgroundResource(R.drawable.ic_select_buy);
                bindingbuyLL.setBackgroundColor(getResources().getColor(R.color.white));
                buyTV.setTextColor(getResources().getColor(R.color.text_black_color));

                sell_topBar.setVisibility(View.GONE);
                sell_LL.setBackgroundResource(R.drawable.ic_disellect_buy_sell);
                sellTV.setTextColor(getResources().getColor(R.color.text_hint_color));

                buy_topBar.setVisibility(View.VISIBLE);
                bindingbuyLL.setVisibility(View.VISIBLE);
                lowest_priceTV.setText(getResources().getString(R.string.lowest_pric));
                lowest_priceTV.setTextColor(getResources().getColor(R.color.green));
                buyBTCBT.setBackgroundTintList(ContextCompat.getColorStateList(PairDetailView.this, R.color.greencolor));

                buyBTCBT.setText("Buy "+mainPair);
                clearFields();

                lowest_priceTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        try {
                            animate(lowest_priceTV);
                            JSONObject dataObj=topSellPrice.get(topSellPrice.size()-1);
                            double price=Double.parseDouble(dataObj.getString("price").replace(",",""));
                            ed_at_price.setText(formatter.format(price)+"");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });


        sell_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
                {
                str_side = "sell";
                sell_topBar.setBackgroundResource(R.drawable.ic_select_sell);
                sell_LL.setBackgroundColor(getResources().getColor(R.color.white));
                sellTV.setTextColor(getResources().getColor(R.color.text_black_color));
                buy_topBar.setVisibility(View.INVISIBLE);
                sell_topBar.setVisibility(View.VISIBLE);
                sell_topBar.setBackgroundResource(R.drawable.ic_select_sell);
                bindingbuyLL.setBackgroundResource(R.drawable.ic_disellect_buy_sell);
                buyTV.setTextColor(getResources().getColor(R.color.text_hint_color));
                lowest_priceTV.setText(getResources().getString(R.string.highest_pri));
                lowest_priceTV.setTextColor(getResources().getColor(R.color.darkRed));
                buyBTCBT.setText("Sell " + mainPair);
                buyBTCBT.setBackgroundTintList(ContextCompat.getColorStateList(PairDetailView.this, R.color.darkRed));
                clearFields();
                lowest_priceTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        animate(lowest_priceTV);
                        try {
                            JSONObject dataObj=topBuyPrice.get(0);
                            double price=Double.parseDouble(dataObj.getString("price").replace(",",""));
                            ed_at_price.setText(formatter.format(price)+"");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });

        ed_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                is_ed_amount_focus=true;
                is_ed_at_price_focus=false;
                is_ed_total_amount_focus=false;
            }
        });

        ed_at_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                is_ed_amount_focus=false;
                is_ed_at_price_focus=true;
                is_ed_total_amount_focus=false;
            }
        });
        ed_total_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                is_ed_amount_focus=false;
                is_ed_at_price_focus=false;
                is_ed_total_amount_focus=true;
            }
        });

        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
            @Override
            public void afterTextChanged(Editable s)
            {

                if(ed_amount.getText().toString().startsWith(".")&&ed_amount.getText().toString().length()==1)
                {
                    return;
                }
                if(is_ed_amount_focus) {
                    if (s.toString().length() > 0 && ed_at_price.getText().toString().length() > 0) {
                        double amount = Double.parseDouble(s.toString());
                        double atPrice = Double.parseDouble(ed_at_price.getText().toString());
                        double total = amount * atPrice;
                        ed_total_amount.setText(formatter.format(total) + "");
                    } else {

                         ed_total_amount.setText("0");
                        ed_at_price.setHint("0");
                        }

                    }

            }
        });






        if(isLimitOrMarket.equalsIgnoreCase("2"))
        {
            ed_at_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                    if(ed_at_price.getText().toString().startsWith("."))
                    {
                        return;
                    }

                    if(is_ed_at_price_focus) {
                        if (s.length() > 0) {
                            double totalCoin = Double.parseDouble(buy_price) / Double.parseDouble(s.toString());
                            String coinValue = formatter.format(totalCoin);
                            ed_amount.setText(coinValue + "");
                        } else {
                            ed_amount.setHint("0");
                            ed_amount.setText(buy_price);
                        }
                    }
                }
            });


            ed_total_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(ed_total_amount.getText().toString().startsWith("."))
                    {
                        return;
                    }

                    if(is_ed_total_amount_focus)
                    {
                        if (s.length() > 0 && ed_at_price.getText().toString().length() > 0)
                        {
                            double price = Double.parseDouble(s.toString())/Double.parseDouble(ed_at_price.getText().toString()) ;
                            String coinValue = formatter.format(price);
                            ed_amount.setText(coinValue + "");

                        }
                    }
                }
            });
        }
        buyBTCBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateOrderPrice();
            }
        });

    }

    private void animate(View view)
    {
        view.setAlpha(.5f);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setAlpha(1f);
            }
        }, 300);
    }

    private void calculateOrderPrice() {
        if (validationRule.checkEmptyString(ed_amount) == 0) {
            alertDialogs.alertDialog(this, getResources().getString(R.string.Response), "Enter " + pair_name.split("/")[0] + " amount.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {
                    //placeorder_slider.resetSlider();
                }
            });
            return;
        }

        if (validationRule.checkEmptyString(ed_amount) == 0) {
            alertDialogs.alertDialog(this, getResources().getString(R.string.Response), "Enter " + pair_name.split("/")[0] + " Quantity.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {
                    //placeorder_slider.resetSlider();
                }
            });
            return;
        }

        if(isLimitOrMarket.equalsIgnoreCase("3"))
        {
            if (validationRule.checkEmptyString(ed_at_stop_price) == 0) {
                alertDialogs.alertDialog(this, getResources().getString(R.string.Response), "Enter Stop Limit Price.", getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }
        }

        Map<String, String> m = new HashMap<>();
        m.put("isherder", "true");
        if (isLimitOrMarket.equalsIgnoreCase("1")) {
            m.put("order_type", "market");
        } else {
            m.put("order_type", "limit");
        }

        m.put("pair_id", pair_id);
        m.put("side", str_side);
        m.put("amount", ed_amount.getText().toString());
        m.put("price", ed_at_price.getText().toString());
        m.put("market_type", "exchange");
        m.put("Version", getAppVersion());
        m.put("PlatForm", "android");
        m.put("Timestamp", System.currentTimeMillis() + "");
        m.put(DefaultConstants.r_token, getNewRToken() + "");
        m.put("stop_limit", ed_at_stop_price.getText()+"");



        calCulateOrder(m);
    }

    private void calCulateOrder(Map map) {

        map.put("token", savePreferences.reterivePreference(this, DefaultConstants.token));
        map.put("DeviceToken", getDeviceToken() + "");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-API-KEY", UtilClass.xApiKey);
        headerMap.put("Rtoken", getNewRToken() + "");


        new ServerHandler().sendToServer(this, getApiUrl() + "calculate-order-price", map, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        try {
                            if (obj.has("token")) {
                                savePreferences.savePreferencesData(PairDetailView.this, obj.getString("token"), DefaultConstants.token);
                                savePreferences.savePreferencesData(PairDetailView.this, obj.getString("r_token"), DefaultConstants.r_token);
                            }
                            shoOrderEstimation(obj, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                    //    placeorder_slider.resetSlider();
                        alertDialogs.alertDialog(PairDetailView.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed)
                            {
                                unauthorizedAccess(obj);
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
    private void shoOrderEstimation(JSONObject obj, final Map<String, String> m) {
        try
        {
            showOrderPlacedConfirmDia = new Dialog(this);
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

            orderPrice.setText(obj.getString("price"));
            orderVolume.setText(obj.getString("volume"));
            orderSide.setText(obj.getString("side"));

            if(isLimitOrMarket.equalsIgnoreCase("1"))
            {
                orderType.setText("Market");
            } else {
                orderType.setText("Limit");
            }

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
        headerMap.put("Rtoken", getNewRToken() + "");

        new ServerHandler().sendToServer(this, getApiUrl() + "place-order", m, 0, headerMap, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        sendBroadCastWhaenOrderPlaced(m.get("pair_id"));
                        if (obj.has("token")) {
                            savePreferences.savePreferencesData(PairDetailView.this, obj.getString("token"), DefaultConstants.token);
                            savePreferences.savePreferencesData(PairDetailView.this, obj.getString("r_token"), DefaultConstants.r_token);

                        }
                        alertDialogs.alertDialog(PairDetailView.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                                showOrderPlacedConfirmDia.dismiss();
                                downSourceDestinationView(constraint_buysell, buySellDialog);

                            }
                        });

                    } else {
                        orderConfirm.setEnabled(true);
                        alertDialogs.alertDialog(PairDetailView.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                            @Override
                            public void getDialogEvent(String buttonPressed) {
                                unauthorizedAccess(obj);
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


            socketHandlers.socket.emit("broadcast_sent_server", map + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ConstraintLayout constraint_buysell;
    public void buysellDialog() {
        try
          {
            str_side = "buy";
            SimpleDialog simpleDialog = new SimpleDialog();
            buySellDialog = simpleDialog.simpleDailog(this, R.layout.buy_sell_bottom_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            initbuysell();
            constraint_buysell = buySellDialog.findViewById(R.id.constraint_buysell);
            buySellDialog.setCancelable(true);
            animateUp(constraint_buysell);
            final ConstraintLayout at_stop_price_valueCL=buySellDialog.findViewById(R.id.at_stop_price_valueCL);


            Spinner spiinerOrderType = buySellDialog.findViewById(R.id.limitTV);
            ArrayList<String> typeAr = new ArrayList<>();
            typeAr.add("Market");//1
            typeAr.add("Limit");//2
//            typeAr.add("Stop Limit");//3

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeAr);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            spiinerOrderType.setAdapter(spinnerArrayAdapter);

            spiinerOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0)
                     {
                        at_stop_price_valueCL.setVisibility(View.GONE);
                        isLimitOrMarket = "1";
                         lowest_priceTV.setVisibility(View.INVISIBLE);
                     }
                    else if(position==1)
                    {
                        at_stop_price_valueCL.setVisibility(View.GONE);
                        isLimitOrMarket = "2";//Actual Limit order
                        lowest_priceTV.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        at_stop_price_valueCL.setVisibility(View.VISIBLE);
                        isLimitOrMarket = "3";//Stop limit
                    }
                    initRate(change, buy_price, sell_price);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            percentageCalculation();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private ArrayList<TextView> percentageAR;
    private TextView inrValueTV;
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
           inrValueTV.setText(buyBalance+" "+mainPair);
       }
       else
       {
           inrValueTV.setText(sellBalance+" "+mainPair);
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

                     inrValueTV.setText(formatter.format(buyBalance)+" "+sub_pair);
                     double amountPercentage=buyBalance*25/100;
                     double atPrice=Double.parseDouble(ed_at_price.getText().toString());
                     ed_amount.setText(formatter.format(amountPercentage/atPrice));
                     ed_total_amount.setText(formatter.format(amountPercentage));
                 }
                 else if(x==1)
                 {
                     inrValueTV.setText(formatter.format(buyBalance)+" "+sub_pair);
                     double amountPercentage=buyBalance*50/100;
                     double atPrice=Double.parseDouble(ed_at_price.getText().toString());
                     ed_amount.setText(formatter.format(amountPercentage/atPrice));
                     ed_total_amount.setText(formatter.format(amountPercentage));
                 }
                 else if(x==2)
                 {
                     inrValueTV.setText(formatter.format(buyBalance)+" "+sub_pair);
                     double amountPercentage=buyBalance*75/100;
                     double atPrice=Double.parseDouble(ed_at_price.getText().toString());
                     ed_amount.setText(formatter.format(amountPercentage/atPrice));
                     ed_total_amount.setText(formatter.format(amountPercentage));
                 }
                 else
                 {
                     inrValueTV.setText(formatter.format(buyBalance)+" "+sub_pair);
                     double amountPercentage=buyBalance*100/100;
                     double atPrice=Double.parseDouble(ed_at_price.getText().toString());
                     ed_amount.setText(formatter.format(amountPercentage/atPrice));
                     ed_total_amount.setText(formatter.format(amountPercentage));
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
                        inrValueTV.setText(formatter.format(sellBalance)+" "+mainPair);
                        ed_amount.setText(formatter.format(sellBalance*25/100));
                    }
                    else if(x==1)
                    {
                        inrValueTV.setText(formatter.format(sellBalance)+" "+mainPair);
                        ed_amount.setText(formatter.format(sellBalance*50/100));
                    }
                    else if(x==2)
                    {
                        inrValueTV.setText(formatter.format(sellBalance)+" "+mainPair);
                        ed_amount.setText(formatter.format(sellBalance*75/100));
                    }
                    else
                    {
                        inrValueTV.setText(formatter.format(sellBalance)+" "+mainPair);
                        ed_amount.setText(formatter.format(sellBalance*100/100));
                    }
                }
            }
        }
    }

    private void clearFields()
    {
        if(str_side.equalsIgnoreCase("buy"))
        {
            inrValueTV.setText(formatter.format(buyBalance)+""+sub_pair);
        }
        else
        {
            inrValueTV.setText(formatter.format(sellBalance)+" "+mainPair);
        }

        for(int x=0;x<percentageAR.size();x++)
        {
          percentageAR.get(x).setTextColor(getResources().getColor(R.color.text_hint_color));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        socketHandlers.socket.off("broadcast_sent_client");

    }


}

