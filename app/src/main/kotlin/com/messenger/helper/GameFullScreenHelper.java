package com.messenger.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.messenger.ads.Callback;
import com.messenger.views.QWebView;

public class GameFullScreenHelper {

    private static Callback onExitFull;

    @SuppressLint("StaticFieldLeak")
    private static WebView gameView;

    @SuppressLint("StaticFieldLeak")
    private static FrameLayout rootView;

    public static void addRootView(Activity activity, FrameLayout rootView) {
        if(gameView==null){
            gameView = new QWebView(activity);
            init();
            gameView.loadUrl("https://www.gamezop.com/?id=4tsDiNyQj");
        }

        if(GameFullScreenHelper.rootView!=null){
            try {
                GameFullScreenHelper.rootView.removeAllViews();
            }catch (Exception ignored){}
        }
        rootView.addView(gameView);
        GameFullScreenHelper.rootView = rootView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private static void init() {
        gameView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        gameView.getSettings().setAllowFileAccess(true);
        gameView.getSettings().setAllowContentAccess(true);
//        gameView.getSettings().setAppCacheEnabled(true);
        gameView.getSettings().setLoadWithOverviewMode(true);
        gameView.getSettings().setBuiltInZoomControls(true);
        gameView.getSettings().setDisplayZoomControls(true);
        gameView.getSettings().setSupportZoom(true);
        gameView.getSettings().setJavaScriptEnabled(true);
        gameView.getSettings().setPluginState(WebSettings.PluginState.ON);
        gameView.getSettings().setDomStorageEnabled(true);
        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(gameView, true);
        }
        gameView.setWebViewClient(new WebViewClient(){});
        gameView.setWebChromeClient(new WebChromeClient());
    }

    public static WebView getGameView() {
        return gameView;
    }

    public static void setOnExitFullCallBack(Callback onExitFull) {
        GameFullScreenHelper.onExitFull = onExitFull;
    }

    public static Callback getOnExitFullCallBack() {
        return onExitFull;
    }

    public static void clearGameView(){
        gameView = null;
    }
}
