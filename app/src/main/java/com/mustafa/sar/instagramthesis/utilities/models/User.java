package com.mustafa.sar.instagramthesis.utilities.models;

public class User {
    private String user_name;
    private String email;
    private String user_id;
    private long phone;

    public User() {
    }

    public User(String username, String email, String user_id, long phone) {
        this.user_name = username;
        this.email = email;
        this.user_id = user_id;
        this.phone = phone;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", phone=" + phone +
                '}';
    }
}
