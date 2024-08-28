package com.messenger.helper.magazineapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadNews {

    private static Retrofit retrofit = null;

    private static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static MagazineApi getMagazineApi() {
        return LoadNews.getClient("https://contentapi.celltick.com/mediaApi/v1.0/").create(MagazineApi.class);
    }
}
