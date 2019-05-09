package com.mustafa.sar.pixabayscoialapp.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.models.User;
import com.mustafa.sar.pixabayscoialapp.post.ViewCommentFragment;
import com.mustafa.sar.pixabayscoialapp.post.ViewPostFragment;
import com.mustafa.sar.pixabayscoialapp.models.Photo;

public class ProfileActivity extends AppCompatActivity  implements
        OnGridImageSelectedListener, OnCommentSelectedListener {

    private static final String TAG = "ProfileActivity";

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
        if (intent.hasExtra("calling activity")) {
            User user = intent.getParcelableExtra("user");
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (!user.getUser_id().equals(userID)){
                Log.d(TAG, "profileContainerMethod: We are coming from Search Activity");
                ViewProfileFragment viewProfileFragment = new ViewProfileFragment();
                viewProfileFragment.setOnGridImageSelectedListener(this);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", intent.getParcelableExtra("user"));
                viewProfileFragment.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, viewProfileFragment);
                transaction.addToBackStack("View Profile Fragment");
                transaction.commit();

            }else {
                ProfileFragment profileFragment = new ProfileFragment();
                //set the OnGridImageSelectedListener that is used for passing the photo and the number
                profileFragment.setOnGridImageSelectedListener(this);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer , profileFragment);
                fragmentTransaction.addToBackStack("Profile");
                fragmentTransaction.commit();

            }
        }else {
            ProfileFragment profileFragment = new ProfileFragment();
            //set the OnGridImageSelectedListener that is used for passing the photo and the number
            profileFragment.setOnGridImageSelectedListener(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer , profileFragment);
            fragmentTransaction.addToBackStack("Profile");
            fragmentTransaction.commit();

        }

    }

    @Override
    public void onBackPressed() {
        //Fixed Bugs white screen appears whe navigating back to sreach activity
        finish();
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
