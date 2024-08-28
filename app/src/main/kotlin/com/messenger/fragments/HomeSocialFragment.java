package com.messenger.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.activities.HTML5Activity;
import com.messenger.activities.ListGameActivity;
import com.messenger.adapters.AppMainAdapter;
import com.messenger.ads.MyAds;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;
import com.messenger.utils.Constant;
import com.messenger.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.Objects;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class HomeSocialFragment extends BaseFragment {


    private View mAdd;

    private PulsatorLayout rippleView;

    private RecyclerView rvApps;

    private View btEditUser;

    private AppMainAdapter mainAdapter;

    private ImageView imgAvatar;

    private TextView tvUserName,tvCountAccount;


    public static HomeSocialFragment newInstance() {
        Bundle args = new Bundle();
        HomeSocialFragment fragment = new HomeSocialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        setupAdd();

        setupList();

        setupUser();

        updateUser();

        if(PreferenceUtil.getInstance(activity).getValue(Constant.SharePrefKey.NATIVE_ADS, "no").equals("yes")){
            MyAds.initNativeMain(view.findViewById(R.id.adLayout));
            view.findViewById(R.id.adLayout).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.adLayout).setVisibility(View.GONE);
        }
    }

    private void initView() {
        mAdd = view.findViewById(R.id.bt_add);
        rippleView = view.findViewById(R.id.pulsator);
        rvApps = view.findViewById(R.id.rcvList);
        btEditUser = view.findViewById(R.id.bt_edit_user);
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvCountAccount = view.findViewById(R.id.tv_count_account);


        if(PreferenceUtil.getInstance(activity).getValue(Constant.SharePrefKey.GAME, "no").equals("yes")){
            view.findViewById(R.id.layoutGame).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.layoutGame).setVisibility(View.GONE);
        }


        view.findViewById(R.id.btn2048).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAds.showInterFull(activity, (value, where) -> {
                    HTML5Activity.linkGame = PreferenceUtil.getInstance(activity).getValue(Constant.SharePrefKey.GAME1, "https://dcrespo3d.github.io/2048-html5/");
                    startActivity(new Intent(getContext(), HTML5Activity.class));
                });

            }
        });

        view.findViewById(R.id.btnFlapyBird).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAds.showInterFull(activity, (value, where) -> {
                    HTML5Activity.linkGame = PreferenceUtil.getInstance(activity).getValue(Constant.SharePrefKey.GAME2, "https://chaping.github.io/game/flappy-bird/");
                    startActivity(new Intent(getContext(), HTML5Activity.class));
                });

            }
        });

        view.findViewById(R.id.btnSeeMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ListGameActivity.class));
            }
        });
    }

    public void updateUser(){

        String img = SqDatabase.getUser().getAvatar(activity);

        if(img.length()>1){
            Glide.with(this)
                    .load(img)
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.ic_account);
        }

        tvUserName.setText(SqDatabase.getUser().getName(activity));
    }

    @SuppressLint("SetTextI18n")
    public void updateData(){
        mainAdapter.setData(SqDatabase.getDb().getMessApps(activity,true));
        tvCountAccount.setText(mainAdapter.getItemCount()+" Account");
    }


    private void setupAdd() {
        mAdd.setOnClickListener(v ->
                MyAds.showInterFull(activity,
                        (value, where) -> activity.showViewAddApp()));
        mAdd.setOnTouchListener(new OnTouch());

        rippleView.start();
    }

    private void setupUser() {
        btEditUser.setOnClickListener(v ->
                MyAds.showInterFull(activity, (value, where) ->
                        activity.showViewUserSetting()));
        btEditUser.setOnTouchListener(new OnTouch());
    }



    private void setupList() {
        rvApps.setLayoutManager(new GridLayoutManager(activity,3));
        ArrayList<MessApp> messApps = new ArrayList<>();

        rvApps.setAdapter(mainAdapter = new AppMainAdapter(activity, messApps) {
            @Override
            public void OnItemClick(MessApp messApp, int position) {
                MyAds.showInterFull(activity, (value, where) ->
                        activity.mWebView.start(messApp));

            }

            @Override
            public void OnMenuClick(MessApp messApp, int position) {
                showDialogSelectAction(messApp,position);
            }
        });

        updateData();
    }

    @SuppressLint("SetTextI18n")
    private void showDialogSelectAction(final MessApp messApp, final int position) {

        final BottomSheetDialog dialog = new BottomSheetDialog(activity,R.style.Dialog_Transparent);
        dialog.setContentView(R.layout.dialog_select_action);

        final View mainDialog = dialog.findViewById(R.id.main_dialog);
        View btDelete = dialog.findViewById(R.id.bt_delete_dialog);
        View btEdit = dialog.findViewById(R.id.bt_edit_dialog);
        View btCancel = dialog.findViewById(R.id.bt_cancel_dialog);
        TextView tvValue = dialog.findViewById(R.id.tv_value_dialog);
        Objects.requireNonNull(tvValue).setText(messApp.getName()+" - "+messApp.getUsename());

        Objects.requireNonNull(btCancel).setOnClickListener(v ->
                dismissDialog(dialog,mainDialog));
        btCancel.setOnTouchListener(new OnTouch());
        Objects.requireNonNull(btEdit).setOnClickListener(v -> {
            activity.showViewUpdateAppAccountInfo(messApp);
            dismissDialog(dialog,mainDialog);
        });
        btEdit.setOnTouchListener(new OnTouch());
        Objects.requireNonNull(btDelete).setOnClickListener(v -> {
            if(SqDatabase.getDb().deleteApp(activity,messApp.getName())==-1){
                Toast.makeText(activity, "Fail!", Toast.LENGTH_SHORT).show();
            }else {
                mainAdapter.remove(position);
            }
            dismissDialog(dialog,mainDialog);


        });
        btDelete.setOnTouchListener(new OnTouch());


        Objects.requireNonNull(mainDialog).animate().scaleX(1)
                .scaleY(1)
                .translationY(0)
                .alpha(1)
                .setDuration(300)
                .start();

        dialog.show();
    }


    private void dismissDialog(final Dialog dialog, final View view){
        Objects.requireNonNull(view).animate().scaleX(0)
                .scaleY(0)
                .translationY(500)
                .alpha(0)
                .setDuration(300)
                .withEndAction(dialog::dismiss)
                .start();
    }



    @Override
    public int getLayout() {
        return R.layout.fragment_home_social;
    }
}
