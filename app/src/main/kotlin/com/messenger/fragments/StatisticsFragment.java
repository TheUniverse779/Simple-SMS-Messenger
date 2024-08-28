package com.messenger.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.UsageAdapter;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;
import com.messenger.views.PieChartView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends BaseFragment {


    public static StatisticsFragment newInstance() {
        Bundle args = new Bundle();
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WeakReference<StatisticsFragment> weakReference = new WeakReference<>(this);

        new Handler().postDelayed(() -> {

            if(weakReference.get()==null)return;

            RecyclerView rvUsage = view.findViewById(R.id.rv_usage);
            rvUsage.setNestedScrollingEnabled(false);
            rvUsage.setLayoutManager(new LinearLayoutManager(activity));

            ArrayList<MessApp> messApps = SqDatabase.getDb().getAllMessApps(activity);
            rvUsage.setAdapter(new UsageAdapter(activity, messApps) {
                @Override
                public void OnItemClick(MessApp messApp) {

                }
            });

            long totalTime = 0;
            long timeShow = 0;
            for (MessApp messApp:messApps){
                totalTime = totalTime + messApp.getOpenCount();
            }

            PieChartView pieChartView = view.findViewById(R.id.pie_chart);
            List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();

            if(messApps.get(0).getOpenCount()>0){

                long pt = (100/totalTime)*messApps.get(0).getOpenCount();
                pieceDataHolders.add(new PieChartView.PieceDataHolder(
                        messApps.get(0).getOpenCount(),Color.parseColor("#E53935"), messApps.get(0).getName()+" ("+pt+"%)"));
                timeShow = timeShow + messApps.get(0).getOpenCount();
            } else {
                pieChartView.setVisibility(View.GONE);
            }

            if(messApps.get(1).getOpenCount()>0){

                long pt = (100/totalTime)*messApps.get(1).getOpenCount();
                pieceDataHolders.add(new PieChartView.PieceDataHolder(
                        messApps.get(1).getOpenCount(),Color.parseColor("#5E35B1"), messApps.get(1).getName()+" ("+pt+"%)"));
                timeShow = timeShow + messApps.get(1).getOpenCount();
            }


            if(messApps.get(2).getOpenCount()>0){

                long pt = (100/totalTime)*messApps.get(2).getOpenCount();
                pieceDataHolders.add(new PieChartView.PieceDataHolder(
                        messApps.get(2).getOpenCount(),Color.parseColor("#1E88E5"), messApps.get(2).getName()+" ("+pt+"%)"));
                timeShow = timeShow + messApps.get(2).getOpenCount();
            }

            if(messApps.get(3).getOpenCount()>0){

                long pt = (100/totalTime)*messApps.get(3).getOpenCount();
                pieceDataHolders.add(new PieChartView.PieceDataHolder(
                        messApps.get(3).getOpenCount(),Color.parseColor("#FB8C00"), messApps.get(3).getName()+" ("+pt+"%)"));
                timeShow = timeShow + messApps.get(3).getOpenCount();
            }

            if(totalTime-timeShow>0){
                long pt = (100/totalTime)*(totalTime-timeShow);
                pieceDataHolders.add(new PieChartView.PieceDataHolder(
                        totalTime-timeShow,Color.parseColor("#43A047"),"Other"+" ("+pt+"%)"));
            }

            pieChartView.setData(pieceDataHolders);

            view.findViewById(R.id.pb_load).setVisibility(View.GONE);

        },350);


    }

    @Override
    public int getLayout() {
        return R.layout.fragment_statistics;
    }
}
