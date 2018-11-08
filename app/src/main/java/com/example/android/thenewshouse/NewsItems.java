package com.example.android.thenewshouse;

import android.graphics.Bitmap;

/**
 * Created by user on 11-07-2017.
 */

public class NewsItems {
    private Bitmap mImageBitmap;
    private String mNewsHead;
    private String mDateofPublication;
    private String mSection;
    private String mWebUrl;

    NewsItems(Bitmap imagebitmap,String newshead,String dateofpublication,String section,String weburl){
        mImageBitmap=imagebitmap;
        mNewsHead=newshead;
        mDateofPublication=dateofpublication;
        mSection=section;
        mWebUrl=weburl;
    }

    public Bitmap getmImageBitmap() {
        return mImageBitmap;
    }
    public String getmNewsHead() {
        return mNewsHead;
    }
    public String getmDateofPublication() {
        return mDateofPublication;
    }
    public String getmSection() {
        return mSection;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }
}
