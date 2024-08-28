package com.messenger.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.DeviceAppAdapter;
import com.messenger.callbacks.OGCallback;
import com.messenger.helper.LockDatabase;
import com.messenger.models.DeviceApp;

import java.util.ArrayList;

public class DeviceAppFragment extends BaseFragment {

    private View btnSave;

    private View pbLoad;

    private RecyclerView rcvDeviceApp;

    private DeviceAppAdapter adapter;

    private ArrayList<DeviceApp> deviceApps = new ArrayList<>();

    private OGCallback onSaveClick;

    private boolean isLoaded = false;

    private SwipeRefreshLayout refreshLayout;

    private TextView tvLoad;

    public static DeviceAppFragment newInstance() {
        Bundle args = new Bundle();
        DeviceAppFragment fragment = new DeviceAppFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnSaveClickListen(OGCallback onSaveClick) {
        this.onSaveClick = onSaveClick;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();

        new Handler().postDelayed(() -> {
            if(pbLoad!=null&&isAdded()){
                setupData();
                initRefresh();
                LockDatabase.get().getInstallApp(ob -> activity.runOnUiThread(() -> {
                    deviceApps = (ArrayList<DeviceApp>) ob;
                    pbLoad.setVisibility(View.GONE);
                    tvLoad.setVisibility(View.GONE);
                    adapter.setData(deviceApps);
                    isLoaded = true;
                }));
            }
        },500);
    }

    private void setupData() {
        rcvDeviceApp.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new DeviceAppAdapter(activity,deviceApps);

        rcvDeviceApp.setAdapter(adapter);

        btnSave.setOnClickListener(view -> {
            if(!isLoaded) return;
            Dialog dialog = new Dialog(activity,R.style.Dialog_Transparent);
            dialog.setContentView(R.layout.dialog_saving);
            dialog.setCancelable(false);
            dialog.show();
            LockDatabase.get().saveAppShow(deviceApps, ob -> {
                activity.runOnUiThread(() -> {
                    dialog.dismiss();
                    onSaveClick.callback(0);
                    activity.onBackPressed();
                });

            });
        });
    }

    private void initViews() {
        btnSave = view.findViewById(R.id.bt_save);
        pbLoad = view.findViewById(R.id.pb_load);
        rcvDeviceApp = view.findViewById(R.id.rcv_device_app);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        tvLoad = view.findViewById(R.id.tv_load);
    }

    private void initRefresh() {
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this::reloadDeviceApp);
    }

    private void reloadDeviceApp() {
        rcvDeviceApp.setVisibility(View.GONE);
        tvLoad.setVisibility(View.VISIBLE);
        pbLoad.setVisibility(View.VISIBLE);
        tvLoad.setText("");
        isLoaded = false;
        LockDatabase.get().reloadDb(ob -> {
            if(ob instanceof String&&tvLoad!=null&&isAdded()){
                activity.runOnUiThread(() -> tvLoad.setText((String)ob));

            }else if(ob instanceof Integer&&tvLoad!=null&&isAdded()){
                activity.runOnUiThread(() -> LockDatabase.get().getInstallApp(ob1 -> activity.runOnUiThread(() -> {
                    deviceApps = (ArrayList<DeviceApp>) ob1;
                    pbLoad.setVisibility(View.GONE);
                    tvLoad.setVisibility(View.GONE);
                    rcvDeviceApp.setVisibility(View.VISIBLE);
                    adapter.setData(deviceApps);
                    isLoaded = true;
                    hideRefresh();
                })));
            }
        });
    }


    private void hideRefresh(){
        new Handler().postDelayed(() -> refreshLayout.setRefreshing(false),1000);
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_device_app;
    }
}
