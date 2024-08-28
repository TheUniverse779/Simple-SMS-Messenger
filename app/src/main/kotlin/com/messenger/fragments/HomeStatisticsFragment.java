package com.messenger.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.UsageAdapter;
import com.messenger.ads.MyAds;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class HomeStatisticsFragment extends BaseFragment {


    private RecyclerView rvUsage;

    private UsageAdapter usageAdapter;

    private  ArrayList<MessApp> messApps;


    public static HomeStatisticsFragment newInstance() {
        Bundle args = new Bundle();
        HomeStatisticsFragment fragment = new HomeStatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WeakReference<HomeStatisticsFragment> weakReference = new WeakReference<>(this);

        rvUsage = view.findViewById(R.id.rv_usage_home);
        rvUsage.setLayoutManager(new LinearLayoutManager(activity));

        new Handler().postDelayed(() -> {

            if(weakReference.get()==null)return;

            initData();

        },350);

    }

    private void initData() {
        messApps = SqDatabase.getDb().getAllMessAppsWithAdsType(activity,5);

        Log.e("hoangSize",messApps.size()+"home");

        usageAdapter = new UsageAdapter(activity, messApps) {
            @Override
            public void OnItemClick(MessApp messApp) {
                MyAds.showInterFull(activity, (value, where) ->
                        activity.mWebView.start(messApp));
            }
        };

        rvUsage.setAdapter(usageAdapter);

        view.findViewById(R.id.pb_load).setVisibility(View.GONE);
    }


    public void updateData() {
        messApps = SqDatabase.getDb().getAllMessAppsWithAdsType(activity,5);
        usageAdapter.setMessApps(messApps);

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home_statistics;
    }
}
