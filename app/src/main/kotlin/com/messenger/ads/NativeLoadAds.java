package com.messenger.ads;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.messenger.App;
import com.simplemobiletools.smsmessenger.BuildConfig;
import com.simplemobiletools.smsmessenger.R;

public class NativeLoadAds {

    private static final String NATIVE_TEST_ID = "ca-app-pub-3940256099942544/2247696110";
    private static final String NATIVE_ID = "ca-app-pub-7057107138215897/3386440118";

    public static void loadNativeAndShow(View ctx,boolean showVideo){
        AdLoader adLoader = new AdLoader.Builder(App.get(), BuildConfig.DEBUG?NATIVE_TEST_ID:NATIVE_ID)
                .forNativeAd(nativeAd -> {
                    showNativeNow(ctx,nativeAd,showVideo);
                    nativeAd.setOnPaidEventListener(adValue -> {
                        try {
                            App.get().initROAS(adValue.getValueMicros(),adValue.getCurrencyCode());
                            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
                            adRevenue.setRevenue((double) (adValue.getValueMicros() / 1000000f), adValue.getCurrencyCode());
                            Adjust.trackAdRevenue(adRevenue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                })
                .build();
        new Thread(() -> adLoader.loadAd(getAdRequest())).start();
    }

    private static void showNativeNow(View ctx,NativeAd ads,boolean showVideo){
        View view = ctx.findViewById(R.id.native_ads);
        NativeAdView adView = view.findViewById(R.id.ad_view);
        adView.setVisibility(View.VISIBLE);
        if(showVideo){
            MediaView mediaView = adView.findViewById(R.id.ad_media);
            if(mediaView!=null){
                adView.setMediaView(mediaView);
            }
        }
        TextView tvHeadLine = adView.findViewById(R.id.ad_headline);
        TextView tvBody = adView.findViewById(R.id.ad_body);
        TextView tvAction = adView.findViewById(R.id.ad_call_to_action);
        ImageView imgIcon = adView.findViewById(R.id.ad_icon);

        adView.setHeadlineView(tvHeadLine);
        adView.setBodyView(tvBody);
        adView.setCallToActionView(tvAction);
        adView.setIconView(imgIcon);
        tvHeadLine.setText(ads.getHeadline()!=null?ads.getHeadline():"");
        tvBody.setText(ads.getBody()!=null?ads.getBody():"");
        tvAction.setText(ads.getCallToAction()!=null?ads.getCallToAction():"");

        CardView cvIcon = adView.findViewById(R.id.cv_icon);
        if(ads.getIcon()!=null){
            if(ads.getIcon().getDrawable()!=null){
                cvIcon.setVisibility(View.VISIBLE);
                imgIcon.setImageDrawable(ads.getIcon().getDrawable());
            }else if(ads.getIcon().getUri()!=null){
                cvIcon.setVisibility(View.VISIBLE);
                imgIcon.setImageURI(ads.getIcon().getUri());
            }else {
                cvIcon.setVisibility(View.GONE);
            }
        }else {
            cvIcon.setVisibility(View.GONE);
        }
        adView.setNativeAd(ads);
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }
}
