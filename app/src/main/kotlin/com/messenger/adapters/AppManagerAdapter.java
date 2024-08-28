package com.messenger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.messenger.models.LockApp;

import java.util.ArrayList;
import java.util.Collections;

public abstract class AppManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NATIVE_AD = 0;

    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;

    private ArrayList<LockApp> lockApps;

    private long sum = 0;

    private View nativeView;


    protected AppManagerAdapter(Context context, ArrayList<LockApp> lockApps) {
        this.context = context;
        this.lockApps = lockApps;

        nativeView = LayoutInflater.from(context).inflate(R.layout.native_ads_list,null);
        MyAds.initNativeList(nativeView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==VIEW_TYPE_ITEM){
            view = LayoutInflater.from(context).inflate(R.layout.item_home_lock,parent,false);
            return new ItemHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_native_ads, parent, false);
            return new NativeAdHolder(view);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            if(holder instanceof ItemHolder){
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.tvAppName.setText(lockApps.get(position).getName());
                String time = lockApps.get(position).getTimeOpen()+" " +
                        (lockApps.get(position).getTimeOpen()>1?"times":"time");
                Double percent = ((100/(double)sum)*lockApps.get(position).getTimeOpen())+0.5;
                itemHolder.tvOpen.setText(time+" ( "+ percent.intValue() +"% ) ");
                if(lockApps.get(position).isLock()){
                    itemHolder.imgLock.setImageResource(R.drawable.ic_lock_app_lock);
                }else itemHolder.imgLock.setImageResource(R.drawable.ic_lock_app_unlock);
                Glide.with(context)
                        .load(lockApps.get(position).getIcon())
                        .into(itemHolder.imgIcon);
            }else {
                NativeAdHolder adHolder = (NativeAdHolder) holder;
                adHolder.nativeAD.removeAllViews();
                try {
                    adHolder.nativeAD.addView(nativeView);
                }catch (Exception ignored){}
            }


    }

    @Override
    public int getItemCount() {
        return lockApps.size();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if(lockApps.get(position).getAppId()!=null){
                return VIEW_TYPE_ITEM;
            }else return VIEW_TYPE_NATIVE_AD;
        } catch (Exception e) {
            return VIEW_TYPE_NATIVE_AD;
        }
    }

    public void setData(ArrayList<LockApp> messApps) {
        this.lockApps = messApps;
        sortList();
    }

    public void sortList() {
        try {
            sum = 0;
            for (LockApp app:lockApps){
                sum = sum+app.getTimeOpen();
            }
            Collections.sort(lockApps, (t1, t2) -> Long.compare(t2.getTimeOpen(),t1.getTimeOpen()));

            //add ads
            lockApps.add(0,new LockApp());

            notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private TextView tvAppName,tvOpen;
        private ImageView imgIcon,imgLock;


        ItemHolder(@NonNull final View itemView) {
            super(itemView);

            tvAppName = itemView.findViewById(R.id.tv_app_name);
            imgIcon = itemView.findViewById(R.id.img_app);
            tvOpen = itemView.findViewById(R.id.tv_time_open);
            imgLock = itemView.findViewById(R.id.img_lock);

            itemView.setOnClickListener(v ->
                    OnItemClick(lockApps.get(getAdapterPosition()),getAdapterPosition()));
            itemView.setOnTouchListener(new OnTouch());
            imgLock.setOnClickListener(v ->
                    OnLockIconClick(lockApps.get(getAdapterPosition()),getAdapterPosition()));

        }
    }

    class NativeAdHolder extends RecyclerView.ViewHolder{
        private FrameLayout nativeAD;
        NativeAdHolder(final View itemView) {
            super(itemView);
            nativeAD = itemView.findViewById(R.id.native_item);
        }
    }


    public abstract void OnItemClick(LockApp lockApp,int position);
    public abstract void OnLockIconClick(LockApp lockApp,int position);
}
