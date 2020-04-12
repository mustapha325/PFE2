package com.example.skymail.Data;

import android.net.Uri;

public class UploadImages {
    private String ImageID;
    private String userEmail;
    private String mImageUrl;



    public UploadImages() {
    }

    public UploadImages(String imageID,String userEmail, String mImageUrl) {
        ImageID = imageID;
        this.userEmail = userEmail;
        this.mImageUrl = mImageUrl;
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}


