package com.messenger.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.KeyPassCodeAdapter;
import com.messenger.callbacks.OGCallback;
import com.messenger.helper.LockDatabase;
import com.messenger.models.Key;
import com.messenger.models.LockApp;
import com.messenger.utils.Utils;

import java.util.ArrayList;

public class PassCodeOutAppFragment extends BaseFragment {

    private StringBuffer passCode = new StringBuffer();

    private String passCheck;

    private RecyclerView rcvKeyInput;

    private ImageView imgIn1,imgIn2,imgIn3,imgIn4;

    private TextView tvTitle;

    private ImageView iconLock;

    private View viewIn;

    private LockApp app;

    private OGCallback callback;


    public static PassCodeOutAppFragment newInstance() {
        Bundle args = new Bundle();
        PassCodeOutAppFragment fragment = new PassCodeOutAppFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public void setApp(LockApp app,OGCallback callback) {
        this.app = app;
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        setIconApp();

        setupKey();
    }

    private void setIconApp() {
        iconLock.setImageDrawable(app.getIcon());
    }

    private void setupKey() {

        String title;

        if(app.isLock()){
            title = activity.getString(R.string.turn_passcode_off);
        }else {
            title = activity.getString(R.string.new_pass_code);
        }

        tvTitle.setText(title);

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
        rcvKeyInput.setLayoutManager(new GridLayoutManager(activity,3));
        rcvKeyInput.setAdapter(new KeyPassCodeAdapter(activity,keys) {
            @Override
            public void OnItemClick(String wl) {
                if(wl.equals("11")){
                    deleteKey();
                }else {
                    inputKey(Integer.parseInt(wl));
                }
            }
        });

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
        if(app.isLock()){
            if(passCode.toString().equals(app.getPin())){

                LockDatabase.get().updateAppLock(app.getAppId(),false,passCheck);
                ArrayList<Object> value = new ArrayList<>();
                value.add(false);
                value.add(app.getPin());

                callback.callback(value);

                activity.onBackPressed();

                Utils.vibrator(activity);
                Toast.makeText(activity,app.getName()+" "+activity.getString(R.string.passcode_off), Toast.LENGTH_SHORT).show();
            }else {
                startAnimationError();
                Utils.vibrator(activity);
            }
        }else {
            if(passCheck==null){
                passCheck = passCode.toString();
                startAnimationInputAgain();
            }else {
                if(passCheck.equals(passCode.toString())){

                    LockDatabase.get().updateAppLock(app.getAppId(),true,passCheck);

                    ArrayList<Object> value = new ArrayList<>();
                    value.add(true);
                    value.add(passCode.toString());
                    callback.callback(value);

                    activity.onBackPressed();

                    Utils.vibrator(activity);
                    Toast.makeText(activity,app.getName()+" "+ activity.getString(R.string.passcode_on), Toast.LENGTH_SHORT).show();
                }else {
                    startAnimationError();
                    Utils.vibrator(activity);
                    passCheck = null;
                    tvTitle.setText(activity.getString(R.string.new_pass_code));
                }
            }
        }
    }


    private void startAnimationInputAgain(){
        viewIn.animate().translationX(-activity.getResources().getDisplayMetrics().widthPixels)
                .setDuration(300)
                .withEndAction(() ->
                        viewIn.animate().translationX(activity.getResources().getDisplayMetrics().widthPixels)
                        .setDuration(0)
                        .withEndAction(() ->
                                viewIn.animate().translationX(0)
                                .setDuration(300).withEndAction(PassCodeOutAppFragment.this::clearIndicator).start()).start()).start();
        tvTitle.animate().translationX(-activity.getResources().getDisplayMetrics().widthPixels)
                .setDuration(300)
                .withEndAction(() ->
                        tvTitle.animate().translationX(activity.getResources().getDisplayMetrics().widthPixels)
                        .setDuration(0)
                        .withEndAction(() -> {
                            tvTitle.setText(activity.getString(R.string.enter_passcode_again));
                            tvTitle.animate().translationX(0)
                                    .setDuration(300).withEndAction(PassCodeOutAppFragment.this::clearIndicator).start();
                        }).start()).start();
    }

    private void startAnimationError() {
        viewIn.animate().translationX(100).setDuration(50).withEndAction(() ->
                viewIn.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        viewIn.animate().translationX(70).setDuration(50).withEndAction(() ->
                                viewIn.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                         viewIn.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                 viewIn.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                         viewIn.animate().translationX(0).setDuration(50).withEndAction(this::clearIndicator).start()).start()).start()).start()).start()).start()).start();
        iconLock.animate().translationX(100).setDuration(50).withEndAction(() ->
                iconLock.animate().translationX(-100).setDuration(100).withEndAction(() ->
                        iconLock.animate().translationX(70).setDuration(50).withEndAction(() ->
                                iconLock.animate().translationX(-70).setDuration(50).withEndAction(() ->
                                        iconLock.animate().translationX(50).setDuration(50).withEndAction(() ->
                                                 iconLock.animate().translationX(-25).setDuration(50).withEndAction(() ->
                                                         iconLock.animate().translationX(0).setDuration(50).start()).start()).start()).start()).start()).start()).start();
    }

    private void clearIndicator(){
        passCode = new StringBuffer();
        imgIn1.setSelected(false);
        imgIn2.setSelected(false);
        imgIn3.setSelected(false);
        imgIn4.setSelected(false);
        viewIn.requestLayout();
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

    private void initView() {
        rcvKeyInput = view.findViewById(R.id.rcvKeyInput);
        imgIn1 = view.findViewById(R.id.imgIn1);
        imgIn2 = view.findViewById(R.id.imgIn2);
        imgIn3 = view.findViewById(R.id.imgIn3);
        imgIn4 = view.findViewById(R.id.imgIn4);
        viewIn = view.findViewById(R.id.viewIn);
        tvTitle = view.findViewById(R.id.tv_title);
        iconLock = view.findViewById(R.id.iconLock);
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_pass_code;
    }
}
