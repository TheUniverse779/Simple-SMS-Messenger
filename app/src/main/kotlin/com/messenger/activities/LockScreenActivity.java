package com.messenger.activities;//package com.messenger.activities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//import android.content.pm.ActivityInfo;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.bumptech.glide.Glide;
//import com.simplemobiletools.smsmessenger.R;
//import com.messenger.ads.AdsConfigLoader;
//import com.messenger.ads.Callback;
//import com.messenger.ads.MyAds;
//import com.messenger.fragments.WebViewFragment;
//import com.messenger.helper.SqDatabase;
//import com.messenger.services.LockNotification;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class LockScreenActivity extends AppCompatActivity {
//
//    private ImageView bgLock;
//
//    private ViewPager2 viewPager;
//
//    private TextView tvDate,tvUnlock;
//
//    private WebViewFragment viewFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        noStatusBarAndNavigation();
//        super.onCreate(savedInstanceState);
//        clearStatusBarAndNavigationBar();
//        try{setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);} catch(Exception ignore){}
//        setContentView(R.layout.activity_lock_screen);
//
//        initViews();
//
//        Glide.with(this)
//                .load(R.drawable.bg_lock)
//                .into(bgLock);
//
//        initViewPager();
//
//        LockNotification.updateNotification(this);
//
//        SqDatabase.getDb().syn(this);
//
//        viewFragment.init(this);
//    }
//
//    private void initViews() {
//        bgLock = findViewById(R.id.bg_lock);
//        viewPager = findViewById(R.id.view_pager);
//        viewFragment = findViewById(R.id.view_wv);
//    }
//
//
//    private void initViewPager() {
//        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
//        viewPager.setAdapter(new RecyclerView.Adapter() {
//            @NonNull
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(LockScreenActivity.this).inflate(R.layout.item_view_lock_screen,parent,false);
//                return new RecyclerView.ViewHolder(view) {};
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//                if(position==1){
//                    holder.itemView.setVisibility(View.GONE);
//                }else {
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    setupViewLock(holder.itemView);
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return 2;
//            }
//        });
//
//        viewPager.setCurrentItem(0);
//
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if(position==1){
//                    new Handler().postDelayed(() -> finish(),500);
//                }
//            }
//        });
//
//    }
//
//    private void setupViewLock(View itemView) {
//
////        AdsConfigLoader.get().synWithCallback((value, where) -> MyAds.initBannerQuickAccess(itemView));
//
//        tvDate = itemView.findViewById(R.id.tv_date);
//        tvUnlock = itemView.findViewById(R.id.tv_unlock);
//        itemView.setBackgroundColor(0);
//        tvDate.setText(new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH).format(new Date()));
//
//        itemView.findViewById(R.id.btn_facebook).setOnClickListener(v -> {
//            viewFragment.start(SqDatabase.getDb().getMessApp(LockScreenActivity.this,"Facebook"));
//        });
//        itemView.findViewById(R.id.btn_messenger).setOnClickListener(v -> {
//            viewFragment.start(SqDatabase.getDb().getMessApp(LockScreenActivity.this,"Messenger"));
//        });
//        itemView.findViewById(R.id.btn_twitter).setOnClickListener(v -> {
//            viewFragment.start(SqDatabase.getDb().getMessApp(LockScreenActivity.this,"Twitter"));
//        });
//        itemView.findViewById(R.id.btn_instagram).setOnClickListener(v -> {
//            viewFragment.start(SqDatabase.getDb().getMessApp(LockScreenActivity.this,"Instagram"));
//        });
//
//        textLockAnimation();
//
//    }
//
//    private void textLockAnimation(){
//        if(tvUnlock==null)return;
//        tvUnlock.setVisibility(View.VISIBLE);
//        tvUnlock.animate()
//                .alpha(0.5f)
//                .translationY(-80).setDuration(900).withEndAction(() ->
//                tvUnlock.animate().translationY(0)
//                        .alpha(1)
//                        .setDuration(900).withEndAction(() ->
//                        tvUnlock.animate()
//                                .alpha(0.5f)
//                                .translationY(-60).setDuration(800).withEndAction(() ->
//                                tvUnlock.animate()
//                                        .alpha(0)
//                                        .translationY(0).setDuration(800)
//                                        .withEndAction(() -> tvUnlock.setVisibility(View.GONE)).start()).start()).start()).start();
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        textLockAnimation();
//    }
//
//    private void clearStatusBarAndNavigationBar() {
//        if (getWindow() != null) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        }
//    }
//
//
//    private void noStatusBarAndNavigation(){
//        try {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//        }catch (Exception ignored){}
//
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//    }
//}
