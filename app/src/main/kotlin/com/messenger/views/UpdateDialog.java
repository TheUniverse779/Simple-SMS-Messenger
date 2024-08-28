package com.messenger.views;//package com.messenger.views;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.PorterDuff;
//import android.net.Uri;
//import android.preference.PreferenceManager;
//import android.text.format.DateFormat;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import com.bumptech.glide.Glide;
//import com.messenger.App;
//import com.simplemobiletools.smsmessenger.BuildConfig;
//import com.simplemobiletools.smsmessenger.R;
//import com.messenger.ads.AdsConfigLoaded;
//import java.util.Calendar;
//import java.util.Random;
//
//public class UpdateDialog extends Dialog {
//
//    public UpdateDialog(@NonNull Context context) {
//        super(context, R.style.Translucent);
//        init();
//    }
//
//    private void init() {
//        setContentView(R.layout.dialog_update);
//
//        TextView tvTitle = findViewById(R.id.tv_title);
//        TextView tvMessage = findViewById(R.id.tv_message);
//        TextView tvTime = findViewById(R.id.tv_time);
//        ImageView imgIcon = findViewById(R.id.img_icon);
//
//        TextView btnCancel = findViewById(R.id.bt_cancel);
//        TextView btnOk = findViewById(R.id.bt_ok);
//
//        tvTime.setText(getStringDateSms(System.currentTimeMillis()));
//        tvMessage.setText(AdsConfigLoaded.get().getUpdateMessage());
//        tvTitle.setText(AdsConfigLoaded.get().getUpdateTitle().trim());
//        btnOk.setText(AdsConfigLoaded.get().getUpdateActionTitle());
//        Glide.with(getContext())
//                .load(AdsConfigLoaded.get().getUpdateIcon())
//                .into(imgIcon);
//
//        btnCancel.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
//        if(AdsConfigLoaded.get().getUpdateMode().equals("2")){
//            setCancelable(false);
//            btnCancel.setVisibility(View.GONE);
//        }
//        btnCancel.setOnClickListener(v -> dismiss());
//
//        btnOk.setOnClickListener(v -> openLink(getContext(),AdsConfigLoaded.get().getUpdateLink()));
//
//        if(!AdsConfigLoaded.get().getUpdateIsAd().equals("1")){
//            findViewById(R.id.tv_ads).setVisibility(View.GONE);
//        }
//
//    }
//
//    public void showDialog(){
//        if(AdsConfigLoaded.get().getUpdateMode().equals("2")){
//            show();
//        }else if(AdsConfigLoaded.get().getUpdateMode().equals("1")){
//            if(AdsConfigLoaded.get().getUpdateIsAd().equals("1")){
//                boolean isInstall = isPackageInstalled(AdsConfigLoaded.get().getUpdateTargetId(),getContext().getPackageManager());
//                if(!isInstall){
//                    int fr = new Random().nextInt(100);
//                    try {
//                        if(fr< Integer.parseInt(AdsConfigLoaded.get().getUpdateAdsFrequency())){
//                            if(!isLimit()){
//                                show();
//                            }
//                        }
//                    }catch (Exception e){
//                        System.out.print(e.getMessage());
//                        show();
//                    }
//                }
//            }else {
//                try {
//                    int appVersionCode = Integer.parseInt(AdsConfigLoaded.get().getAppVersionCode());
//                    if(BuildConfig.VERSION_CODE<appVersionCode){
//                        show();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    private boolean isLimit(){
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.get());
//        int ii = preferences.getInt(AdsConfigLoaded.get().getUpdateTargetId()+"+ads_limit",0);
//        if(ii<= Integer.parseInt(AdsConfigLoaded.get().getUpdateAdsLimit())){
//            preferences.edit().putInt(AdsConfigLoaded.get().getUpdateTargetId()+"+ads_limit",++ii).apply();
//            return false;
//        }else {
//            return true;
//        }
//    }
//
//
//    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
//        try {
//            packageManager.getPackageInfo(packageName, 0);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }
//
//    private void openLink(Context context, String url) {
//        try {
//            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private static String getStringDateSms(long date) {
//        return DateFormat.format("HH:mm aa",date).toString();
//    }
//
//}
