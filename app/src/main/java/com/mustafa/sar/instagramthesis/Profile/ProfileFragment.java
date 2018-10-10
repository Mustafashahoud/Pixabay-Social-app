package com.mustafa.sar.instagramthesis.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mustafa.sar.instagramthesis.Login.LoginActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.BottomNavigationViewHelper;
import com.mustafa.sar.instagramthesis.utilities.FirebaseUtilities;
import com.mustafa.sar.instagramthesis.utilities.UniversalImageLoader;
import com.mustafa.sar.instagramthesis.utilities.models.GeneralInfoUserModel;
import com.mustafa.sar.instagramthesis.utilities.models.User;
import com.mustafa.sar.instagramthesis.utilities.models.UserProfileAccountSetting;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener myAuthenListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUtilities firebaseUtilities;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile , container , false);

        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.profileName);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();
        firebaseUtilities = new FirebaseUtilities(getActivity());
        Log.d(TAG, "onCreateView: stared.");

        setupBottomNavigationView();
        setupToolbar();

        setupFirebaseAuth();





        return view;
    }

    /**
     * Sitting up the BottomNavigationView and disabling the animation and shifting
     */
    private void setupBottomNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
        // menuItem.getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);


    }


    /**
     * sets the toolbar
     */
    private void setupToolbar() {
        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountSettingActivity.class);
                startActivity(intent);

            }
        });

    }

    ///////////////////////////*******************Firebase********************///////////////////////

    public void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        myAuthenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser =  firebaseAuth.getCurrentUser();

                if (currentUser != null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed in" + currentUser.getUid());

                }
                else if (currentUser == null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed out" );
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Retrieving the user info from database
                // retrieveAccountUserInfo returns  the retrieved General obj
                setProfileWidget( firebaseUtilities.retrieveAccountUserInfo(dataSnapshot));

                // Retrieving the images

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidget(GeneralInfoUserModel generalInfoUserModel){

        /*
            1- We have database two tables each one is represented by a class
            2- When we register  a new user their data will be stored in the database
            3- By DataSnapshot we get the stored info and put them in a class "GeneralInfoUserModel" that contains two classes
        *   4- So the object of the "GeneralInfoUserModel"  has the data
        *
        * */
        User user = new User();
        UserProfileAccountSetting setting = generalInfoUserModel.getUserProfileAccountSetting();

        // Set the profile photo.
        UniversalImageLoader.setImage(setting.getProfile_photo() , mProfilePhoto , null , "");

        //set the widgets Followers and Following and Posts etc ..

        mDisplayName.setText(setting.getDisplay_name());
        mDescription.setText(setting.getDescription());
        mFollowing.setText(String.valueOf (setting.getFollowings()));
        mFollowers.setText(String.valueOf(setting.getFollowers()));
        mPosts.setText(String.valueOf(setting.getPosts()));
        mWebsite.setText(setting.getWebsite());
        mUsername.setText(setting.getUser_name());

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: checking if the user is logged in or out");
        mAuth.addAuthStateListener(myAuthenListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (myAuthenListener != null) {
            mAuth.removeAuthStateListener(myAuthenListener);
        }
    }


}
