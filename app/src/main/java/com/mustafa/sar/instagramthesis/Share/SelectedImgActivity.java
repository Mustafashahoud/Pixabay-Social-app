package com.mustafa.sar.instagramthesis.Share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mustafa.sar.instagramthesis.Home.DetailActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.FirebaseUtilities;
import com.mustafa.sar.instagramthesis.utilities.UniversalImageLoader;

public class SelectedImgActivity extends AppCompatActivity {

    private static final String TAG = "SelectedImgActivity";
    Intent intent;
    private ImageView selectedImg;
    private TextView shareImg;
    private ImageView backarraw;
    private EditText descriptionEditText;
    private CircleProgressBar circleProgressBar;
    private int countPhoto = 0;
    private static final String mAppend = "file:/";
    /*Photo selected form the Gallery*/
    private String selectedImgPath;
    /* Photo taken by the camera*/
    private String cameraImgPath;

    /* Photo Coming from Pixabay website*/
    private String pixabayImgUrlLowQuality;
    private String pixabayImgUrlHighQuality;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener myAuthenListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUtilities firebaseUtilities;

    //Storage FireBase
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ShareActivity");
        setContentView(R.layout.selectedimg_activity);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        setupFirebaseAuth();
        firebaseUtilities = new FirebaseUtilities(SelectedImgActivity.this);
        descriptionEditText = (EditText) findViewById(R.id.description);
        shareImg = (TextView) findViewById(R.id.shareTextView);
        backarraw = (ImageView) findViewById(R.id.backarrow);
        selectedImg = (ImageView) findViewById(R.id.selectedImgFinal);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleprogressbar);
        circleProgressBar.setVisibility(View.GONE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setImg();
        backarraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the SelectedImgActivity.");
                finish();
            }
        });
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");
                String  description = descriptionEditText.getText().toString();
                intent = getIntent();
                if (intent.hasExtra("SelectedImg")){
                    firebaseUtilities.uploadPhotoUsingUrl(getString(R.string.normal_photo), description, countPhoto, selectedImgPath);

                }else if (intent.hasExtra("photo_URL_bitmap")){
                    firebaseUtilities.uploadPhotoUsingUrl(getString(R.string.normal_photo), description, countPhoto, cameraImgPath);
                }else if (intent.hasExtra(DetailActivity.IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_LOW_QUALITY)
                       && intent.hasExtra(DetailActivity.IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_High_QUALITY)){
                    firebaseUtilities.uploadPhotoUsingUrl(getString(R.string.normal_photo), description, countPhoto, pixabayImgUrlHighQuality);
                }
            }
        });
    }

    private void setImg(){
        intent = getIntent();
        if (intent.hasExtra("SelectedImg")){
            selectedImgPath = intent.getStringExtra("SelectedImg");
            UniversalImageLoader.setImage(selectedImgPath, selectedImg, null, mAppend);

        }else if (intent.hasExtra("photo_URL_bitmap")){
            cameraImgPath = intent.getStringExtra("photo_URL_bitmap");
            UniversalImageLoader.setImage(cameraImgPath, selectedImg, null, mAppend);
        }
        else if (intent.hasExtra(DetailActivity.IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_LOW_QUALITY)
              && intent.hasExtra(DetailActivity.IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_High_QUALITY)){
            pixabayImgUrlLowQuality = intent.getStringExtra(DetailActivity.IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_LOW_QUALITY);
            pixabayImgUrlHighQuality = intent.getStringExtra(DetailActivity.IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_High_QUALITY);
            UniversalImageLoader.setImage(pixabayImgUrlLowQuality, selectedImg, null, "");
        }
    }

    /* ***************************** FireBase **************************/
    public void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        myAuthenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    Log.d(TAG, "setupFirebaseAuth: User is signed in" + currentUser.getUid());

                } else if (currentUser == null) {
                    Log.d(TAG, "setupFirebaseAuth: User is signed out");
                }

            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                countPhoto = firebaseUtilities.getCountPhotos(dataSnapshot);
                Log.d(TAG, "onDataChange: The count of Photos = " + countPhoto);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });

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