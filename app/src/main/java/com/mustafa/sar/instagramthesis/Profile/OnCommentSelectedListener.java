package com.mustafa.sar.instagramthesis.Profile;

import android.os.Bundle;


public interface OnCommentSelectedListener{
    String keyForActionValue = "first key";
    int ACTION_KEY = 1;

    String getPhoto = "getPhoto";

    void onCommentSelectedListener(Bundle photo);
}