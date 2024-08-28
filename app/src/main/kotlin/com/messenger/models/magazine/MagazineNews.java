package com.messenger.models.magazine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MagazineNews {
    @SerializedName("totalItems")
    @Expose
    private int totalItems;
    @SerializedName("content")
    @Expose
    private List<MagazineContentNews> content = null;


    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<MagazineContentNews> getContent() {
        return content;
    }

    public void setContent(List<MagazineContentNews> content) {
        this.content = content;
    }


}
