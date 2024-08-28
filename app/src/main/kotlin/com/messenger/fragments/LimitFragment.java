package com.messenger.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.LimitAdapter;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;


public class LimitFragment extends BaseFragment {


    public static LimitFragment newInstance() {
        Bundle args = new Bundle();
        LimitFragment fragment = new LimitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WeakReference<LimitFragment> weakReference = new WeakReference<>(this);

        new Handler().postDelayed(() -> {

            if(weakReference.get()==null)return;

            RecyclerView rvUsage = view.findViewById(R.id.rv_usage);
            rvUsage.setLayoutManager(new LinearLayoutManager(activity));

            ArrayList<MessApp> messApps = SqDatabase.getDb().getAllMessApps(activity);

            Collections.sort(messApps, (s1, s2) -> {
                if (s1.getTimeLimit() > s2.getTimeLimit() )
                    return -1;
                else if (s1.getTimeLimit() < s2.getTimeLimit())
                    return 1;
                return 0;
            });
            rvUsage.setAdapter(new LimitAdapter(activity,messApps));

            view.findViewById(R.id.pb_load).setVisibility(View.GONE);

        },350);

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_limit;
    }
}
