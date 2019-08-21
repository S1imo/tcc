package com.bento.a.storage;

public class Upload {

    private String mName;
    private String mImageUrl;

    public Upload(){}

    public Upload(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
    public String getmImageUrl() {
        return mImageUrl;
    }
    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
