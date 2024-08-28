package com.messenger.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.adapters.NewsAdapter;
import com.messenger.ads.MyAds;
import com.messenger.helper.magazineapi.LoadNews;
import com.messenger.models.magazine.MagazineContentNews;
import com.messenger.models.magazine.MagazineNews;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeNewsFragment extends BaseFragment{

    private String device_id;

    private String country  = "EN";

    private GridLayoutManager mLayout;

    private NewsAdapter newsAdapter;

    private Boolean isLoading = false;

    private RecyclerView rcvNews;

    private List<MagazineContentNews> news;

    private SwipeRefreshLayout refreshLayout;


    public static HomeNewsFragment newInstance() {
        Bundle args = new Bundle();
        HomeNewsFragment fragment = new HomeNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        country = Locale.getDefault().getCountry().toUpperCase();

        device_id= Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        setupList();

        initRefresh();
    }

    private void initRefresh() {
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(() -> {
            news.clear();
            newsAdapter.notifyDataSetChanged();
            getNews();
        });
    }

    private void setupList() {

        news = new ArrayList<>();
        news.add(new MagazineContentNews());

        newsAdapter = new NewsAdapter(activity,news) {
            @Override
            public void onItemClick(MagazineContentNews news) {
                MyAds.showInterFull(activity, (value, where)
                        -> activity.mWebView.start(news));

            }
        };


        mLayout = new GridLayoutManager(activity,2);

        mLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(newsAdapter.getItemViewType(position)==NewsAdapter.VIEW_TYPE_NATIVE_AD){
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        rcvNews.setLayoutManager(mLayout);


        rcvNews.setAdapter(newsAdapter);

        rcvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                refreshLayout.setEnabled(mLayout.findFirstCompletelyVisibleItemPosition() == 0);
                if (isLoading)
                    return;
                int visibleItemCount = mLayout.getChildCount();
                int totalItemCount = mLayout.getItemCount();
                int pastVisibleItems = mLayout.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount-10) {
                    getNews();
                }
            }
        });

        getNews();

    }

    private void hideRefresh(){
        new Handler().postDelayed(() -> refreshLayout.setRefreshing(false),1000);
    }

    private void getNews(){
        isLoading = true;
        LoadNews.getMagazineApi().getNews(device_id,country,24,newsAdapter.getItemCount()).enqueue(new Callback<MagazineNews>() {
            @Override
            public void onResponse(@NonNull Call<MagazineNews> call, @NonNull Response<MagazineNews> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    isLoading = false;
                    newsAdapter.addNews(response.body().getContent());
                }

                hideRefresh();
            }

            @Override
            public void onFailure(@NonNull Call<MagazineNews> call, @NonNull Throwable t) {
                Toast.makeText(activity, "Load news fail!", Toast.LENGTH_SHORT).show();
                hideRefresh();
            }
        });
    }

    private void initView() {
        rcvNews = view.findViewById(R.id.rcv_news);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home_news;
    }
}
