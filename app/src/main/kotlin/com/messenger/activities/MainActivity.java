package com.messenger.activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.messenger.App;
import com.simplemobiletools.smsmessenger.BuildConfig;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.Task;
import com.messenger.ads.MyAds;
import com.messenger.callbacks.OGCallback;
import com.messenger.fragments.AddAccountInfoFragment;
import com.messenger.fragments.DeviceAppFragment;
import com.messenger.fragments.EditAccountInfoFragment;
import com.messenger.fragments.HomeAppManagerFragment;
import com.messenger.fragments.HomeNewsFragment;
import com.messenger.fragments.HomeSocialFragment;
import com.messenger.fragments.HomeStatisticsFragment;
import com.messenger.fragments.LimitFragment;
import com.messenger.fragments.PassCodeFragment;
import com.messenger.fragments.PassCodeOutAppFragment;
import com.messenger.fragments.SelectServiceFragment;
import com.messenger.fragments.StatisticsFragment;
import com.messenger.fragments.UserSettingFragment;
import com.messenger.fragments.WebViewFragment;
import com.messenger.helper.GameFullScreenHelper;
import com.messenger.helper.LockDatabase;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.helper.permissons.Func;
import com.messenger.helper.permissons.PermissionUtil;
import com.messenger.models.LockApp;
import com.messenger.models.MessApp;
import com.messenger.utils.RateUtils;
import com.messenger.views.sliding.SlidingRootNav;
import com.messenger.views.sliding.SlidingRootNavBuilder;

public class MainActivity extends AppCompatActivity{

    private SlidingRootNav slidingRootNav;

    public WebViewFragment mWebView;

    private TextView tvIsLock;

    private ViewPager viewPager;

    private HomeSocialFragment homeSocialFragment;

    private HomeStatisticsFragment homeStatisticsFragment;

    public ViewGroup layoutBanner;

//    private boolean isRequestQuickAccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch(Exception ignore){}
        setContentView(R.layout.activity_main_mess_4);

        if(!LockDatabase.get().isLockAppEnableEmpty()){
            App.get().startLockAppService();
        }

        SqDatabase.getDb().syn(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutBanner = findViewById(R.id.banner);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.view_navigation)
                .inject();

        initView();

        initTab();

        setupMenu();

        mWebView.init(this);

        MyAds.initInterAds(MainActivity.this);
        MyAds.initBannerIds(MainActivity.this);

        App.get().sendTracker(getClass().getSimpleName());

//        if(SqDatabase.getUser().isLockScreen(this)){
//            LockNotification.updateNotification(this);
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
//                Dialog dialog = new Dialog(this,R.style.Translucent);
//                dialog.setContentView(R.layout.dialog_overlay);
//                dialog.setCancelable(false);
//                dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> dialog.dismiss());
//                dialog.findViewById(R.id.bt_give_permission).setOnClickListener(v -> {
//                    isRequestQuickAccess = true;
//                    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                    myIntent.setData(Uri.parse("package:" + getPackageName()));
//                    startActivity(myIntent);
//                    dialog.dismiss();
//                });
//                dialog.show();
//            }
//        }


//        new UpdateDialog(this).showDialog();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(isRequestQuickAccess){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                SqDatabase.getUser().setLockScreen(this,Settings.canDrawOverlays(this));
//                setupMenu();
//            }
//            isRequestQuickAccess = false;
//        }
    }

    public void updateStatistics(){
        if(homeStatisticsFragment==null)return;
        homeStatisticsFragment.updateData();
    }

    private void initTab() {
        TabLayout tabLayout = findViewById(R.id.tab_layout_main);
        viewPager = findViewById(R.id.view_pager_main);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0: return homeSocialFragment = HomeSocialFragment.newInstance();
                    case 1: return HomeAppManagerFragment.newInstance();
                    case 2: return homeStatisticsFragment = HomeStatisticsFragment.newInstance();
//                    case 3: return homeStatisticsFragment = HomeStatisticsFragment.newInstance();
                }
                return HomeNewsFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0){
                    return "Social";
                }if(position==1){
                    return "App Manager";
                }else{
                    return "Statistics";
                }
            }
        });

        viewPager.setOffscreenPageLimit(5);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if(MyAds.isInterLoaded()&& MyAds.getFlag()==1){
//                    ProgressDialog dialog = new ProgressDialog(MainActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//                    dialog.setCancelable(false);
//                    dialog.setMessage("Ads loading...");
//                    dialog.show();
//                    new Handler().postDelayed(() -> {
//                        if(dialog!=null&&dialog.isShowing()){
//                            MyAds.showInterFull(MainActivity.this, (value, where) -> {
//                                if(dialog!=null&&dialog.isShowing()){
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
//                    },1000);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupMenu() {
        View btAccount = slidingRootNav.getLayout().findViewById(R.id.bt_account);
        View btPassCode = slidingRootNav.getLayout().findViewById(R.id.bt_passcode);
        View btUsage = slidingRootNav.getLayout().findViewById(R.id.bt_usage);
        View btLimit = slidingRootNav.getLayout().findViewById(R.id.bt_limit);
//        View btnLockScreen = slidingRootNav.getLayout().findViewById(R.id.bt_lock_screen);
        View btRate = slidingRootNav.getLayout().findViewById(R.id.bt_rate);
        View btFeedback = slidingRootNav.getLayout().findViewById(R.id.bt_feedback);
        View btShare = slidingRootNav.getLayout().findViewById(R.id.bt_share);

//        TextView tvLockScreen = slidingRootNav.getLayout().findViewById(R.id.tv_is_lock_screen);

        btAccount.setOnTouchListener(new OnTouch());
        btPassCode.setOnTouchListener(new OnTouch());
        btUsage.setOnTouchListener(new OnTouch());
        btLimit.setOnTouchListener(new OnTouch());
        btRate.setOnTouchListener(new OnTouch());
        btFeedback.setOnTouchListener(new OnTouch());
        btShare.setOnTouchListener(new OnTouch());
//        btnLockScreen.setOnTouchListener(new OnTouch());
        btAccount.setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            clearFragment();
        });

        btUsage.setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            clearFragment();
            viewPager.setCurrentItem(1);
        });

        btLimit.setOnClickListener(v ->
                MyAds.showInterFull(this, (value, where) -> {
                    slidingRootNav.closeMenu();
                    clearFragment();
                    showViewTimeLimit();
                }));

        btPassCode.setOnClickListener(v ->
                MyAds.showInterFull(this, (value, where) -> {
                    slidingRootNav.closeMenu();
                    new Handler().postDelayed(() -> {
                        clearFragment();
                        showViewPassCode();
                    },250);
                }));

        btRate.setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            RateUtils.goToStore(MainActivity.this, BuildConfig.APPLICATION_ID);
        });

        btFeedback.setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            RateUtils.Feedback(MainActivity.this,"ohyad@gmail.com");
        });
        btShare.setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            RateUtils.ShareApp(MainActivity.this,BuildConfig.APPLICATION_ID);
        });

//        tvLockScreen.setText(SqDatabase.getUser().isLockScreen(this)?"ON":"OFF");
//
//        btnLockScreen.setOnClickListener(v -> {
//            if(tvLockScreen.getText().toString().equals("ON")){
//                tvLockScreen.setText("OFF");
//                SqDatabase.getUser().setLockScreen(MainActivity.this,false);
//            }else {
//                tvLockScreen.setText("ON");
//                SqDatabase.getUser().setLockScreen(MainActivity.this,true);
//            }
//        });


        updateTextIsLock();
    }


    public void updateTextIsLock(){
        tvIsLock.setText(SqDatabase.getUser().isLock(this)?"ON":"OFF");
    }


    private void initView() {
        mWebView = findViewById(R.id.view_wv);
        tvIsLock = slidingRootNav.getLayout().findViewById(R.id.tv_is_lock);
    }


    public void showViewAddApp(){
        if(getSupportFragmentManager().findFragmentByTag(SelectServiceFragment.class.getName())==null){
            SelectServiceFragment selectServiceFragment = SelectServiceFragment.newInstance();
            addFragment(selectServiceFragment,SelectServiceFragment.class.getName(),SelectServiceFragment.class.getName());
        }
    }
    public void showViewDeviceApp(OGCallback callback){
        if(getSupportFragmentManager().findFragmentByTag(DeviceAppFragment.class.getName())==null){
            DeviceAppFragment deviceAppFragment = DeviceAppFragment.newInstance();
            deviceAppFragment.setOnSaveClickListen(callback);
            addFragment(deviceAppFragment, DeviceAppFragment.class.getName(),DeviceAppFragment.class.getName());
        }
    }

    public void showViewAddAppAccountInfo(){
        if(getSupportFragmentManager().findFragmentByTag(AddAccountInfoFragment.class.getName())==null){
            AddAccountInfoFragment addAccountInfoFragment = AddAccountInfoFragment.newInstance();
            addFragment(addAccountInfoFragment, AddAccountInfoFragment.class.getName(),AddAccountInfoFragment.class.getName());
        }
    }
    public void showViewUpdateAppAccountInfo(MessApp messApp){
        if(getSupportFragmentManager().findFragmentByTag(EditAccountInfoFragment.class.getName())==null){
            EditAccountInfoFragment editAccountInfoFragment = EditAccountInfoFragment.newInstance();
            editAccountInfoFragment.setMessApp(messApp);
            addFragment(editAccountInfoFragment, EditAccountInfoFragment.class.getName(),EditAccountInfoFragment.class.getName());
        }
    }

    public void showViewPassCode(){
        if(getSupportFragmentManager().findFragmentByTag(PassCodeFragment.class.getName())==null){
            PassCodeFragment passCodeFragment = PassCodeFragment.newInstance();
            addFragment(passCodeFragment, PassCodeFragment.class.getName(),PassCodeFragment.class.getName());
        }
    }

    public void showPassCodeAppLock(LockApp app,OGCallback callback){
        if(getSupportFragmentManager().findFragmentByTag(PassCodeOutAppFragment.class.getName())==null){
            PassCodeOutAppFragment outAppFragment = PassCodeOutAppFragment.newInstance();
            outAppFragment.setApp(app,callback);
            addFragment(outAppFragment, PassCodeOutAppFragment.class.getName(),PassCodeOutAppFragment.class.getName());
        }
    }
    public void showViewUsage(){
        if(getSupportFragmentManager().findFragmentByTag(StatisticsFragment.class.getName())==null){
            StatisticsFragment statisticsFragment = StatisticsFragment.newInstance();
            addFragment(statisticsFragment, StatisticsFragment.class.getName(), StatisticsFragment.class.getName());
        }
    }
    public void showViewTimeLimit(){
        if(getSupportFragmentManager().findFragmentByTag(LimitFragment.class.getName())==null){
            LimitFragment limitFragment = LimitFragment.newInstance();
            addFragment(limitFragment, LimitFragment.class.getName(),LimitFragment.class.getName());
        }
    }
    public void showViewUserSetting(){
        if(getSupportFragmentManager().findFragmentByTag(UserSettingFragment.class.getName())==null){
            UserSettingFragment userSettingFragment = UserSettingFragment.newInstance();
            addFragment(userSettingFragment,UserSettingFragment.class.getName(),UserSettingFragment.class.getName());
        }
    }
    protected void addFragment(@NonNull Fragment fragment,
                               @NonNull String fragmentTag,
                               @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(backStackStateName)
                .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                .add(R.id.rl_main, fragment, fragmentTag)
                .commitAllowingStateLoss();


    }

    public void addApp() {
        clearFragment();
        if(homeSocialFragment !=null)
            homeSocialFragment.updateData();
    }

    private void clearFragment(){
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        if (mWebView.getAlpha()==1){
            mWebView.close(400);
        }
    }

    private Boolean isExit = false;
    @Override
    public void onBackPressed() {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        if (slidingRootNav.isMenuOpened()) {
            slidingRootNav.closeMenu();
        } else if(index>0){
            super.onBackPressed();
        } else if (mWebView.getAlpha()==1){
            if(mWebView.canBack())
                mWebView.goBack();
            else
                mWebView.close(400);
        } else if (isExit) {
            finish();
        } else {
            Toast.makeText(this,"Press again to exit!",
                    Toast.LENGTH_SHORT).show();
            isExit = true;
            new Handler().postDelayed(() ->
                    isExit = false,3*1000);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WebViewFragment.KEY_SELECT_FILE) {
            if (mWebView.getUploadMessage() == null)
                return;
            mWebView.getUploadMessage().onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mWebView.setUploadMessage(null);
        }
    }


    public void requestNotification(Task<Boolean> task){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mRequestObject = PermissionUtil.with(MainActivity.this)
                    .request(POST_NOTIFICATIONS)
                    .onAllGranted(new Func() {
                        @Override protected void call() {
                            task.success(true);
                        }
                    })
                    .onAnyDenied(new Func() {
                        @Override
                        protected void call() {
                            task.success(false);
                        }
                    })
                    .ask(12);
        }else {
            task.success(true);
        }
    }

    public PermissionUtil.PermissionRequestObject mRequestObject;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mRequestObject!=null) mRequestObject.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeCallback();
        GameFullScreenHelper.clearGameView();
    }

    public void updateUser() {
        if(homeSocialFragment !=null)
            homeSocialFragment.updateUser();
    }
}
