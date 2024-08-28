package com.messenger.ads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.messenger.App;

public class GDPRConsentHelper {

    public interface ConsentCallback{
        void callback();
    }

    private final ConsentInformation consentInformation;

    private static GDPRConsentHelper gdprConsentHelper;

    private GDPRConsentHelper() {
        consentInformation  =  UserMessagingPlatform.getConsentInformation(App.get());
    }

    public static GDPRConsentHelper get(){
        if(gdprConsentHelper == null){
            return gdprConsentHelper = new GDPRConsentHelper();
        }
        else return gdprConsentHelper;
    }

    public void syn(Activity activity, ConsentCallback callback){
//        if(App.get().isPro()){
//            callback.callback();
//            return;
//        }
        if(canRequestAds()||lastAgree()){
            callback.callback();
            return;
        }

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .build();
        consentInformation.requestConsentInfoUpdate(activity, params, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
            @Override
            public void onConsentInfoUpdateSuccess() {
                if(canRequestAds()){
                    setLastAgree(true);
                    callback.callback();
                }else {
                    setLastAgree(false);
                    UserMessagingPlatform.loadConsentForm(activity, consentForm -> consentForm.show(activity, formError -> {
                        setLastAgree(canRequestAds());
                        callback.callback();
                    }), formError -> callback.callback());
                }
            }
        }, formError
                -> callback.callback());
    }


    public boolean canRequestAds(){
        return consentInformation!=null&&consentInformation.canRequestAds();
    }
    public boolean lastAgree(){
        SharedPreferences preferences = App.get().getSharedPreferences("gdpr_consent",Context.MODE_PRIVATE);
        return preferences.getBoolean("last_agree",false);
    }
    private void setLastAgree(Boolean agree){
        SharedPreferences preferences = App.get().getSharedPreferences("gdpr_consent",Context.MODE_PRIVATE);
        preferences.edit().putBoolean("last_agree",agree).apply();

    }
}
