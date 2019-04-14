package com.mustafa.sar.instagramthesis.models;

public class GeneralInfoUserModel {
    private User user ;
    private UserProfileAccountSetting userProfileAccountSetting;

    public GeneralInfoUserModel(User user, UserProfileAccountSetting userProfileAccountSetting) {
        this.user = user;
        this.userProfileAccountSetting = userProfileAccountSetting;
    }
    public GeneralInfoUserModel() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfileAccountSetting getUserProfileAccountSetting() {
        return userProfileAccountSetting;
    }

    public void setUserProfileAccountSetting(UserProfileAccountSetting userProfileAccountSetting) {
        this.userProfileAccountSetting = userProfileAccountSetting;
    }
}
