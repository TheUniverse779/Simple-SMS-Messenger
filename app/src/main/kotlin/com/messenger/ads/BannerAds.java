package com.messenger.ads;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.messenger.App;
import com.simplemobiletools.smsmessenger.BuildConfig;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.utils.Constant;
import com.messenger.utils.PreferenceUtil;

public class BannerAds {

    private static final String BANNER_TEST_ID = "ca-app-pub-3940256099942544/2014213617";
    private static final String BANNER_ID = "ca-app-pub-7057107138215897/8551615813";
    private static final String BANNER_ID_COLL = "ca-app-pub-7057107138215897/3057310468";

    public static void loadBannerAds(Activity ctx) {
        final AdView mAdViewBanner = new AdView(ctx);
        mAdViewBanner.setAdSize(getAdSize(ctx));
        if(PreferenceUtil.getInstance(App.get()).getValue(Constant.SharePrefKey.BANNER_COLL, "no").equals("yes")) {
            mAdViewBanner.setAdUnitId(BuildConfig.DEBUG?BANNER_TEST_ID:BANNER_ID_COLL);
        }else {
            mAdViewBanner.setAdUnitId(BuildConfig.DEBUG?BANNER_TEST_ID:BANNER_ID);
        }
        final LinearLayout adViewContainer = ctx.findViewById(R.id.adView_container);
        adViewContainer.removeAllViews();
        adViewContainer.addView(mAdViewBanner);
        final AdRequest adRequest = getAdRequest();
        mAdViewBanner.loadAd(adRequest);
        mAdViewBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adViewContainer.setVisibility(View.VISIBLE);
                hideBannerLoading(ctx,false);

                mAdViewBanner.setOnPaidEventListener(adValue -> {
                    try {
                        App.get().initROAS(adValue.getValueMicros(),adValue.getCurrencyCode());
                        AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
                        adRevenue.setRevenue((double) (adValue.getValueMicros() / 1000000f), adValue.getCurrencyCode());
                        Adjust.trackAdRevenue(adRevenue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adViewContainer.setVisibility(View.GONE);
                hideBannerLoading(ctx,true);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adViewContainer.setVisibility(View.GONE);
                hideBannerLoading(ctx,true);
            }

        });

        hideBannerLoading(ctx,false);
    }

    public static void hideBannerLoading(Activity ctx,boolean bl){
        TextView tvBannerLoading = ctx.findViewById(R.id.tv_loading);
        tvBannerLoading.setMinimumHeight(getAdSize(ctx).getHeightInPixels(ctx));
        if(bl){
            tvBannerLoading.setVisibility(View.GONE);
            ctx.findViewById(R.id.view_d).setVisibility(View.GONE);
        }else {
            tvBannerLoading.setVisibility(View.VISIBLE);
            ctx.findViewById(R.id.view_d).setVisibility(View.VISIBLE);
        }
    }

    public static void clearBanner(Activity ctx){
        final LinearLayout adViewContainer = ctx.findViewById(R.id.adView_container);
        adViewContainer.removeAllViews();
    }

    public static AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    @NonNull
    private static AdRequest getAdRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if(PreferenceUtil.getInstance(App.get()).getValue(Constant.SharePrefKey.SHOW_BANNER_COLL, "no").equals("yes")) {
            Bundle extras = new Bundle();
            extras.putString("collapsible", "bottom");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        }
        return builder.build();
    }

}
