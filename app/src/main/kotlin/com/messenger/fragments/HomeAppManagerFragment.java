package com.messenger.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.App;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.Task;
import com.messenger.adapters.AppManagerAdapter;
import com.messenger.helper.LockDatabase;
import com.messenger.models.LockApp;
import com.messenger.utils.Utils;

import java.util.ArrayList;

public class HomeAppManagerFragment extends BaseFragment {

    private View vData;

    private TextView tvCountApp,tvEmpty;

    private View btnAdd;

    private View pbLoad;

    private RecyclerView rcvData;

    private AppManagerAdapter adapter;

    private ArrayList<LockApp> lockApps = new ArrayList<>();

    public static HomeAppManagerFragment newInstance() {
        Bundle args = new Bundle();
        HomeAppManagerFragment fragment = new HomeAppManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();

        setupData();

        loadData();
    }

    private void loadData() {
        pbLoad.setVisibility(View.VISIBLE);
        LockDatabase.get().getLockApp(ob -> activity.runOnUiThread(() -> {
            lockApps = (ArrayList<LockApp>) ob;
            updateData();
            if(lockApps.size()==0){
                showEmpty();
            }
        }));
    }

    private void setupData() {
        rcvData.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AppManagerAdapter(activity,lockApps) {
            @Override
            public void OnItemClick(LockApp lockApp, int position) {
                if(Utils.openApp(activity,lockApp.getAppId())){
                    updateTimeOpen(lockApp);
                }
            }

            @Override
            public void OnLockIconClick(LockApp lockApp, int position) {

                activity.requestNotification(new Task<Boolean>() {
                    @Override
                    public void success(Boolean result) {
                        if(result){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(activity)) {
                                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                    builder.setTitle("Messenger Notification");
                                    builder.setMessage("Messenger needs Overlay permission to show screen lock on locked apps.");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Enable", (dialogInterface, i) -> {
                                        if(!Settings.canDrawOverlays(activity)){
                                            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                            myIntent.setData(Uri.parse("package:" + activity.getPackageName()));
                                            startActivityForResult(myIntent, 101);
                                        }
                                    });
                                    builder.show();
                                    return;
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                if(!Utils.checkUsagePermission(activity)){
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setMessage("App Lock function request USAGE STATS permission!");
                                    dialog.setNegativeButton("Cancel",null);
                                    dialog.setPositiveButton("SET", (dialogInterface, i) -> Utils.startUsage(activity));
                                    dialog.show();
                                } else {
                                    checkLock(lockApp,position);
                                }
                            }else {
                                Toast.makeText(activity, "Function request Android 5 +", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }
        };

        rcvData.setAdapter(adapter);

        btnAdd.setOnClickListener(view -> activity.showViewDeviceApp(ob -> loadData()));
    }

    private void checkLock(LockApp lockApp, int position) {
        activity.showPassCodeAppLock(lockApp, ob -> {
            ArrayList<Object> values = (ArrayList<Object>) ob;
            boolean isCheck = (boolean) values.get(0);
            lockApp.setLock(isCheck);
            lockApp.setPin((String) values.get(1));
            adapter.notifyItemChanged(position);

            if(isCheck){
                App.get().startLockAppService();
            }else {
                if(LockDatabase.get().isLockAppEnableEmpty()){
                    App.get().stopLockAppService();
                }
            }
        });
    }

    private void updateTimeOpen(LockApp lockApp){
        LockDatabase.get().upTimeOpen(lockApp);
        lockApp.setTimeOpen(lockApp.getTimeOpen()+1);
        adapter.sortList();
    }

    @SuppressLint("SetTextI18n")
    private void updateData() {
        pbLoad.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        vData.setVisibility(View.VISIBLE);
        tvCountApp.setText("Total: "+lockApps.size()+" "+(lockApps.size()>1?"apps":"app"));
        adapter.setData(lockApps);
    }

    private void initViews() {
        vData = view.findViewById(R.id.view_data);
        tvCountApp = view.findViewById(R.id.tv_count_app);
        tvEmpty = view.findViewById(R.id.tv_empty);
        btnAdd = view.findViewById(R.id.bt_add);
        pbLoad = view.findViewById(R.id.pb_load);
        rcvData= view.findViewById(R.id.rcv_data);
    }

    private void showEmpty(){
        pbLoad.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home_app_lock;
    }

}
