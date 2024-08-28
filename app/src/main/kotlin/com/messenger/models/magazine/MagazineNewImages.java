package com.messenger.models.magazine;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MagazineNewImages {
    @SerializedName("mainImage")
    @Expose
    private MagazineMainImage magazineMainImage;
    @SerializedName("mainImageThumbnail")
    @Expose
    private MagazineMainImageThumbnail magazineMainImageThumbnail;
    @SerializedName("additionalImages")
    @Expose
    private List<MagazineAdditionalImage> magazineAdditionalImages = null;

    public MagazineMainImage getMagazineMainImage() {
        return magazineMainImage;
    }

    public void setMagazineMainImage(MagazineMainImage magazineMainImage) {
        this.magazineMainImage = magazineMainImage;
    }

    public MagazineMainImageThumbnail getMagazineMainImageThumbnail() {
        return magazineMainImageThumbnail;
    }

    public void setMagazineMainImageThumbnail(MagazineMainImageThumbnail magazineMainImageThumbnail) {
        this.magazineMainImageThumbnail = magazineMainImageThumbnail;
    }

    public List<MagazineAdditionalImage> getMagazineAdditionalImages() {
        return magazineAdditionalImages;
    }

    public void setMagazineAdditionalImages(List<MagazineAdditionalImage> magazineAdditionalImages) {
        this.magazineAdditionalImages = magazineAdditionalImages;
    }

}
