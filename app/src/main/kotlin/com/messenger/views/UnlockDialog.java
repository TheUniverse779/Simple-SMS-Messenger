package com.messenger.views;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.App;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.KeyPassCodeAdapter;
import com.messenger.helper.SqDatabase;
import com.messenger.models.Key;
import com.messenger.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public abstract class UnlockDialog extends Dialog {

    private Activity activity;

    private View mainDialog;

    private StringBuffer passCode = new StringBuffer();

    private RecyclerView rcvKeyInput;

    private ImageView imgIn1,imgIn2,imgIn3,imgIn4;

    private TextView tvTitle;

    private View iconLock,viewIn,close;


    public UnlockDialog(@NonNull Activity activity) {
        super(activity, R.style.Dialog_FullScreen);
        this.activity = activity;

        initView();

        setup();
    }

    private void setup() {

        setCancelable(false);

        tvTitle.setText(getContext().getString(R.string.enter_passcode));

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
        rcvKeyInput.setLayoutManager(new GridLayoutManager(getContext(),3));
        rcvKeyInput.setAdapter(new KeyPassCodeAdapter(getContext(),keys) {
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
            activity.finish();
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
        if(passCode.toString().equals(SqDatabase.getUser().getPassCode(getContext()))){
            Utils.vibrator(getContext());
            onSuccess();
            dismissDialog();
        }else {
            startAnimationError();
            Utils.vibrator(getContext());
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
        setContentView(R.layout.dialog_unlock);
        rcvKeyInput = findViewById(R.id.rcvKeyInput);
        imgIn1 = findViewById(R.id.imgIn1);
        imgIn2 = findViewById(R.id.imgIn2);
        imgIn3 = findViewById(R.id.imgIn3);
        imgIn4 = findViewById(R.id.imgIn4);
        viewIn = findViewById(R.id.viewIn);
        tvTitle = findViewById(R.id.tv_title);
        mainDialog = findViewById(R.id.main_dialog);
        iconLock = findViewById(R.id.iconLock);
        close = findViewById(R.id.bt_back);
    }


    public abstract void onSuccess();

    @Override
    public void show() {
        if(SqDatabase.getUser().isLock(getContext())){
            Objects.requireNonNull(mainDialog).animate().scaleX(1)
                    .scaleY(1)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(500)
                    .start();
            App.get().sendTracker(getClass().getSimpleName());
            super.show();
        } else {
            onSuccess();
            this.dismiss();
        }
    }



    private void dismissDialog(){
        Objects.requireNonNull(mainDialog).animate()
                .setDuration(300)
                .translationY(-activity.getResources().getDisplayMetrics().heightPixels)
                .withEndAction(this::dismiss)
                .start();
    }
}
