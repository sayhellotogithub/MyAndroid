package com.iblogstreet.basecontructor.domain;

/**
 * Created by Administrator on 2016/3/16.
 */
public class BaiduNews {
    // title url  abstract image_url
    private String title;
    private String url;
    private String brief;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BaiduNews(String title, String url, String brief, String imageUrl) {
        this.title = title;
        this.url = url;
        this.brief = brief;
        this.imageUrl = imageUrl;
    }
}
