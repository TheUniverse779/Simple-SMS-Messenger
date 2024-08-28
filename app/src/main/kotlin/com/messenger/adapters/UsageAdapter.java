package com.messenger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.ads.MyAds;
import com.messenger.helper.OnTouch;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;
import com.messenger.views.PieChartView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class UsageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NATIVE_AD = 0;

    private static final int VIEW_TYPE_ITEM = 1;

    private static final int VIEW_TYPE_ITEM_CHART = 3;

    private Context context;

    private ArrayList<MessApp> messApps;

    private View nativeView;

    protected UsageAdapter(Context ac, ArrayList<MessApp> messApps) {
        this.context = ac;
        this.messApps = messApps;
        nativeView = LayoutInflater.from(context).inflate(R.layout.native_ads_list,null);
        MyAds.initNativeList(nativeView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType==VIEW_TYPE_ITEM){
            view = LayoutInflater.from(context).inflate(R.layout.item_usage, parent, false);
            return new ItemHolder(view);
        }else if(viewType==VIEW_TYPE_ITEM_CHART){
            view = LayoutInflater.from(context).inflate(R.layout.item_usage_view_chart, parent, false);
            return new ChartHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_native_ads, parent, false);
            return new NativeAdHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemHolder){
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.tvAppName.setText(messApps.get(position).getName());

            long launch = messApps.get(position).getOpenCount();

            String launchString = launch>1?launch+" times":launch+" time";

            itemHolder.tvTimeOpen.setText(launchString);

            Glide.with(context)
                    .load(getIcon(messApps.get(position).getIcon()))
                    .into(itemHolder.icon);
        } else if(holder instanceof ChartHolder){
            ChartHolder chartHolder = (ChartHolder) holder;
            synChart(chartHolder.itemView);
        }else {
            NativeAdHolder adHolder = (NativeAdHolder) holder;
            adHolder.nativeAD.removeAllViews();
            try {
                adHolder.nativeAD.addView(nativeView);
            }catch (Exception ignored){}
        }


    }

    public void setMessApps(ArrayList<MessApp> messApps) {
        this.messApps = messApps;
        Log.e("hoangSize",messApps.size()+"adapter");
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (messApps.get(position).getUrl()!=null){
            if(messApps.get(position).getUrl().equals("chart")){
                return VIEW_TYPE_ITEM_CHART;
            }else {
                return VIEW_TYPE_ITEM;
            }
        } else {
            return VIEW_TYPE_NATIVE_AD;
        }
    }

    @Override
    public int getItemCount() {
        return messApps.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private TextView tvAppName,tvTimeOpen;

        private ImageView icon;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            tvTimeOpen = itemView.findViewById(R.id.tv_time_open);
            icon = itemView.findViewById(R.id.img_app);
            itemView.setOnClickListener(v -> {
                OnItemClick(messApps.get(getAdapterPosition()));
            });
            itemView.setOnTouchListener(new OnTouch());
        }
    }


    class ChartHolder extends RecyclerView.ViewHolder{
        ChartHolder(final View itemView) {
            super(itemView);
        }

    }

    class NativeAdHolder extends RecyclerView.ViewHolder{
        private FrameLayout nativeAD;
        NativeAdHolder(final View itemView) {
            super(itemView);
            nativeAD = itemView.findViewById(R.id.native_item);
        }
    }

    private void synChart(View view){
        long totalTime = 0;
        long timeShow = 0;

        ArrayList<MessApp> messApps = SqDatabase.getDb().getAllMessApps(context);
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
            pieceDataHolders.add(new PieChartView.PieceDataHolder(1,Color.parseColor("#43A047"), "Other (100%)"));
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
    }

    private Bitmap getIcon(String s){
        try {
            InputStream inputStream = context.getAssets().open("oh/"+s+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract void OnItemClick(MessApp messApp);
}
