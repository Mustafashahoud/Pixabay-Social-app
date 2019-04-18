package com.mustafa.sar.instagramthesis.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * this calss to fill out and interact with database firebase it represents a table or a node
 * and for this example it refers to user_profile_account
 */

public class UserProfileAccountSetting implements Parcelable {
    private String description;
    private String display_name;
    private long followers;
    private long followings ;
    private long posts ;
    private String profile_photo;
    private String username;
    private String website;
    private String user_id;

    public UserProfileAccountSetting(String description, String display_name, long followers,
                                     long followings, long posts, String profile_photo,
                                     String username, String website, String user_id) {
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.username = username;
        this.website = website;
        this.user_id = user_id;
    }

    protected UserProfileAccountSetting(Parcel in) {
        description = in.readString();
        display_name = in.readString();
        followers = in.readLong();
        followings = in.readLong();
        posts = in.readLong();
        profile_photo = in.readString();
        username = in.readString();
        website = in.readString();
        user_id = in.readString();
    }

    public static final Creator<UserProfileAccountSetting> CREATOR = new Creator<UserProfileAccountSetting>() {
        @Override
        public UserProfileAccountSetting createFromParcel(Parcel in) {
            return new UserProfileAccountSetting(in);
        }

        @Override
        public UserProfileAccountSetting[] newArray(int size) {
            return new UserProfileAccountSetting[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserProfileAccountSetting() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowings() {
        return followings;
    }

    public void setFollowings(long followings) {
        this.followings = followings;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "UserProfileAccountSetting{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", followers=" + followers +
                ", followings=" + followings +
                ", posts=" + posts +
                ", profile_photo='" + profile_photo + '\'' +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(display_name);
        dest.writeLong(followers);
        dest.writeLong(followings);
        dest.writeLong(posts);
        dest.writeString(profile_photo);
        dest.writeString(username);
        dest.writeString(website);
        dest.writeString(user_id);
    }
}
