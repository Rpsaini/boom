package com.web.bloomex.pairdetailfragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.web.bloomex.R;


public class CryptoGraphFragment extends Fragment {
    private View view;
    //private ProgressBar viewprogressbar;

    private String transferAMount = "", transferTo = "";
    private Dialog paymentWaitingDialog;
    private PairDetailView pairDetailView;

    public CryptoGraphFragment() {
        // Required empty public constructor
    }


    public static CryptoGraphFragment newInstance(String param1, String param2) {
        CryptoGraphFragment fragment = new CryptoGraphFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_crypto_graph, container, false);
        pairDetailView = (PairDetailView) getActivity();

        WebView mywebview = (WebView) view.findViewById(R.id.webview);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //viewprogressbar = view.findViewById(R.id.viewprogressbar);
        mywebview.setWebViewClient(new WebViewController());


        return view;
    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            // viewprogressbar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    paymentWaitingDialog.dismiss();
                }
            }, 2000);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    paymentWaitingDialog.dismiss();
//                }
//            }, 2000);

            super.onPageFinished(view, url);

        }


        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
        }

    }
}

