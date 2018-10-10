package com.mustafa.sar.instagramthesis.utilities.models;

/**
 * this calss to fill out and interact with database firebase it represents a table or a node
 * and for this example it refers to user_profile_account
 */

public class UserProfileAccountSetting {
    private String description;
    private String display_name;
    private long followers;
    private long followings ;
    private long posts ;
    private String profile_photo;
    private String user_name;
    private String website;

    public UserProfileAccountSetting(String description, String display_name, long followers, long followings,
                                     long posts, String profile_photo, String user_name, String website) {
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.user_name = user_name;
        this.website = website;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
                ", user_name='" + user_name + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
