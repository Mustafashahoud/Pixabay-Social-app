package com.mustafa.sar.instagramthesis.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.post.ViewCommentFragment;
import com.mustafa.sar.instagramthesis.post.ViewPostFragment;
import com.mustafa.sar.instagramthesis.models.Photo;

public class ProfileActivity extends AppCompatActivity  implements
        ProfileFragment.OnGridImageSelectedListener, OnCommentSelectedListener {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context context = ProfileActivity.this;
    private ImageView profilePhotoImg;


    @Override
    public void onCommentSelectedListener(Bundle bundle) {

        Log.d(TAG, "onCommentSelectedListener:  selected a comment");

        int actionInt = bundle.getInt(OnCommentSelectedListener.keyForActionValue);
        //String valueReceived = bundle.getString(ActionListenerInterface.getKeyForStringValue, "This is Default");

        if (actionInt == OnCommentSelectedListener.ACTION_KEY){

            ViewCommentFragment fragment = new ViewCommentFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.addToBackStack(getString(R.string.view_comments_fragment));
            transaction.commit();
        }

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ProfileActivity");
        setContentView(R.layout.activity_profile);
        profileContainerMethod();
    }

    private void profileContainerMethod(){

        Intent intent = getIntent();
        
        if (intent.hasExtra("calling activity")){

            Log.d(TAG, "profileContainerMethod: We are coming from Search Activity");
            ViewProfileFragment fragment = new ViewProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", intent.getParcelableExtra("user"));
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.addToBackStack("View Profile Fragment");
            transaction.commit();

        }else {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer , profileFragment);
            fragmentTransaction.addToBackStack("Profile");
            fragmentTransaction.commit();

        }

    }
    // Imp of onGridImageSelected to navigate to ViewPostFragment
    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        ViewPostFragment viewPostFragment = new ViewPostFragment();
        viewPostFragment.setOnCommentSelectedListener(this);
        Bundle args = new Bundle();
        args.putParcelable("Photo", photo);
        args.putInt("Activity Number", activityNumber);
        viewPostFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, viewPostFragment);
        fragmentTransaction.addToBackStack("View Post Fragment");
        fragmentTransaction.commit();
    }


}
