package com.mustafa.sar.pixabayscoialapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mustafa.sar.pixabayscoialapp.Home.HomeActivity;
import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.models.Comment;
import com.mustafa.sar.pixabayscoialapp.models.Like;
import com.mustafa.sar.pixabayscoialapp.utilities.BottomNavigationViewHelper;
import com.mustafa.sar.pixabayscoialapp.utilities.FirebaseUtilities;
import com.mustafa.sar.pixabayscoialapp.utilities.UniversalImageLoader;
import com.mustafa.sar.pixabayscoialapp.utilities.gallery.GridImageAdapter;
import com.mustafa.sar.pixabayscoialapp.models.GeneralInfoUserModel;
import com.mustafa.sar.pixabayscoialapp.models.Photo;
import com.mustafa.sar.pixabayscoialapp.models.User;
import com.mustafa.sar.pixabayscoialapp.models.UserProfileAccountSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    OnGridImageSelectedListener mOnGridImageSelectedListener;

    private static final int ACTIVITY_NUM = 4;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription , tvEditProfile;
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

    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;

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
        tvEditProfile = (TextView) view.findViewById(R.id.tvEditProfile);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();
        firebaseUtilities = new FirebaseUtilities(getActivity());
        Log.d(TAG, "onCreateView: stared.");

        setupBottomNavigationView();
        setupToolbar();

        setupFirebaseAuth();

        setupTvEditProfile();
       mProgressBar.setVisibility(View.GONE);

        populateGridView();

        getFollowingCount();
        getFollowersCount();
        getPostsCount();


        return view;
    }

    public void setOnGridImageSelectedListener(OnGridImageSelectedListener mOnGridImageSelectedListener){
        this.mOnGridImageSelectedListener = mOnGridImageSelectedListener;
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
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    Intent intent = new Intent(getActivity() , HomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * sets the toolbar
     */
    private void setupToolbar() {
        ((ProfileActivity) getActivity()).setSupportActionBar(toolbar);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountSettingActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

    }

    private void setupTvEditProfile(){
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity() , AccountSettingActivity.class);
                intent.putExtra("calling_activity" , "profile_activity");
                startActivity(intent);
            }
        });
    }

    private void populateGridView(){
        /*We will need an ArrayList to save the photos inside it*/
        final ArrayList<Photo> photos = new ArrayList<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = myRef.child(getString(R.string.db_user_photos)).child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    /*We get the photos objects for all photos of a certain user */
                    //photos.add(photo.getValue(Photo.class));
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                    try{
                        photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                        photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                        photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                        photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());

                        ArrayList<Comment> comments = new ArrayList<>();
                        for (DataSnapshot dSnapshot : singleSnapshot
                                .child(getString(R.string.field_comments)).getChildren()){
                            Comment comment = new Comment();
                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                            comments.add(comment);
                        }

                        photo.setComments(comments);

                        List<Like> likesList = new ArrayList<Like>();
                        for (DataSnapshot dSnapshot : singleSnapshot
                                .child(getString(R.string.field_likes)).getChildren()){
                            Like like = new Like();
                            like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
                            likesList.add(like);
                        }
                        photo.setLikes(likesList);
                        photos.add(photo);

                    }catch (NullPointerException e){
                        Log.d(TAG, "onDataChange: NullPointerException" + e.getMessage());
                    }

                }
                ArrayList<String> imgUrls = new ArrayList();
                for (int i = 0; i < photos.size(); i++) {
                    /*We get the url of each photo objects in the database*/
                    imgUrls.add(photos.get(i).getImage_path());
                }
                setColumnGridViewWidth();
                GridImageAdapter gridImageAdapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, "",imgUrls);
                gridView.setAdapter(gridImageAdapter);

                //We will add a listener to the items of GridView  and then we can use our interface to navigate to postViewFragment
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowersCount(){
        mFollowersCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.db_followers))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                mFollowers.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFollowingCount(){
        mFollowingCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.db_following))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getValue());
                    mFollowingCount++;
                }
                mFollowing.setText(String.valueOf(mFollowingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPostsCount(){
        mPostsCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.db_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found post:" + singleSnapshot.getValue());
                    mPostsCount++;
                }
                mPosts.setText(String.valueOf(mPostsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setColumnGridViewWidth(){
        int widthGridView = getActivity().getResources().getDisplayMetrics().widthPixels;//get the widthPixels od the GridView
        int imgWidth = widthGridView/3;
        gridView.setColumnWidth(imgWidth);
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");

                //retrieve user information from the database
                setProfileWidget(firebaseUtilities.retrieveAccountUserInfo(dataSnapshot));

                //retrieve images for the user in question

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");

            }
        });

    }

    // Retrieving the user info from database
    // retrieveAccountUserInfo returns  the retrieved General obj


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
        mWebsite.setText(setting.getWebsite());
        mUsername.setText(setting.getUsername());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: checking if the user is logged in or out");
        mAuth.addAuthStateListener(myAuthenListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (myAuthenListener != null) {
            mAuth.removeAuthStateListener(myAuthenListener);
        }
    }
}
