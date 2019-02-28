package com.mustafa.sar.instagramthesis.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mustafa.sar.instagramthesis.Home.HomeActivity;
import com.mustafa.sar.instagramthesis.Profile.AccountSettingActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.models.GeneralInfoUserModel;
import com.mustafa.sar.instagramthesis.utilities.models.Photo;
import com.mustafa.sar.instagramthesis.utilities.models.User;
import com.mustafa.sar.instagramthesis.utilities.models.UserProfileAccountSetting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class FirebaseUtilities {

    private FirebaseAuth mAuth;
    private Context mContext;
    private String userID;
    private Activity activity;

    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference storageRef;

    CircleProgressBar circleProgressBar;

    public FirebaseUtilities(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    public FirebaseUtilities(Activity activity) {

        this.activity = activity;

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

    }
    /**
     * This methods Registers or signs up a new user to firebase Authentication
     *
     * @param email
     * @param password
     */
    public void registerNewUserAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //after this method gets called the new user will be signed in
                            // Sign in success
                            verificationEmail();
                            // firebaseUtilities.createNewUser(userID ,email, mUsername, "", "", "");
                            Log.d(TAG, "createUserWithEmail:success");
                        } else if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verificationEmail() {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: The verification email has been sent to " + user.getEmail());


                            } else if (!task.isSuccessful()) {
                                Toast.makeText(activity, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    /**
     * adds info to yhe user_edit_profile_info node
     * adds info to yhe user_profile_account node
     *
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void createNewUser(String userID, String email, String username, String description,
                              String website, String profile_photo) {

        User user = new User(userID, 1, email, shrinkUsername(username));

        // Set the Object user attributes to the Database table user_edite_profile_info
        myRef.child(activity.getString(R.string.db_user)).child(userID)
                .setValue(user);

        UserProfileAccountSetting userProfileAccountSetting =
                new UserProfileAccountSetting(description, shrinkUsername(username),
                        0, 0, 0, profile_photo, username, website);

        myRef.child(activity.getString(R.string.db_userprofileaccount))
                .child(userID).setValue(userProfileAccountSetting);


    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }


    public boolean checkIfUserIsInUse(String userID, String username, DataSnapshot dataSnapshot) {
        //here we instantiate an object of User
        User user = new User();

        //this iterate will loop through all nodes and its children in realtime database
        for (DataSnapshot ds : dataSnapshot.child(userID).getChildren()) {
            //here we get the username from database"that is stored in ""."" format and put it in the class
            //to be able to compare it with
            user.setUsername(ds.getValue(User.class).getUsername());

            //I have created a method which replaces the "." from database with ""
            if (expandUsername(user.getUsername()).equals(username)) {
                Log.d(TAG, "checkIfUserIsInUse: there is match" + username);
                return true;
            }

        }
        return false;
    }


    public static String expandUsername(String username) {

        return username.replace(".", " ");

    }

    public static String shrinkUsername(String username) {

        return username.replace(" ", ".");

    }

    /**
     * Retrieves account user info user profile account setting.
     *
     * @param dataSnapshot the data snapshot,Any time you read data from the Database,
     *                     you receive the data as a DataSnapshot
     * @return the user profile account setting
     */
    public GeneralInfoUserModel retrieveAccountUserInfo(DataSnapshot dataSnapshot) {
        User user = new User();
        UserProfileAccountSetting userProfileAccountSetting = new UserProfileAccountSetting();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals("user_profile_account")) {

                try {

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

                    userProfileAccountSetting.setUsername(ds.child(userID)
                            .getValue(UserProfileAccountSetting.class).getUsername());

                } catch (NullPointerException e) {
                    Log.d(TAG, "retrieveAccountUserInfo: NullPointerException " + e.getMessage());
                }

            }

          /*  if (ds.getKey().equals("user_edite_profile_info")){

                try {

                    user.setUsername(ds.child(userID)
                            .getValue(User.class).getUsername());

                    user.setEmail(ds.child(userID)
                            .getValue(User.class).getEmail());

                    user.setPhone(ds.child(userID)
                            .getValue(User.class).getPhone());

                    user.setUser_id(ds.child(userID)
                            .getValue(User.class).getUser_id());

                }
                catch (NullPointerException e){

                }

            }*/

            if (ds.getKey().equals("user_edite_profile_info")) {
                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }


        }

        return new GeneralInfoUserModel(user, userProfileAccountSetting);

    }

    /**
     * Update 'user_account_settings' node for the current user
     *
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, long phoneNumber) {

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if (displayName != null) {
            myRef.child(activity.getString(R.string.db_userprofileaccount))
                    .child(userID)
                    .child("display_name")
                    .setValue(displayName);
        }


        if (website != null) {
            myRef.child(activity.getString(R.string.db_userprofileaccount))
                    .child(userID)
                    .child("website")
                    .setValue(website);
        }

        if (description != null) {
            myRef.child(activity.getString(R.string.db_userprofileaccount))
                    .child(userID)
                    .child("description")
                    .setValue(description);
        }

        if (phoneNumber != 0) {
            myRef.child(activity.getString(R.string.db_user))
                    .child(userID)
                    .child("phone_number")
                    .setValue(phoneNumber);
        }
    }

    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: updating username to: " + username);

        myRef.child(activity.getString(R.string.db_user))
                .child(userID)
                .child(activity.getString(R.string.field_username))
                .setValue(username);

        myRef.child(activity.getString(R.string.db_userprofileaccount))
                .child(userID)
                .child(activity.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * Get count photos that a user has Uploaded.
     * I am gonna need to name the photos {photo1, photo2, .....}
     *
     * @param dataSnapshot the data snapshot
     * @return the int
     */
    public int getCountPhotos(DataSnapshot dataSnapshot) {
        int count = 0;
        for (DataSnapshot ds : dataSnapshot.child("user_photos")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()) {
            count++;

        }
        return count;
    }

    private String getTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Budapest"));
        return  simpleDateFormat.format(new Date());
    }
    public static String getTags(String string){
        if(string.indexOf("#") > 0){
            StringBuilder sb = new StringBuilder();
            char[] charArray = string.toCharArray();
            boolean foundWord = false;
            for( char c : charArray){
                if(c == '#'){
                    foundWord = true;
                    sb.append(c);
                }else{
                    if(foundWord){
                        sb.append(c);
                    }
                }
                if(c == ' ' ){
                    foundWord = false;
                }
            }
            String s = sb.toString().replace(" ", "").replace("#", ",#");
            return s.substring(1, s.length());
        }
        return string;
    }


    public void savePhotoToDatabase (String description, String photoStringUrl){

        String photoKey = myRef.child("user_photos").push().getKey();
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Photo photo = new Photo();

        photo.setCaption(description);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(photoStringUrl);
        photo.setPhoto_id(photoKey);
        photo.setTags(getTags(description));
        photo.setUser_id(user_id);

        myRef.child("user_photos").child(user_id).child(photoKey).setValue(photo);
        myRef.child("photos").child(photoKey).setValue(photo);
    }

    public void saveProfilePhotoToDatabase(String url){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child("user_profile_account").child(user_id).child("profile_photo").setValue(url);
    }

    public void uploadPhoto(String picType, final String description, int photoCount, String url) {

        final FileDirectory fd = new FileDirectory(); //fd.STORAGE_PHOTO_PATH --> photos/users

        if (picType.equals(activity.getString(R.string.normal_photo))) {

            String user_id = "";

            if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
            //photos/users/user_id/Photo{countPhoto+1}

            Uri file = Uri.fromFile(new File(url));

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

            final StorageReference ref = storageRef.child(fd.STORAGE_PHOTO_PATH + user_id + "/Photo" + (photoCount + 1));
            UploadTask uploadTask = ref.putFile(file , metadata);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getError();
                    float progress = (float) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    float i = 0;
                    circleProgressBar = (activity).findViewById(R.id.circleprogressbar);
                    circleProgressBar.setProgress(progress);
                    while (progress - 15 > i) {
                        circleProgressBar.setSuffix(progress + "%");
                        circleProgressBar.setVisibility(View.VISIBLE);
                        i = progress;
                    }

                    ((AccountSettingActivity)activity).setupViewPager(
                            ((AccountSettingActivity)activity).sectionsStatePagerAdapter.getFragmentNumber(
                                    activity.getString(R.string.edit_profile_fragment)));


                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    int errorCode = ((StorageException) exception).getErrorCode();
                    String errorMessage = exception.getMessage();
                    // test the errorCode and errorMessage, and handle accordingly
                    Log.d(TAG, "onSuccess: The photo has NOT been uploaded" + errorCode + errorMessage);
                    Toast.makeText(activity, "Sorry the photo has not been uploaded .. Please Try again", Toast.LENGTH_SHORT);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    circleProgressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onSuccess: The photo is being uploaded");
                    Toast.makeText(activity, "The photo has been uploaded successfully", Toast.LENGTH_SHORT).show();
                    //
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null){
                            String photoStringUrl = downloadUri.toString();
                            Log.d(TAG, "onComplete: Upload " + photoStringUrl);

                            //Inserting the photo to database
                            savePhotoToDatabase(description, photoStringUrl);

                            //Navigating to Home Screen Activity
                            Intent intent = new Intent(activity , HomeActivity.class);
                            activity.startActivity(intent);
                        }
                    }
                }
            });

 /***PPPRRRROOOOOFFFFFIIIILLLLEEEE PPPHHHOOOOTTTTOOOO**/

        } else if (picType.equals(activity.getString(R.string.profile_photo))) {

            String user_id = "";

            if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

            Uri file = Uri.fromFile(new File(url));

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

            final StorageReference ref = storageRef.child(fd.STORAGE_PHOTO_PATH + user_id + "/profile_photo");
            UploadTask uploadTask = ref.putFile(file , metadata);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getError();
                    float progress = (float) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    float i = 0;
                    circleProgressBar = (activity).findViewById(R.id.circleprogressbar);
                    circleProgressBar.setProgress(progress);
                    while (progress - 15 > i) {
                        circleProgressBar.setSuffix(progress + "%");
                        circleProgressBar.setVisibility(View.VISIBLE);
                        i = progress;
                    }
                }

            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    int errorCode = ((StorageException) exception).getErrorCode();
                    String errorMessage = exception.getMessage();
                    // test the errorCode and errorMessage, and handle accordingly
                    Log.d(TAG, "onSuccess: The photo has NOT been uploaded" + errorCode + errorMessage);
                    Toast.makeText(activity, "Sorry the photo has not been uploaded .. Please Try again", Toast.LENGTH_SHORT);
                    circleProgressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: The photo is being uploaded");
                    Toast.makeText(activity, "The photo has been uploaded successfully", Toast.LENGTH_SHORT).show();
                    circleProgressBar.setVisibility(View.GONE);
                    ((AccountSettingActivity)activity).setupViewPager(
                            ((AccountSettingActivity)activity).sectionsStatePagerAdapter.getFragmentNumber(
                                    activity.getString(R.string.edit_profile_fragment)));
                    //
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null){
                            String photoStringUrl = downloadUri.toString();
                            Log.d(TAG, "onComplete: Upload " + photoStringUrl);

                            //Inserting the profile photo into database {user_profile_account}
                            saveProfilePhotoToDatabase(photoStringUrl);
                        }
                    }
                }
            });

        }

    }

}
