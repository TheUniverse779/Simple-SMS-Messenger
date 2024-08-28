package com.messenger.models.magazine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MagazineContentNews {
    @SerializedName("contentId")
    @Expose
    private String contentId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("contentSource")
    @Expose
    private String contentSource;
    @SerializedName("contentSourceDisplay")
    @Expose
    private String contentSourceDisplay;
    @SerializedName("contentSourceLogo")
    @Expose
    private String contentSourceLogo;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("categoriesEnglish")
    @Expose
    private List<String> categoriesEnglish = null;
    @SerializedName("images")
    @Expose
    private MagazineNewImages images;
    @SerializedName("countries")
    @Expose
    private List<String> countries = null;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("publishedAt")
    @Expose
    private Long publishedAt;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("isPreview")
    @Expose
    private Boolean isPreview;
    @SerializedName("gaId")
    @Expose
    private String gaId;
    @SerializedName("contentURL")
    @Expose
    private String contentURL;
    @SerializedName("recommendationId")
    @Expose


    private String recommendationId;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public String getContentSourceDisplay() {
        return contentSourceDisplay;
    }

    public void setContentSourceDisplay(String contentSourceDisplay) {
        this.contentSourceDisplay = contentSourceDisplay;
    }

    public String getContentSourceLogo() {
        return contentSourceLogo;
    }

    public void setContentSourceLogo(String contentSourceLogo) {
        this.contentSourceLogo = contentSourceLogo;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategoriesEnglish() {
        return categoriesEnglish;
    }

    public void setCategoriesEnglish(List<String> categoriesEnglish) {
        this.categoriesEnglish = categoriesEnglish;
    }

    public MagazineNewImages getImages() {
        return images;
    }

    public void setImages(MagazineNewImages images) {
        this.images = images;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Boolean getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Boolean isPreview) {
        this.isPreview = isPreview;
    }

    public String getGaId() {
        return gaId;
    }

    public void setGaId(String gaId) {
        this.gaId = gaId;
    }

    public String getContentURL() {
        return contentURL;
    }

    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }

    public String getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(String recommendationId) {
        this.recommendationId = recommendationId;
    }



}