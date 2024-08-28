package com.messenger.ads;//package com.messenger.ads;
//
//import com.simplemobiletools.smsmessenger.BuildConfig;
//
//public class AdsConfig {
//
//    private static final String ADMOB_ADS_BANNER_ID_TEST = "ca-app-pub-3940256099942544/6300978111";
//
//    private static final String ADMOB_ADS_INTER_ID_TEST = "ca-app-pub-3940256099942544/1033173712";
//
//    private static final String ADS_NATIVE_ID_TEST = "ca-app-pub-3940256099942544/2247696110";
//
//    private static final String FACEBOOK_ADS_BANNER_ID_TEST = "YOUR_PLACEMENT_ID";
//
//    private static final String FACEBOOK_ADS_INTER_ID_TEST = "YOUR_PLACEMENT_ID";
//
//    private static final String FACEBOOK_ADS_NATIVE_ID_TEST = "YOUR_PLACEMENT_ID";
//
//    public static String getAdmobBannerId(){
//        return BuildConfig.DEBUG?ADMOB_ADS_BANNER_ID_TEST: AdsConfigLoaded.get().getInAppBannerId();
//    }
//
//    public static String getFacebookBannerId(){
//        return BuildConfig.DEBUG?FACEBOOK_ADS_BANNER_ID_TEST: AdsConfigLoaded.get().getInAppBannerId();
//    }
//
//
//    public static String getAdmobBannerQuickAccessId(){
//        return BuildConfig.DEBUG?ADMOB_ADS_BANNER_ID_TEST: AdsConfigLoaded.get().getSystemAppBannerId();
//    }
//
//    public static String getFacebookBannerQuickAccessId(){
//        return BuildConfig.DEBUG?FACEBOOK_ADS_BANNER_ID_TEST: AdsConfigLoaded.get().getSystemAppBannerId();
//    }
//
//
//    public static String getAdmobNativeId(){
//        return BuildConfig.DEBUG?ADS_NATIVE_ID_TEST: AdsConfigLoaded.get().getInAppNativeId();
//    }
//
//    public static String getFacebookNativeId(){
//        return BuildConfig.DEBUG?FACEBOOK_ADS_NATIVE_ID_TEST: AdsConfigLoaded.get().getInAppNativeId();
//    }
//
//
//    public static String getAdmobInterId(){
//        return BuildConfig.DEBUG?ADMOB_ADS_INTER_ID_TEST: AdsConfigLoaded.get().getInAppInterId();
//    }
//
//    public static String getFacebookInterId(){
//        return BuildConfig.DEBUG?FACEBOOK_ADS_INTER_ID_TEST: AdsConfigLoaded.get().getInAppInterId();
//    }
//
//
//    public static int getTimeDelay(){
//        return BuildConfig.DEBUG?10000:Integer.parseInt(AdsConfigLoaded.get().getInterDelay())*1000;
//    }
//
//}
