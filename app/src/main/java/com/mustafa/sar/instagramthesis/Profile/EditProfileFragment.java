package com.mustafa.sar.instagramthesis.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenResult;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.Share.ShareActivity;
import com.mustafa.sar.instagramthesis.utilities.FirebaseUtilities;
import com.mustafa.sar.instagramthesis.utilities.UniversalImageLoader;
import com.mustafa.sar.instagramthesis.utilities.models.GeneralInfoUserModel;
import com.mustafa.sar.instagramthesis.utilities.models.User;
import com.mustafa.sar.instagramthesis.utilities.models.UserProfileAccountSetting;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";
    ImageView mProfilePhoto;
    ImageView backArrow;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener myAuthenListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUtilities firebaseUtilities;
    private String userID;



    //EditProfile Fragment widgets
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;

    GeneralInfoUserModel mUserSetting;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        //////////widgets////
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mDisplayName = (EditText) view.findViewById(R.id.displayName);
        mUsername = (EditText) view.findViewById(R.id.userName);
        mWebsite = (EditText) view.findViewById(R.id.website);
        mDescription = (EditText) view.findViewById(R.id.description);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPhoneNumber = (EditText) view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        firebaseUtilities = new FirebaseUtilities(getActivity());
        ////////

        //to retrieve imageView profile photo
        //mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);

        backArrow = (ImageView) view.findViewById(R.id.backArrow);

        backArrowListener();

        setupFirebaseAuth();

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here we will be navigated to the Share Activity but we need a way to differentiate
                // weather we are coming by pressing Change Profile photo text view or not
                // Cuz we wanna know if it is a profile photo or normal photo
                // we will use a flag to do so.
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); // this flag is a number but NOT zero you can use any flag
                 /*it will be caught by  galleryFragment cuz it is the first one in the share activity*/
                getActivity().startActivity(intent);
                getActivity().finish();



            }
        });

        ImageView checkMark = (ImageView) view.findViewById(R.id.checkMark);
        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
            }
        });

        return view;
    }

    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before doing so it checks to make sure the username and the email chosen is unique
     */
    private void saveProfileSettings(){
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


 /*  User user = new User();
                for(DataSnapshot ds:  dataSnapshot.child("").getChildren()){
                    if(ds.getKey().equals(userID)){
                        user.setUsername(ds.getValue(User.class).getUsername());
                    }
                }
                Log.d(TAG, "onDataChange: CURRENT USERNAME: " + user.getUsername());*/
              // What we wanna do is to use the Query cuz it is super slow using the children and firebase nodes
                //So we get the user and compare it to what was loaded originally

                //case1: the user did not change their username

                String newUserName = username;
                String oldUserName = mUserSetting.getUser().getUsername() ;

                if(!oldUserName.equals(newUserName))
                {
                    //1- the new username is "username"
                    //2- the old username is "mUserSetting.getUser().getUsername()" cux it is taken from the table
                    checkIfUsernameExists(username);
                }

                //case2: the user changed their username therefore we need to check for uniqueness
                /*else{

                }*/
                String newDisplayName = displayName;
                String oldDispalyName = mUserSetting.getUserProfileAccountSetting().getDisplay_name() ;
                if (!oldDispalyName.equals(newDisplayName)){
                    //Update the display name
                    firebaseUtilities.updateUserAccountSettings(displayName, null, null, 0);
                }

                String newWebsite = website;
                String oldWebsite = mUserSetting.getUserProfileAccountSetting().getWebsite() ;
                if (!oldWebsite.equals(newWebsite)){
                    //Update the website
                    firebaseUtilities.updateUserAccountSettings(null, website, null, 0);
                }

                String newDescription = description;
                String oldDescription = mUserSetting.getUserProfileAccountSetting().getDescription() ;
                if (!oldDescription.equals(newDescription)){
                    //Update the description
                    firebaseUtilities.updateUserAccountSettings(null, null, description, 0);

                }

                long newPhoneNum = phoneNumber;
                long oldPhoneNum = mUserSetting.getUser().getPhone_number() ;
                if (newPhoneNum != oldPhoneNum ){
                    //Update the description
                    firebaseUtilities.updateUserAccountSettings(null, null, null, phoneNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Check is @param username already exists in teh database
     * @param username the userName
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.db_user))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //It is gonna return a dataSnapshot if there is a match
                if(!dataSnapshot.exists()){
                    //add the username
                    firebaseUtilities.updateUsername(username);

                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();

                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setProfileWidgets(GeneralInfoUserModel userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database:name " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: mail " + userSettings.getUser().getEmail());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: phone " + userSettings.getUser().getPhone_number());

        User user = userSettings.getUser();
        UserProfileAccountSetting settings = userSettings.getUserProfileAccountSetting();
        mUserSetting = userSettings;
        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");
        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(user.getEmail());
        mPhoneNumber.setText(String.valueOf(user.getPhone_number()));

    }

    private void backArrowListener() {

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    public void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getUid();

        myAuthenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser =  firebaseAuth.getCurrentUser();

                if (currentUser != null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed in  " + currentUser.getUid());

                }
                else if (currentUser == null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed out" );
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(firebaseUtilities.retrieveAccountUserInfo(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

