package com.messenger.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


import com.messenger.ads.MyAds;
import com.simplemobiletools.smsmessenger.R;

public class HTML5Activity extends AppCompatActivity{


    public static  String linkGame = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_5);
//        ((WebView)findViewById(R.id.webView)).loadUrl("http://192.168.31.133/2048Game/");
        ((WebView)findViewById(R.id.webView)).loadUrl(linkGame);
        ((WebView)findViewById(R.id.webView)).getSettings().setJavaScriptEnabled(true);
        ((WebView)findViewById(R.id.webView)).getSettings().setLoadWithOverviewMode(true);
        ((WebView)findViewById(R.id.webView)).getSettings().setUseWideViewPort(true);
        (findViewById(R.id.bt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyAds.showInterFull(this, (value, where) -> {
            super.onBackPressed();
        });
    }
}
