package com.mustafa.sar.instagramthesis.Share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.FirebaseUtilities;
import com.mustafa.sar.instagramthesis.utilities.UniversalImageLoader;

public class SelectedImgActivity extends AppCompatActivity {

    private static final String TAG = "SelectedImgActivity";

    private ImageView selectedImg;
    private TextView shareImg;
    private ImageView backarraw;
    private Context mContext = SelectedImgActivity.this;
    private EditText descriptionEditText;
    private CircleProgressBar circleProgressBar;


    private int countPhoto = 0;


    private static final String mAppend = "file:/";
    /*Photo selected form the Gallery*/
    private String selectedImgPath;
    /* Photo taken by the camera*/
    private String cameraImgPath;


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

    Intent intent;


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
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleprogressbar);
        circleProgressBar.setVisibility(View.GONE);
        //disable the keyboard when accessing the activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Sets the chosen img in the imageView
        // Receiving the selected image from  Gallery fragment and displaying it ..
        setImg();


//        final String caption = description.getText().toString();


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
                    firebaseUtilities.uploadPhotoUsingUrlUsingUrl(getString(R.string.normal_photo), description, countPhoto, selectedImgPath);

                }else if (intent.hasExtra("photo_URL_bitmap")){
                    firebaseUtilities.uploadPhotoUsingUrlUsingUrl(getString(R.string.normal_photo), description, countPhoto, cameraImgPath);
                }
            }
        });
    }

    private void setImg(){
        intent = getIntent();
        if (intent.hasExtra("SelectedImg")){
            selectedImgPath = intent.getStringExtra("SelectedImg");
            selectedImg = (ImageView) findViewById(R.id.selectedImgFinal);
            UniversalImageLoader.setImage(selectedImgPath, selectedImg, null, mAppend);

        }else if (intent.hasExtra("photo_URL_bitmap")){
            cameraImgPath = intent.getStringExtra("photo_URL_bitmap");
            selectedImg = (ImageView) findViewById(R.id.selectedImgFinal);
            UniversalImageLoader.setImage(cameraImgPath, selectedImg, null, mAppend);
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