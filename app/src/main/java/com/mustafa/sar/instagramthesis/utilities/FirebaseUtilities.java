package com.mustafa.sar.instagramthesis.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.models.GeneralInfoUserModel;
import com.mustafa.sar.instagramthesis.utilities.models.User;
import com.mustafa.sar.instagramthesis.utilities.models.UserProfileAccountSetting;

import static android.content.ContentValues.TAG;

public class FirebaseUtilities {

    private FirebaseAuth mAuth;
    private Context mContext;
    private String userID ;

    //Database
    private FirebaseDatabase firebaseDatabase ;
    private  DatabaseReference  myRef;



public FirebaseUtilities (Context context){
    mContext = context;
    mAuth = FirebaseAuth.getInstance();

    firebaseDatabase = FirebaseDatabase.getInstance();
    myRef = firebaseDatabase.getReference();

    if (FirebaseAuth.getInstance().getCurrentUser() != null){
        userID = mAuth.getCurrentUser().getUid();
    }

}

    /**
     * This methods Registers or signs up a new user to firebase Authentication
     * @param email
     * @param password
     */
public void registerNewUserAccount(String email , String password ) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //after this method gets called the new user will be signed in
                            // Sign in success,
                            verificationEmail();
                            Log.d(TAG, "createUserWithEmail:success" );
                        } else if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
}
    public void verificationEmail(){

        final FirebaseUser user = mAuth.getCurrentUser();
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(mContext,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else if (!task.isSuccessful()) {

                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(mContext ,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    /**
     *  adds info to yhe user_edit_profile_info node
     *  adds info to yhe user_profile_account node
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void createNewUser(String userID ,String email , String username , String description ,
                              String website , String profile_photo ) {

        User user = new User( shrinkUsername(username) , email , userID , 1 );

        // Set the Object user attributes to the Database table user_edite_profile_info
        myRef.child(mContext.getString(R.string.db_user)).child(userID)
                .setValue(user);

        UserProfileAccountSetting userProfileAccountSetting =
                new UserProfileAccountSetting(description , shrinkUsername(username) ,
                0 , 0 , 0 ,profile_photo ,username ,website);

        myRef.child(mContext.getString(R.string.db_userprofileaccount))
                .child(userID).setValue(userProfileAccountSetting);


    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }


    public boolean checkIfUserIsInUse( String userID , String username , DataSnapshot dataSnapshot){


        //here we instantiate an object of User
        User user = new User();

        //this iterate will loop through all nodes and its children in realtime database
        for (DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
            //here we get the username from database"that is stored in ""."" format and put it in the class
            //to be able to compare it with
            user.setUsername(ds.getValue(User.class).getUsername());

            //I have created a method which replaces the "." from database with ""
            if (expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUserIsInUse: there is match" + username) ;
                return true;
            }

        }
        return false;
    }
    public static String expandUsername(String username){

        return username.replace("." , " ");

    }

    public static String shrinkUsername(String username){

        return username.replace(" " , ".");

    }

    /**
     * Retrieves account user info user profile account setting.
     *
     * @param dataSnapshot the data snapshot,Any time you read data from the Database,
     *                     you receive the data as a DataSnapshot
     * @return the user profile account setting
     */
    public GeneralInfoUserModel retrieveAccountUserInfo(DataSnapshot dataSnapshot){
        User user = new User();
        UserProfileAccountSetting userProfileAccountSetting = new UserProfileAccountSetting();

        for( DataSnapshot ds : dataSnapshot.getChildren()){
            if (ds.getKey().equals("user_profile_account")){

                try{

                    //Retrieving the info from tables to the models
                    userProfileAccountSetting.setDisplay_name(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getDisplay_name());

                    userProfileAccountSetting.setDescription(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getDescription());

                    userProfileAccountSetting.setFollowers(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getFollowers());

                    userProfileAccountSetting.setFollowings(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getFollowings());

                    userProfileAccountSetting.setPosts(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getPosts());

                    userProfileAccountSetting.setWebsite(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getWebsite());

                    userProfileAccountSetting.setProfile_photo(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getProfile_photo());

                    userProfileAccountSetting.setUser_name(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getUser_name());

                }catch(NullPointerException e){
                    Log.d(TAG, "retrieveAccountUserInfo: NullPointerException " + e.getMessage());
                }

            }

            if (ds.getKey().equals("user_edit_profile_info")){

                user.setUsername(ds.child(userID)
                        .getValue(User.class).getUsername());

                user.setEmail(ds.child(userID)
                        .getValue(User.class).getEmail());

                user.setPhone(ds.child(userID)
                        .getValue(User.class).getPhone());

                user.setUser_id(ds.child(userID)
                        .getValue(User.class).getUser_id());

            }


        }

            return new GeneralInfoUserModel(user , userProfileAccountSetting);

    }

}





