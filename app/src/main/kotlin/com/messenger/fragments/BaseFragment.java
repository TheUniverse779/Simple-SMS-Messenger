package com.messenger.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.messenger.App;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.activities.MainActivity;
import com.messenger.helper.OnTouch;


public abstract class BaseFragment extends Fragment {

    View view;

    MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayout(),container,false);
        back();
        return view ;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            App.get().sendTracker(getClass().getSimpleName());
        }
    }

    private void back(){
        View bt_back = view.findViewById(R.id.bt_back);
        if(bt_back!=null){
            bt_back.setOnClickListener(v -> activity.onBackPressed());
            bt_back.setOnTouchListener(new OnTouch());
        }

    }

    public abstract int getLayout();
}
