package com.messenger.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.messenger.App;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.activities.MainActivity;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;
import com.messenger.models.magazine.MagazineContentNews;
import com.messenger.utils.PermissionRequest;
import com.messenger.utils.Utils;
import com.messenger.views.QWebView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WebViewFragment extends FrameLayout {

    public static final int KEY_SELECT_FILE = 1200;

    private MessApp messApp;

    private TextView tvTimeLimit;

    private Context context;

    private QWebView qWebView;

    private View reload;

    private ProgressBar loading;

    private TextView tvWebName;

    private ValueCallback<Uri[]> uploadMessage;

    private Handler hdLimit;

    private Runnable rdLimit;

    private SimpleDateFormat limitFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private long countLimit;

    public void init(Context context){
        this.context = context;
        initView();
        close(0);
        initWebView();
        loading.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        this.setVisibility(View.GONE);

        setupLimit();

    }

    private void setupLimit() {
        hdLimit = new Handler();

        rdLimit = () -> {
            countLimit++;
            tvTimeLimit.setText(limitFormat.format(countLimit*1000));
            hdLimit.postDelayed(rdLimit,1000);
            if(countLimit==(messApp.getTimeLimit()*60)){
                tvWebName.setLines(2);
                tvWebName.setText("You have reach to the limit time!");
                tvWebName.setTextColor(Color.RED);
            }
        };
    }

    @SuppressLint("SetTextI18n")
    public void start(MessApp messApp){

        App.get().sendTracker(getClass().getSimpleName());

        App.get().sendTracker(messApp.getName());

        messApp.setOpenCount(messApp.getOpenCount()+1);

        messApp.setTimeLimit(SqDatabase.getDb().getMessApp(App.get(),messApp.getName()).getTimeLimit());

        SqDatabase.getDb().launchOpenUp(App.get(),messApp);

        this.setVisibility(VISIBLE);

        WebViewFragment.this.animate()
                .scaleX(1)
                .translationY(0)
                .scaleY(1)
                .alpha(1)
                .setDuration(400)
                .withEndAction(() -> {
                    if(context instanceof MainActivity){
                        ((MainActivity)context).updateStatistics();
                    }
                })
                .start();

        this.messApp = messApp;

        startLimit();


        tvWebName.setText(messApp.getUsename()+" - "+messApp.getName());
        tvWebName.setTextColor(Color.WHITE);
        tvWebName.setLines(1);


        if (isMobile(messApp.getUrl()))
            qWebView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        else qWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1");

        qWebView.loadUrl(messApp.getUrl());


        ((MainActivity)context).layoutBanner.setVisibility(GONE);

    }

    @SuppressLint("SetTextI18n")
    public void start(MagazineContentNews news){

        this.setVisibility(VISIBLE);

        WebViewFragment.this.animate()
                .scaleX(1)
                .translationY(0)
                .scaleY(1)
                .alpha(1)
                .setDuration(400)
                .start();

        App.get().sendTracker("MagazineContentNews");

        tvWebName.setText(news.getTitle());
        tvWebName.setTextColor(Color.WHITE);
        tvWebName.setLines(1);

        tvTimeLimit.setVisibility(GONE);

        qWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1");


        qWebView.loadUrl(news.getContentURL());



        qWebView.loadUrl("javascript:if (typeof(document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0]) !=" +
                " 'undefined' && document.getElementsByClassName('ui-footer')[0] != null)" +
                "{document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0].style.display = 'none';} void 0");

    }

    private void startLimit() {
        countLimit = 0;
        if(messApp.getTimeLimit()>0){
            tvTimeLimit.setVisibility(VISIBLE);
            hdLimit.postDelayed(rdLimit,1000);
        }else {
            tvTimeLimit.setVisibility(GONE);
        }
    }

    public void close(int time){
        removeCallback();
        this.animate()
                .scaleX(0)
                .translationY(App.get().getResources().getDisplayMetrics().heightPixels)
                .scaleY(0)
                .alpha(0)
                .setDuration(time)
                .withEndAction(() -> {
                    reload.setAlpha(0);
                    qWebView.loadUrl("javascript:document.open();document.close();");
                    WebViewFragment.this.setVisibility(GONE);
                })
                .start();

        ((MainActivity)context).layoutBanner.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

        reload.setOnClickListener(v -> qWebView.reload());
        reload.setOnTouchListener(new OnTouch());

        qWebView.setActivity(context);

        qWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        qWebView.getSettings().setAllowFileAccess(true);
        qWebView.getSettings().setAllowContentAccess(true);
//        qWebView.getSettings().setAppCacheEnabled(true);
        qWebView.getSettings().setLoadWithOverviewMode(true);
        qWebView.getSettings().setBuiltInZoomControls(true);
        qWebView.getSettings().setDisplayZoomControls(true);
        qWebView.getSettings().setSupportZoom(true);
        qWebView.getSettings().setJavaScriptEnabled(true);

        qWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        qWebView.getSettings().setDomStorageEnabled(true);


        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(qWebView, true);
        }



        qWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                reload.setAlpha(0);
//                qWebView.loadUrl("javascript:(function() { " +
//                        "document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0].style.display='none'; " +
//                        "document.getElementsByClassName('tiktok-a5lqug-DivFooterGuide e1pecv674')[0].style.display='none'; " +
//                        "})()");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                reload.setAlpha(1);
                String  fix = App.get().getString(R.string.fb_fixedBar).replace("$spx",
                        "" + Utils.heightForFixedFacebookNavbar(App.get()));
                qWebView.loadUrl(App.get().getString(R.string.fb_Css).replace("$css", fix));

                qWebView.loadUrl("javascript:if (typeof(document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0]) !=" +
                        " 'undefined' && document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0] != null)" +
                        "{document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0].style.display = 'none';} void 0");

                qWebView.loadUrl("javascript:if (typeof(document.getElementsByClassName('tiktok-a5lqug-DivFooterGuide e1pecv674')[0]) !=" +
                        " 'undefined' && document.getElementsByClassName('tiktok-a5lqug-DivFooterGuide e1pecv674')[0] != null)" +
                        "{document.getElementsByClassName('tiktok-a5lqug-DivFooterGuide e1pecv674')[0].style.display = 'none';} void 0");

//                qWebView.loadUrl("javascript:(function() { " +
//                        "document.getElementsByClassName('tiktok-py8jux-DivModalContainer e1gjoq3k0')[0].style.display='none'; " +
//                        "document.getElementsByClassName('tiktok-a5lqug-DivFooterGuide e1pecv674')[0].style.display='none'; " +
//                        "})()");

            }

        });

        qWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if(context instanceof MainActivity){
                    if(PermissionRequest.checkStore((MainActivity) context,12)){
                        if (uploadMessage != null) {
                            uploadMessage.onReceiveValue(null);
                            uploadMessage = null;
                        }
                        uploadMessage = filePathCallback;

                        Intent intent = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            intent = fileChooserParams.createIntent();
                        }
                        try {
                            ((MainActivity)context).startActivityForResult(intent, KEY_SELECT_FILE);

                        } catch (ActivityNotFoundException e) {
                            uploadMessage = null;
                            Toast.makeText(App.get(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        return true;

                    }
                }

                return false;
            }

        });

    }



    @SuppressLint("SetTextI18n")
    private void initView() {
        View btBack = findViewById(R.id.bt_back);
        tvWebName = findViewById(R.id.tv_web_name);
        loading = findViewById(R.id.loading);
        qWebView = findViewById(R.id.wv_load);
        tvTimeLimit = findViewById(R.id.tv_time_limit);
        reload = findViewById(R.id.reload);
        btBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close(400);
            }
        });

    }
    private boolean isMobile(String url) {
        return !url.contains("badoo") &&
                !url.contains("linkedin") &&
                !url.contains("pinterest") &&
                !url.contains("reddit") &&
                !url.contains("renren") &&
                !url.contains("baidu") &&
                !url.contains("taringa") &&
                !url.contains("tiktok") &&
                !url.contains("mail");
    }

    public void removeCallback(){
        countLimit = 0;
        if(hdLimit!=null)
        hdLimit.removeCallbacks(rdLimit);
    }

    public void setUploadMessage(ValueCallback<Uri[]> uploadMessage) {
        this.uploadMessage = uploadMessage;
    }

    public ValueCallback<Uri[]> getUploadMessage() {
        return uploadMessage;
    }

    public boolean canBack(){
        return qWebView.canGoBack();
    }

    public void goBack(){
        qWebView.goBack();
    }


    public WebViewFragment(@NonNull Context context) {
        super(context);
    }

    public WebViewFragment(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }







}
