package com.messenger.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.SelectServiceAdapter;
import com.messenger.ads.MyAds;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.views.checkbox.OneCheckPro;

public class SelectServiceFragment extends BaseFragment {


    public static SelectServiceFragment newInstance() {
        Bundle args = new Bundle();
        SelectServiceFragment fragment = new SelectServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView rcv = view.findViewById(R.id.rv_list_ad);
        rcv.setLayoutManager(new GridLayoutManager(activity,3));
        rcv.setNestedScrollingEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rcv.setAdapter(new SelectServiceAdapter(activity, SqDatabase.getDb().getMessApps(activity,false)));
                view.findViewById(R.id.prLoad).setVisibility(View.GONE);

            }
        },300);

        View btNext = view.findViewById(R.id.bt_next);
        btNext.setOnClickListener(v -> {
            if(TextUtils.isEmpty(OneCheckPro.getTextCheck())){
                Toast.makeText(activity,"Please Select Service!", Toast.LENGTH_SHORT).show();
            }else {
                MyAds.showInterFull(activity, (value, where) -> activity.showViewAddAppAccountInfo());

            }
        });
        btNext.setOnTouchListener(new OnTouch());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OneCheckPro.clear();

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_select_service;
    }
}
