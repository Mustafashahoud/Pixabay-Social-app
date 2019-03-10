package com.mustafa.sar.instagramthesis.Profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.post.ViewPostFragment;
import com.mustafa.sar.instagramthesis.utilities.models.Photo;

public class ProfileActivity extends AppCompatActivity  implements ProfileFragment.OnGridImageSelectedListener {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context context = ProfileActivity.this;
    private ImageView profilePhotoImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ProfileActivity");
        setContentView(R.layout.activity_profile);
        profileContainerMethod();
    }

    private void profileContainerMethod(){

        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer , profileFragment);
        fragmentTransaction.addToBackStack("Profile");
        fragmentTransaction.commit();

    }
    // Imp of onGridImageSelected to navigate to ViewPostFragment
    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        ViewPostFragment viewPostFragment = new ViewPostFragment();
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
