package com.mustafa.sar.instagramthesis.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mustafa.sar.instagramthesis.Home.HomeActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.DelayUtil;
import com.mustafa.sar.instagramthesis.utilities.FirebaseUtilities;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private Context mContext = RegisterActivity.this;

    //firebase variables
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener myAuthenListener;

    //firebase database
    FirebaseDatabase database;
    DatabaseReference myRef;

    //FirebaseUtilities
    FirebaseUtilities firebaseUtilities;

    //Widgets
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;
    private ProgressBar mProgressBar;
    public String userID;

    //append to username in case there is a match
    String append;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*//database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
*/

        //prepare Widgets
        prepareWidgets();

        //FirebaseUtilities
        firebaseUtilities = new FirebaseUtilities(mContext);


        //Setting up a listener when the user signs in
        setupFirebaseAuth();


        //Registering a new Account by pressing the register button
        addNewUser();


    }


    /**
     * Initialize the activity widgets
     */
    private void prepareWidgets() {
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mUsername = (EditText) findViewById(R.id.nameLogin);
        mEmail = (EditText) findViewById(R.id.emailInput);
        btnRegister = (Button) findViewById(R.id.registerButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressRegister);
        loadingPleaseWait = (TextView) findViewById(R.id.pleaseWaitTv);
        mPassword = (EditText) findViewById(R.id.passwordInput);
        mContext = RegisterActivity.this;
        mProgressBar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);

    }
    //.............................................Firebase ................................................

    /**
     * Setting up a listener when the user signs in
     * and The @NonNull FirebaseAuth firebaseAuth is the only way to get the actual userId
     * using currentUser = firebaseAuth.getCurrentUser(); userID = firebaseAuth.getUid();
     */
    public void setupFirebaseAuth() {
        //Authentication
        mAuth = FirebaseAuth.getInstance();


        myAuthenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                userID = firebaseAuth.getUid();

                if (currentUser != null) {
                    Log.d(TAG, "setupFirebaseAuth: User is signed in  " + currentUser.getUid());
                    // Read from the database

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mProgressBar.setVisibility(View.VISIBLE);
                            loadingPleaseWait.setVisibility(View.VISIBLE);

                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            //What i want to do here is to
                            Log.d(TAG, "onDataChange: we are in onDataChange");

                            // 1- To check if the user is already in use

                            if (firebaseUtilities.checkIfUserIsInUse(userID , username
                                    , dataSnapshot)) {
                                //this means the username is already in use
                                //so we have to append a random string to it
                                //and the user can change it later on

                                append = myRef.push().getKey().substring(5, 10);
                            }
                            // if the user name is not in use the append will stay ""
                            username = username + append;

                            // 2- add a new user (in my realtime database is user_edite_profile_info)
                            //3- add a new account user setting (user_profile_setting)

                            firebaseUtilities.createNewUser(userID ,email, username, "", "", "");

                            Toast.makeText(mContext, "Your account has been add successfully! ", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                            mProgressBar.setVisibility(View.GONE);

                            //navigationToHomeActivity(RegisterActivity.this);

                            //It will finish the current activity and go back to the previous activity.
                            //so the user has to sign in again after verifying their email.
                            finish();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                } else if (currentUser == null) {
                    Log.d(TAG, "setupFirebaseAuth: User is signed out");


                }

            }
        };

    }

    /**
     * This method is responsible for creating a new user but it does not store data about the user
     * only stores email and userID
     * if we wanna store stuff like photos and some info we need to use firebase Database
     */

    public void addNewUser() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference();

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                username = mUsername.getText().toString();

                if (isEmpty(email) || isEmpty(password) || isEmpty(username)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);
                    Log.d(TAG, "createNewAccount: there is a missing field");
                    Toast.makeText(mContext, "you must fill out all fields"
                            , Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    loadingPleaseWait.setVisibility(View.GONE);

                } else if (!isEmpty(email) || !isEmpty(password) || !isEmpty(username)) {
                    Log.d(TAG, "createNewAccount: all fields are filled out");
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);
                    //calling  registerNewUserAccountUtilMethod() for registering or signing up a new user
                    firebaseUtilities.registerNewUserAccount(email, password);

                    Toast.makeText(mContext, "Authentication success, Sending verification email ... ", Toast.LENGTH_SHORT).show();

                    //We have to sign the user out till they verify their email by pressing on the verification email
                    mAuth.signOut();


                    // we had to to stop the program for 2 sec till we become able to get the authentication cuz it is gonna register
                    // the account using createUserWithEmailAndPassword() but the better way is to put inside the listener in setupFirebaseAuth
                    /*DelayUtil delayUtil = new DelayUtil();
                    System.out.println("started:"+ new Date());
                    delayUtil.delay(2000);
                    System.out.println("half second after:"+ new Date());

                    *//**
                     * database listener .. it is called whenever there is a change or update in database.
                     * myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                     *//*
                    */
                    mProgressBar.setVisibility(View.GONE);
                    loadingPleaseWait.setVisibility(View.GONE);

                }

            }
        });

    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }






    @Override
    public void onStart() {
        super.onStart();
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
