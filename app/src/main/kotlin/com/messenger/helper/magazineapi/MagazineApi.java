package com.messenger.helper.magazineapi;

import com.messenger.models.magazine.MagazineNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MagazineApi {

    @GET("content?key=uUK7301SYkBEdD7tEXAaV4BipZp0eu0N&publisherId=doduong126-Mess4")
    Call<MagazineNews> getNews(@Query("userId") String userId, @Query("originCountryCode") String country,
                               @Query("limit") int limit, @Query("offset") int offset);

}
