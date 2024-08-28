package com.messenger.ads;//package com.messenger.ads;
//
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//
//import com.messenger.App;
//
//public class AdsConfigLoaded {
//
//
//    protected final static String INTER_IN_APP_ID = "ca-app-pub-7057107138215897/4612370802";
//    protected final static String BANNER_IN_APP_ID = "ca-app-pub-7057107138215897/8551615813";
//    protected final static String NATIVE_IN_APP_ID = "ca-app-pub-7057107138215897/3386440118";
//
//    protected final static String INTER_SYSTEM_ID = "";
//    protected final static String BANNER_SYSTEM_ID = "ca-app-pub-7057107138215897/9760276774";
//    protected final static String NATIVE_SYSTEM_ID = "";
//
//    private static AdsConfigLoaded adsConfigLoaded;
//    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.get());
//
//    public static AdsConfigLoaded get(){
//        if(adsConfigLoaded ==null){
//            adsConfigLoaded = new AdsConfigLoaded();
//        }
//        return adsConfigLoaded;
//    }
//
//    public void setInApp(String inAppPlatform, String showBanner, String showInter, String showNative, String showReward,
//                         String interStart, String interDelay, String bannerId, String interId, String nativeId, String rewardId, String adsId){
//        preferences.edit()
//                .putString("in_app_platform",inAppPlatform)
//                .putString("in_app_show_banner",showBanner)
//                .putString("in_app_show_inter",showInter)
//                .putString("in_app_show_native",showNative)
//                .putString("in_app_show_reward",showReward)
//                .putString("in_app_inter_start",interStart)
//                .putString("in_app_inter_delay",interDelay)
//                .putString("in_app_banner_id",bannerId)
//                .putString("in_app_inter_id",interId)
//                .putString("in_app_native_id",nativeId)
//                .putString("in_app_reward_id",rewardId)
//                .putString("in_app_ads_id",adsId)
//                .apply();
//    }
//
//    public String getInAppPlatForm(){return preferences.getString("in_app_platform","admob"); }
//    public String getInShowBanner(){return preferences.getString("in_app_show_banner","1"); }
//    public String getInShowInter(){return preferences.getString("in_app_show_inter","1"); }
//    public String getInShowNative(){return preferences.getString("in_app_show_native","1"); }
//    public String getInShowReward(){return preferences.getString("in_app_show_reward","1"); }
//    public String getInterStart(){return preferences.getString("in_app_inter_start","1"); }
//    public String getInterDelay(){return preferences.getString("in_app_inter_delay","30"); }
//    public String getInAppBannerId(){return preferences.getString("in_app_banner_id",BANNER_IN_APP_ID); }
//    public String getInAppInterId(){return preferences.getString("in_app_inter_id",INTER_IN_APP_ID); }
//    public String getInAppNativeId(){return preferences.getString("in_app_native_id",NATIVE_IN_APP_ID); }
//    public String getInRewardId(){return preferences.getString("in_app_reward_id","1"); }
//    public String getInAppAdsId(){return preferences.getString("in_app_ads_id","1"); }
//
//    public void setSystem(String s_ads_platform, String s_show_banner, String s_show_inter, String s_show_native, String s_show_reward,
//                          String s_banner_id, String s_inter_id, String s_native_id, String s_reward_id){
//        preferences.edit()
//                .putString("s_ads_platform",s_ads_platform)
//                .putString("s_show_banner",s_show_banner)
//                .putString("s_show_inter",s_show_inter)
//                .putString("s_show_native",s_show_native)
//                .putString("s_show_reward",s_show_reward)
//                .putString("s_banner_id",s_banner_id)
//                .putString("s_inter_id",s_inter_id)
//                .putString("s_native_id",s_native_id)
//                .putString("s_reward_id",s_reward_id)
//                .apply();
//    }
//
//    public String getSystemPlatForm(){return preferences.getString("s_ads_platform","admob"); }
//    public String getSystemShowBanner(){return preferences.getString("s_show_banner","1"); }
//    public String getSystemShowInter(){return preferences.getString("s_show_inter","1"); }
//    public String getSystemShowNative(){return preferences.getString("s_show_native","1"); }
//    public String getSystemShowReward(){return preferences.getString("s_show_reward","1"); }
//    public String getSystemAppBannerId(){return preferences.getString("s_banner_id",BANNER_SYSTEM_ID); }
//    public String getSystemAppInterId(){return preferences.getString("s_inter_id",INTER_SYSTEM_ID); }
//    public String getSystemAppNativeId(){return preferences.getString("s_native_id",NATIVE_SYSTEM_ID); }
//    public String getSystemRewardId(){return preferences.getString("s_reward_id","1"); }
//
//
//    public void setUpdate(String update_link, String update_message, String update_action_title, String update_mode,
//                          String update_title, String update_icon, String update_is_ad, String target_id, String ads_frequency, String ads_limit,String app_version_code){
//        preferences.edit()
//                .putString("update_link",update_link)
//                .putString("update_message",update_message)
//                .putString("update_action_title",update_action_title)
//                .putString("update_mode",update_mode)
//                .putString("update_title",update_title)
//                .putString("update_icon",update_icon)
//                .putString("update_is_ad",update_is_ad)
//                .putString("target_id",target_id)
//                .putString("ads_frequency",ads_frequency)
//                .putString("ads_limit",ads_limit)
//                .putString("app_version_code",app_version_code)
//                .apply();
//    }
//    public String getUpdateLink(){return preferences.getString("update_link","Update"); }
//    public String getUpdateMessage(){return preferences.getString("update_message","Update"); }
//    public String getUpdateActionTitle(){return preferences.getString("update_action_title","OK"); }
//    public String getUpdateMode(){return preferences.getString("update_mode","0"); }
//    public String getUpdateTitle(){return preferences.getString("update_title","Update"); }
//    public String getUpdateIcon(){return preferences.getString("update_icon","update"); }
//    public String getUpdateIsAd(){return preferences.getString("update_is_ad","1"); }
//    public String getUpdateTargetId(){return preferences.getString("target_id","target_id"); }
//    public String getUpdateAdsFrequency(){return preferences.getString("ads_frequency","70"); }
//    public String getUpdateAdsLimit(){return preferences.getString("ads_limit","10"); }
//    public String getAppVersionCode(){return preferences.getString("app_version_code","0"); }
//
//
//}
