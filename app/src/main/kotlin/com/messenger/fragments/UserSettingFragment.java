package com.messenger.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.ads.MyAds;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.utils.PermissionRequest;

public class UserSettingFragment extends BaseFragment {

    private static final int PICK_IMAGE = 5;

    private ImageView imgAvatar;

    public static UserSettingFragment newInstance() {
        Bundle args = new Bundle();
        UserSettingFragment fragment = new UserSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View chooseAvatar = view.findViewById(R.id.bt_select_avatar);
        View btUsage = view.findViewById(R.id.bt_usage);
        View btLimit = view.findViewById(R.id.bt_time_limit);
        EditText edtUserName = view.findViewById(R.id.edt_user_name);
        imgAvatar = view.findViewById(R.id.img_avatar);

        chooseAvatar.setOnClickListener(v -> {
            if(PermissionRequest.checkStore(activity,12)){
                    selectAvatar();
            }
        });

        btLimit.setOnClickListener(v ->  MyAds.showInterFull(activity, (value, where) -> activity.showViewTimeLimit()));
        btUsage.setOnClickListener(v ->  MyAds.showInterFull(activity, (value, where) -> activity.showViewUsage()));

        chooseAvatar.setOnTouchListener(new OnTouch());
        btUsage.setOnTouchListener(new OnTouch());
        btLimit.setOnTouchListener(new OnTouch());


        edtUserName.setText(SqDatabase.getUser().getName(activity));
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SqDatabase.getUser().setName(activity,s.toString());
                activity.updateUser();
            }
        });

        String img = SqDatabase.getUser().getAvatar(activity);
        if(img.length()>1){
            Glide.with(activity)
                    .load(img)
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.ic_account);
        }



    }

    private void selectAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent , PICK_IMAGE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE&&resultCode==RESULT_OK){
            if (data != null) {
                String url = data.getDataString();
                Glide.with(activity)
                        .load(url)
                        .into(imgAvatar);

                SqDatabase.getUser().setAvatar(activity,url);

                activity.updateUser();
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user_setting;
    }
}
