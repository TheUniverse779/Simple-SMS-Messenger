package com.messenger.activities;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.KeyPassCodeAdapter;
import com.messenger.helper.LockDatabase;
import com.messenger.models.Key;
import com.messenger.models.LockApp;
import com.messenger.services.LockAppService;
import com.messenger.utils.Utils;

import java.util.ArrayList;

public class UnlockAppActivity extends AppCompatActivity {

    private LockApp lockApp;

    private StringBuffer passCode = new StringBuffer();

    private RecyclerView rcvKeyInput;

    private ImageView imgIn1,imgIn2,imgIn3,imgIn4;

    private TextView tvTitle;

    private View viewIn,close;

    private ImageView iconLock;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hideSystemUI();
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch(Exception ignore){}
        clearSystemView();
        setContentView(R.layout.activity_unlock_app);

        String appId = getIntent().getStringExtra(LockAppService.KEY_APP_ID);
        if(appId!=null){
            lockApp = LockDatabase.get().getLockApp(appId);
            if(lockApp==null)finish();
        }else {
            finish();
        }

        initView();

        setup();

        iconLock.setImageDrawable(lockApp.getIcon());


    }

    private void setup() {
        tvTitle.setText(getString(R.string.enter_passcode));
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(new Key("1",""));
        keys.add(new Key("2","ABC"));
        keys.add(new Key("3","DEF"));
        keys.add(new Key("4","GHI"));
        keys.add(new Key("5","JKL"));
        keys.add(new Key("6","MNO"));
        keys.add(new Key("7","PQRS"));
        keys.add(new Key("8","TUV"));
        keys.add(new Key("9","WXYZ"));
        keys.add(new Key("10",""));
        keys.add(new Key("0",""));
        keys.add(new Key("11","Delete"));
        rcvKeyInput.setLayoutManager(new GridLayoutManager(this,3));
        rcvKeyInput.setAdapter(new KeyPassCodeAdapter(this,keys) {
            @Override
            public void OnItemClick(String wl) {
                if(wl.equals("11")){
                    deleteKey();
                }else {
                    inputKey(Integer.parseInt(wl));
                }
            }
        });

        close.setOnClickListener(v -> {
            Utils.goHome(this);
            LockDatabase.get().setAppCurrent("android");
            finish();
        });
    }

    private void deleteKey() {
        if(imgIn4.isSelected()){
            imgIn4.setSelected(false);
        }else if(imgIn3.isSelected()){
            imgIn3.setSelected(false);
        }else if(imgIn2.isSelected()){
            imgIn2.setSelected(false);
        } else {
            imgIn1.setSelected(false);
        }

        if(passCode.length()>0){
            passCode.replace(passCode.length()-1,passCode.length(),"");
        }

        viewIn.requestLayout();
    }

    private void inputKey(int i) {

        if(passCode.length()<4){
            passCode.append(i);
        } else {
            return;
        }

        if(passCode.length()==1){
            imgIn1.setSelected(true);
        }else if(passCode.length()==2){
            imgIn2.setSelected(true);
        } else if(passCode.length()==3){
            imgIn3.setSelected(true);
        }else {
            imgIn4.setSelected(true);
            imgIn4.requestLayout();
            check();
        }

        viewIn.requestLayout();
    }

    private void check() {
        if(passCode.toString().equals(lockApp.getPin())){
            Utils.vibrator(this);
            finish();
        } else {
            startAnimationError();
            Utils.vibrator(this);
        }
    }
    private void startAnimationError() {
        viewIn.animate().translationX(100).setDuration(50).withEndAction(() ->
                viewIn.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        viewIn.animate().translationX(70).setDuration(50).withEndAction(() -> {
                            viewIn.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                    viewIn.animate().translationX(50).setDuration(50).withEndAction(() ->
                                            viewIn.animate().translationX(-25).setDuration(50).withEndAction(() -> {
                                                viewIn.animate().translationX(0).setDuration(50).withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        clearIndicator();
                                                    }
                                                }).start();
                                            }).start()).start()).start();
                        }).start()).start()).start();
        iconLock.animate().translationX(100).setDuration(50).withEndAction(() ->
                iconLock.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        iconLock.animate().translationX(70).setDuration(50).withEndAction(() -> {
                            iconLock.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                    iconLock.animate().translationX(50).setDuration(50).withEndAction(() ->
                                            iconLock.animate().translationX(-25).setDuration(50).withEndAction(() -> {
                                                iconLock.animate().translationX(0).setDuration(50).start();
                                            }).start()).start()).start();
                        }).start()).start()).start();
    }

    private void clearIndicator(){
        passCode = new StringBuffer();
        imgIn1.setSelected(false);
        imgIn2.setSelected(false);
        imgIn3.setSelected(false);
        imgIn4.setSelected(false);
        viewIn.requestLayout();
    }



    private void initView() {
        rcvKeyInput = findViewById(R.id.rcvKeyInput);
        imgIn1 = findViewById(R.id.imgIn1);
        imgIn2 = findViewById(R.id.imgIn2);
        imgIn3 = findViewById(R.id.imgIn3);
        imgIn4 = findViewById(R.id.imgIn4);
        viewIn = findViewById(R.id.viewIn);
        tvTitle = findViewById(R.id.tv_title);
        iconLock = findViewById(R.id.iconLock);
        close = findViewById(R.id.bt_back);
    }

    private void clearSystemView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    private void hideSystemUI() {

        try{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch(Exception ignore){}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {

    }

}
