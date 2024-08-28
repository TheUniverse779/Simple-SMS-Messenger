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
import com.messenger.models.magazine.MagazineContentNews;

import java.util.List;

public abstract class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_NATIVE_AD = 0;

    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;

    private List<MagazineContentNews> news;

    private View nativeView;

    protected NewsAdapter(Context context, List<MagazineContentNews> news) {
        this.context = context;
        this.news = news;
        nativeView = LayoutInflater.from(context).inflate(R.layout.native_ads_list,null);
        MyAds.initNativeList(nativeView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==VIEW_TYPE_ITEM){
            view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
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
            Glide.with(context)
                    .load(news.get(position).getImages().getMagazineMainImage().getUrl())
                    .into(itemHolder.imgAvatar);
            itemHolder.tvNew.setText("      "+news.get(position).getTitle());
        }else {
            NativeAdHolder adHolder = (NativeAdHolder) holder;
            adHolder.nativeAD.removeAllViews();
            try {
                adHolder.nativeAD.addView(nativeView);
            }catch (Exception ignored){}
        }



    }

    @Override
    public int getItemViewType(int position) {
        if (news.get(position).getContentURL()!=null){
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_NATIVE_AD;
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setNews(List<MagazineContentNews> news) {
        this.news.clear();
        this.news = news;
        notifyDataSetChanged();
    }

    public void addNews(List<MagazineContentNews> news) {
        addNative(news);
        int i = this.news.size();
        this.news.addAll(news);
        notifyItemRangeInserted(i,news.size());
    }

    private void addNative(List<MagazineContentNews> news) {

        if(news.size()>6){
            news.add(6,new MagazineContentNews());
        }
        if(news.size()>13){
            news.add(13,new MagazineContentNews());
        }
        if(news.size()>20){
            news.add(20,new MagazineContentNews());
        }
        if(news.size()>=28){
            news.add(28,new MagazineContentNews());
        }
    }

    class NativeAdHolder extends RecyclerView.ViewHolder{
        private FrameLayout nativeAD;
        NativeAdHolder(final View itemView) {
            super(itemView);
            nativeAD = itemView.findViewById(R.id.native_item);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private View viewAvatar,viewNew;
        private TextView tvNew;
        private ImageView imgAvatar;

        ItemHolder(@NonNull final View itemView) {
            super(itemView);
            viewAvatar = itemView.findViewById(R.id.view_avatar);
            viewNew = itemView.findViewById(R.id.view_text);
            tvNew = itemView.findViewById(R.id.tv_value);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            itemView.setOnTouchListener(new OnTouch());
            itemView.setOnClickListener(v -> onItemClick(news.get(getAdapterPosition())));
            viewAvatar.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels/2.1);
            viewAvatar.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels/2.4);
            viewNew.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels/2.1);

        }
    }

    public abstract void onItemClick(MagazineContentNews news);

}
