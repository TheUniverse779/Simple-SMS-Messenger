package com.messenger.fragments;

import android.os.Bundle;

import com.simplemobiletools.smsmessenger.R;

public class ReadNewsFragment extends BaseFragment {

    public static ReadNewsFragment newInstance() {
        Bundle args = new Bundle();
        ReadNewsFragment fragment = new ReadNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_read_news;
    }
}
