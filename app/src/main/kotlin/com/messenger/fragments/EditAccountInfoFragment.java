package com.messenger.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.ads.MyAds;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;

public class EditAccountInfoFragment extends BaseFragment {

    private MessApp messApp;

    public static EditAccountInfoFragment newInstance() {
        Bundle args = new Bundle();
        EditAccountInfoFragment fragment = new EditAccountInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setMessApp(MessApp messApp) {
        this.messApp = messApp;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvAccount = view.findViewById(R.id.ac_name);
        tvAccount.setText("Account's Name ("+ messApp.getName() +") ");

        final EditText edtAccountName = view.findViewById(R.id.edtAccountName);
        edtAccountName.setText(messApp.getUsename());

        View btDone = view.findViewById(R.id.bt_done);
        btDone.setOnClickListener(v -> {
            if(TextUtils.isEmpty(edtAccountName.getText().toString())){
                Toast.makeText(activity, "Please input use name!", Toast.LENGTH_SHORT).show();
            }else {
                if(SqDatabase.getDb().updateAppUserName(activity,messApp.getName(),edtAccountName.getText().toString())==-1){
                    Toast.makeText(activity, "Fail!", Toast.LENGTH_SHORT).show();
                }else {
                    MyAds.showInterFull(activity, (value, where) -> activity.addApp());

                }

            }
        });
        btDone.setOnTouchListener(new OnTouch());

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_account_info;
    }
}
