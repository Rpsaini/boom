package com.web.bloomex.pairdetailfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.web.bloomex.R;
import com.web.bloomex.trades.TradesFrag;

import java.util.ArrayList;

public class ChartFragment extends Fragment {
    private ProgressBar viewprogressbar;
    private View view;
    private PairDetailView pairDetailView;

  // private CandleStickChart candleStickChart;
 // private PairDetailView pairDetailView;
 // private TextView chartTitle;

    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        trades();


        ArrayList<Integer> HeightAr= ((PairDetailView)getActivity()).ghetHeightandWithOfScreen();
        pairDetailView = (PairDetailView)getActivity();
        float height=HeightAr.get(0);
        float width=HeightAr.get(1);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        float density=metrics.density;
         height=height/density;
         width= width/density;


        String url = pairDetailView.getApiUrl() + "chart-data?pair=" + pairDetailView.joinedPair+"&height="+(height-150)+"&width="+width;



        WebView mywebview = (WebView) view.findViewById(R.id.webview);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mywebview.getSettings().setSupportZoom(true);
        mywebview.getSettings().setBuiltInZoomControls(true);
        mywebview.getSettings().setDisplayZoomControls(false);

        webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/80.0.3987.163 Chrome/80.0.3987.163 Safari/537.36");
        mywebview.loadUrl(url);

        viewprogressbar = view.findViewById(R.id.viewprogressbar);
        mywebview.setWebViewClient(new WebViewController());
        return view;
    }


    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            viewprogressbar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            viewprogressbar.setVisibility(View.GONE);
            super.onPageFinished(view, url);

        }


        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
        }
    }


    private void replaceMainFragment(Fragment upcoming, String tag) {
        FragmentTransaction ft_main = getActivity().getSupportFragmentManager().beginTransaction();
        ft_main.replace(R.id.ll_container, upcoming);
        ft_main.commit();
    }


    private void trades()
    {
        TradesFrag frg = new TradesFrag();
        Bundle args = new Bundle();
        frg.setArguments(args);
        replaceMainFragment(frg, "allorders");

    }
//    private void showChartData() {
//        candleStickChart = view.findViewById(R.id.candle_stick_chart);
//        candleStickChart.setHighlightPerDragEnabled(true);
//        candleStickChart.setDrawBorders(true);
//        candleStickChart.setBorderColor(getResources().getColor(R.color.lightGray));
//
//        YAxis yAxis = candleStickChart.getAxisLeft();
//        YAxis rightAxis = candleStickChart.getAxisRight();
//        yAxis.setDrawGridLines(false);//
//        rightAxis.setDrawGridLines(true);//
//        candleStickChart.requestDisallowInterceptTouchEvent(true);
//
//        XAxis xAxis = candleStickChart.getXAxis();
//        xAxis.setDrawGridLines(false);// disable x axis grid lines
//        xAxis.setDrawLabels(true);
//
//        xAxis.setTextColor(R.color.hintTextColor);
//        rightAxis.setTextColor(R.color.hintTextColor);
//        yAxis.setDrawLabels(true);
//        yAxis.setTextColor(R.color.hintTextColor);
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setAvoidFirstLastClipping(true);
//
//
//        Legend l = candleStickChart.getLegend();
//        l.setEnabled(true);
//        l.setTextColor(R.color.dark_blue);
//        setDataSet();
//    }
//
//    private void setDataSet() {
//        CandleDataSet set1 = new CandleDataSet(setCancleData(), "Buy Sell");
//        set1.setColor(Color.rgb(80, 80, 80));
//        set1.setShadowColor(getResources().getColor(R.color.lightGray));
//        set1.setShadowWidth(1f);
//        set1.setDecreasingColor(getResources().getColor(R.color.darkRed));
//        set1.setDecreasingPaintStyle(Paint.Style.FILL);
//
//
//        set1.setIncreasingColor(getResources().getColor(R.color.greencolor));
//        set1.setIncreasingPaintStyle(Paint.Style.FILL);
//        set1.setNeutralColor(R.color.hintTextColor);
//        set1.setDrawValues(false);
//
//
//// create a data object with the datasets
//        CandleData data = new CandleData(set1);
//
//
//// set data
//        candleStickChart.setData(data);
//        candleStickChart.invalidate();
//
//    }
//
//    private JSONObject chartDataObj;
//
//    private ArrayList<CandleEntry> setCancleData() {
//
//        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<CandleEntry>();
//        if (chartDataObj != null) {
//            try {
//
//                // String data = "{\"s\":\"ok\",\"t\":[1563109760,1563112186,1563112804,1563115300,1563118614,1563121994,1563127686,1563132155,1563134446,1563136497,1563144229,1563146998,1563147285,1563155010,1563173693,1563180898,1563181705,1563189177,1563194644,1563199122,1563201438,1563203416,1563205966,1563208554,1563211309,1563213711,1563215742,1563221652,1563226036,1563229516,1563230177,1563231610,1563233934,1563242498,1563251721,1563261567,1563265870,1563272028,1563273309,1563276619,1563278794,1563281199,1563282453,1563284334,1563286786,1563289721,1563292623,1563300067,1563306631,1563314749,1563320614,1563324913,1563327536,1563345590,1563351661,1563352864,1563354752,1563355895,1563358884,1563360011,1563361413,1563363141,1563365225,1563367239,1563371038,1563376496,1563377907,1563382944,1563385285,1563387362,1563390275,1563432339,1563437911,1563438819,1563441058,1563442614,1563444082,1563445920,1563448277,1563449615,1563451572,1563453088,1563455485,1563456625,1563481980,1563500762,1563502090,1563504086,1563506090,1563507167,1563509180,1563511074,1563512896,1563514343,1563516091,1563518209,1563519726,1563521578,1563528608,1563546571,1563560819,1563576243,1563589511,1563604936,1563626648,1563638819,1563640558,1563642456,1563645909,1563648350,1563653648,1563654662,1563657442,1563658781,1563660088,1563662307,1563664043,1563668185,1563670819,1563672683,1563681598,1563693429,1563724048,1563725965,1563732382,1563735614,1563740489,1563741667,1563743122,1563745523,1563748541,1563753333,1563754616,1563755650,1563758812,1563759694,1563780153,1563780748,1563800686,1563808932,1563819871,1563821181,1563822139,1563826824,1563827672,1563829991,1563831870,1563833128,1563835485,1563837722,1563842723,1563844090,1563846599,1563848898,1563849066,1563852342,1563853116,1563855007,1563856662,1563860094,1563862091,1563863963,1563866042,1563867063,1563889640,1563896063,1563901541,1563911973,1563912849,1563914265,1563915770,1563919549,1563922344,1563928579,1563932280,1563947098,1563948512,1563953902,1563957095,1563967030,1563988120,1563991009,1563991974,1563999344,1564001447,1564002117,1564007512,1564015116,1564017416,1564018547,1564039438,1564040690,1564047498,1564058212,1564064804,1564098578,1564106602,1564134419,1564157751,1564160442,1564162382,1564164491,1564170986,1564184390,1564194206],\"c\":[60880,60880,61125,62000,60821,60789,61535,61980,61385,60870,60870,59000,59200,59900,59575,59600,59600,61085,61115,62220,61818,60255,63250,63504,63675,63250,63199,64100,64018,63835,63752,63402,63197,62580,62660,62450,62005,60700,60550,61000,60900,61255,59285,58125,55875,57099,55801,55537,55321,55367,54200,54615,54470,55402,54455,53690,53450,55170,54550,55050,55619,56105,56651,56503,56453,56554.5,56802.21,55925,56060.8,57145.54,56850,56328,56645,56285,55960,54380,54399,54210,54201,58125,59320,59245,60500,60050,60540,59940,59050.33,59600,59625,59425,59412,60120,59610,58510,58620,59100,58550,58840,59777,59110,59697.84,59325,59920,60215,60160,61964,61644.1,61747,61700.5,61800.1,62350,61200,61194,61062,61210,61345,60810,60510,60660,60660,60610,60729,59699.8,59598.2,60035.5,60548.2,60387,60450,60623,60413,60633,60328,60420,60732,60465,60589,60130,60170,58830,59189.8,59098.8,58789.3,58702,58887.5,58884,58910,58805,58505,58505,58509,58429,58629,58429,58429,58211,57800.45,56879.25,57603,57260,57215,56975,56922,56910,56990,57420,57390,58614,58220,58800,57202,56665,56106,56150,56060,56010,56444,56788,56476,55980,56320,55733.55,55463.2,55850.2,56122.35,56478,56080,57500,58822,58101,57820,57785,58000,57920,57385,57220,55778,56200,55895,56210.11,56398.5,56118.5,56398.5,55930.2,56550,57195],\"o\":[60880,60600,61125,61900,61125,60789,61535,61980,62165,60870,60870,59000,59200,59900,59575,59600,59600,59300,60850,62220,61818,60565,63100,63950,63702,63250,63204,64100,64018,63835,63752,63584,63321,62580,62660,62450,62005,60700,60700,60920,60900,61255,60000,59250,55900,57099,55998.81,56071.77,55333.45,55367,54200,54615,54470,55805,54455,53690,53450,54050,54550,54849,55439,55925,56225,56811,56224,56554.5,56802.21,56605.44,56060.8,56125,56850,56328,56645,56265,56083,56239,54385,54399,53652,54399,58125,59005,59320,60200,60250,59940,59050.33,59202,59625,59300,59050.33,59300,60650,58810,58921,58985,58810,58680,58670,59110,59697.84,59325,59920,60215,60160,61964,61644.1,61747,61700.5,61800.1,62251,62140,61040,61194,61305,61210,60974,60510,60660,60660,60610,60729,59699.8,59598.2,60035.5,60400.1,60250.43,60450,60610,60413,60323,60328,60287,60732,60465,60256,60000,59908,58830,59189.8,59098.8,58789.3,58702,58887.5,58884,58775.5,58805,58695,58505,58509,58429,58629,58429,58616,58301,58202,57210,57800,57501,57215,56975,56998,56909,56990,57420,58350,58614,58220,58800,58520,57202,56106,56150,56060,56010,56500,56788,56385,55980,56320,55733.55,55463.2,55850.2,56122.35,56478,56197,56559.08,57822,58101,57820,57785,58000,57920,57450,57404.2,55778,56200,55895,56179,56398.5,56118.5,56398.5,55930.2,56550,57195],\"h\":[60880,60880,61125,62000,61125,60789,61535,61980,62165,60870,60870,59000,59200,59900,59575,59600,59600,61085,61115,62220,61818,60565,63250,63950,63702,63250,63204,64100,64018,63835,63752,63835,63321,62580,62660,62450,62005,60700,60930,61000,60900,61255,60000,59250,55900,57099,55998.81,56071.77,55333.45,55367,54200,54615,54470,55805,54455,53690,53450,55170,54550,55170,55920,56105,56651,56811,56453,56554.5,56802.21,56605.44,56060.8,57145.54,56850,56328,56645,56285,56239,56239,54980,54399,54201,58125,59320,59245,60500,60390,60540,59940,59050.33,59600,59625,59425,59412,60120,60650,59014,58921,59100,58844,59100,59777,59110,59697.84,59325,59920,60215,60160,61964,61644.1,61747,61700.5,61800.1,62350,62140,61301,61453,61305,61345,60974,60510,60660,60660,60610,60729,59699.8,59598.2,60035.5,60548.2,60521,60450,60623,60413,60633,60328,60420,60732,60465,60589,60130,60180,58830,59189.8,59098.8,58789.3,58702,58887.5,58884,58910,58805,58695,58505,58509,58429,58629,58429,58616,58301,58202,57498,57850,57501,57215,56975,57098,56988,56990,57420,58350,58614,58220,58800,58520,57202,56106,56150,56060,56010,56500,56788,56476,55980,56320,55733.55,55463.2,55850.2,56122.35,56478,56197,57500,58822,58101,57820,57785,58000,57920,57450,57404.2,55778,56200,55895,56210.11,56398.5,56118.5,56398.5,55930.2,56550,57195],\"l\":[60880,60600,61125,61900,60821,60789,61535,61980,61225,60870,60870,59000,59200,59900,59575,59600,59600,59300,60850,62220,61818,60255,63100,63504,63675,63250,63199,64100,64018,63835,63752,63402,63183,62580,62660,62450,62005,60700,60550,60750,60900,61255,59285,58125,55875,57099,55801,55537,55321,55367,54200,54615,54470,55402,54455,53690,53450,54050,54550,54820,55439,55925,56225,56450,56224,56554.5,56802.21,55925,56060.8,56125,56850,56328,56645,56265,55960,54380,54200,54084,53652,54399,58125,59005,59320,60050,60250,59940,59050.33,59202,59625,59300,59050.33,59210,59610,58510,58620,58985,58550,58680,58670,59110,59697.84,59325,59920,60215,60160,61964,61644.1,61747,61700.5,61800.1,62251,61010,61040,61062,61210,61210,60810,60510,60660,60660,60610,60729,59699.8,59598.2,60035.5,60400.1,60250.43,60450,60610,60413,60323,60328,60287,60732,60465,60256,60000,59908,58830,59189.8,59098.8,58789.3,58702,58887.5,58884,58775.5,58805,58505,58505,58509,58429,58629,58429,58429,58211,57800.45,56879.25,57603,57260,57215,56975,56922,56909,56990,57420,57390,58614,58220,58800,57202,56665,56106,56150,56060,56010,56444,56788,56350,55980,56320,55733.55,55463.2,55850.2,56122.35,56478,56020.08,56559.08,57822,58101,57820,57785,58000,57920,57385,57220,55778,56200,55895,56179,56398.5,56118.5,56398.5,55930.2,56550,57195],\"v\":[1133,968,201,1280,224,1377,1449,832,896,928,1194,1475,377,1333,1435,371,218,1290,1172,1325,763,906,1267,799,157,725,588,172,599,642,793,232,110,895,1412,235,772,1361,967,167,788,661,141,1066,493,1477,1337,611,1266,1008,436,529,414,202,1229,471,827,316,544,1327,859,1238,1459,869,633,1370,1004,1305,1230,471,1373,518,1032,1414,1484,1426,1391,1320,537,1156,828,873,185,1143,975,1314,113,302,130,558,128,890,296,1488,258,829,1357,1163,633,1087,133,505,105,1066,419,1489,991,309,1309,1428,1366,636,800,1452,278,275,1266,292,477,1297,751,505,686,947,493,845,276,350,507,810,1337,541,1215,1342,106,134,1331,997,344,1139,925,210,275,225,162,453,400,1328,646,778,1125,1298,1183,310,744,176,1055,920,426,1463,229,262,503,1345,104,510,1380,1336,1408,224,974,832,335,1149,958,397,102,1258,225,649,535,1250,446,218,1461,1091,294,1015,511,621,978,641,783,1381,486,788,390,365,624,297,490,1498,1030,725,1147]}";
//                // JSONObject chartDataObj = new JSONObject(data);
//                JSONArray t = chartDataObj.getJSONArray("t");
//                JSONArray c = chartDataObj.getJSONArray("c");
//                JSONArray o = chartDataObj.getJSONArray("o");
//                JSONArray h = chartDataObj.getJSONArray("h");
//                JSONArray l = chartDataObj.getJSONArray("l");
//                JSONArray v = chartDataObj.getJSONArray("v");
//                for (int x = 0; x < t.length(); x++) {
//                    yValsCandleStick.add(new CandleEntry(x, Float.parseFloat(h.getString(x)), Float.parseFloat(l.getString(x)), Float.parseFloat(o.getString(x)), Float.parseFloat(c.getString(x)), "Hello"));
//                    HourAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(Long.parseLong(t.getString(x)));
//                    XAxis xAxis = candleStickChart.getXAxis();
//                    xAxis.setValueFormatter(xAxisFormatter);
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            yValsCandleStick.add(new CandleEntry(0, 0, 0, 0, 0, "Exchange"));
//        }
//
//        return yValsCandleStick;
//    }
//
//    private void getChartData()
//    {
//        Map<String, String> m=new HashMap<>();
//        m.put("pair_id", pairDetailView.pair_id);
//        m.put("buy_page", 1 + "");
//        m.put("sell_page", 1 + "");
//        m.put("resolution", "30");
//        m.put("page_type", "");
//        m.put("isherder", "true");
//        m.put("token",pairDetailView.savePreferences.reterivePreference(pairDetailView, DefaultConstants.token)+"");
//        m.put("DeviceToken",pairDetailView.getDeviceToken()+"");
//
//        Map<String,String> headerMap=new HashMap<>();
//        headerMap.put("X-API-KEY", UtilClass.xApiKey);
//        headerMap.put("Rtoken", pairDetailView.getNewRToken()+"");
//
//        System.out.println("MAp data===="+m);
//
//        new ServerHandler().sendToServer(pairDetailView, pairDetailView.getApiUrl()+"get-chart-n-orders", m, 0,headerMap, 20000, R.layout.progressbar, new CallBack() {
//            @Override
//            public void getRespone(String dta, ArrayList<Object> respons) {
//                try {
//                    System.out.println("Responseorder==="+dta);
//                    JSONObject obj = new JSONObject(dta);
//                    if (obj.getBoolean("status")) {
//                        try
//                        {
//
//
//
//
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//
//                    } else {pairDetailView.alertDialogs.alertDialog(pairDetailView, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//                        }
//                    });
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

}