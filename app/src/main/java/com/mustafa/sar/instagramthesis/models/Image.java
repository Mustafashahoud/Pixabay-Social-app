package com.mustafa.sar.instagramthesis.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Image {
    private int previewHeight;
    private int likes;
    private int favorites;
    private String tags;
    @SerializedName("webformatHeight")
    private int webFormatHeight;
    private long views;
    @SerializedName("webformatWidth")
    private int webFormatWidth;
    private int previewWidth;
    private int comments;
    private long downloads;
    private String pageURL;
    @SerializedName("previewURL")
    private String previewUrl;
    @SerializedName("webformatURL")
    private String webFormatUrl;
    private int imageWidth;
    @SerializedName("user_id")
    private long userId;
    private String user;
    private long id;
    @SerializedName("userImageURL")
    private String userImageUrl;
    private int imageHeight;

    public Image(long id,
                 String pageUrl,
                 String tags,
                 String previewUrl,
                 int previewWidth,
                 int previewHeight,
                 int likes,
                 int favorites,
                 int webFormatHeight,
                 String webFormatUrl,
                 long views,
                 int webFormatWidth,
                 int comments,
                 long downloads,
                 int imageWidth,
                 long userId,
                 String user,
                 String userImageUrl,
                 int imageHeight) {

        this.previewHeight = previewHeight;
        this.likes = likes;
        this.favorites = favorites;
        this.tags = tags;
        this.webFormatHeight = webFormatHeight;
        this.views = views;
        this.webFormatWidth = webFormatWidth;
        this.previewWidth = previewWidth;
        this.comments = comments;
        this.downloads = downloads;
        this.pageURL = pageUrl;
        this.previewUrl = previewUrl;
        this.webFormatUrl = webFormatUrl;
        this.imageWidth = imageWidth;
        this.userId = userId;
        this.user = user;
        this.id = id;
        this.userImageUrl = userImageUrl;
        this.imageHeight = imageHeight;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public String getLikes() {
        return String.valueOf(likes);
    }

    public String getFavorites() {
        return String.valueOf(favorites);
    }

    public String getTags() {
        if (tags == null) return "";
        if (tags.contains(", ")) {
            String[] splitTags = tags.toUpperCase().split(", ");
            return TextUtils.join(" - ", splitTags);
        } else return tags;
    }

    public int getWebFormatHeight() {
        return webFormatHeight;
    }

    public long getViews() {
        return views;
    }

    public int getWebFormatWidth() {
        return webFormatWidth;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public String getComments() {
        return String.valueOf(comments);
    }

    public long getDownloads() {
        return downloads;
    }

    public String getPageURL() {
        return pageURL;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getWebFormatUrl() {
        return webFormatUrl;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public long getUserId() {
        return userId;
    }

    public String getUser() {
        return "By: " + user;
    }

    public long getId() {
        return id;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

}