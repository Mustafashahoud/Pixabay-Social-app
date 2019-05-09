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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mustafa.sar.instagramthesis.Home.HomeActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.FirebaseUtilities;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Context mContext = LoginActivity.this;
    //firebase variables
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener myAuthenListener;

    //FirebaseUtilities
    FirebaseUtilities firebaseUtilities;



    //widgets
    private ProgressBar progressBar;
    private TextView mPleaseWait;
    private EditText mEmail , mPassword;
    private Button loginButton ;
    private TextView createNewAccountLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //prepare Widgets
        prepareWidgets();


        //FirebaseUtilies
        firebaseUtilities = new FirebaseUtilities(mContext);

        //Setting the ProgressBar invisible
        progressBar.setVisibility(View.GONE);
        mPleaseWait.setVisibility(View.GONE);

        //Setting up a listener to catch when the user signs in
        setupFirebaseAuth();

        // Creating a new account
        LoginProcessImpl();

        //For Navigation to Register Activity
        setupNavigationForCreateNewAccountLink();

    }

    /**
     * This method slinks the all widgets that we need
     */
    public void prepareWidgets(){
        progressBar = (ProgressBar) findViewById(R.id.progressLogin);
        mPleaseWait =(TextView) findViewById(R.id.pleaseWaitTv);
        mEmail = (EditText) findViewById(R.id.emailInput);
        mPassword = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button)findViewById(R.id.loginButton);
        createNewAccountLink =(TextView) findViewById(R.id.linkCreateAccount);
    }



    public void setupNavigationForCreateNewAccountLink(){
        createNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Navigation to Register Activity ");
                Intent intent = new Intent (LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }


    //..............................................Firebase .....................................................

    /**
     * Thi Method is to Sign in or log in the user
     */
    public void LoginProcessImpl(){

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final String password = mPassword.getText().toString();
                    final String email = mEmail.getText().toString();

                if (isEmpty(email) || isEmpty(password)){
                    Log.d(TAG, "onClick: LoginButton clicked with  Empty Password and/or Email");
                    Toast.makeText(mContext,"My dear you must fill out the both fields",Toast.LENGTH_SHORT).show();
                }
                else if (!isEmpty(email) && !isEmpty(password)) {
                    Log.d(TAG, "onClick: LoginButton clicked with filled Password and Email");
                    progressBar.setVisibility(View.VISIBLE);
                    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this ,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if (task.isSuccessful()) {
                                        try{
                                            if (user.isEmailVerified()){

                                                Log.d(TAG, "onComplete: success. email is verified");
                                                Log.d(TAG , "signInWithEmail:success ");
                                                Toast.makeText(LoginActivity.this , "Authentication success",
                                                        Toast.LENGTH_SHORT ).show();
                                                progressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                navigationToHomeActivity(LoginActivity.this);

                                            }else if(!user.isEmailVerified()){
                                                Toast.makeText(mContext,"The email is not verified .. Please check your inbox" ,Toast.LENGTH_SHORT).show();
                                                firebaseUtilities.verificationEmail();
                                                progressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();

                                            }

                                        }catch(NullPointerException e){

                                        }

                                        // after pressing the LoginButton if the Authentication is successful
                                        // it will navigate to Home Activity
                                     /*   currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null ){
                                            Log.d(TAG, "LoginProcessImpl: Current User is Logged in we have to go to Home activity");
                                            Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }*/


                                    } else if (!task.isSuccessful()) {

                                        //Password or/and email is not correct
                                        Log.d(TAG , "signInWithEmail:failure ");
                                        Toast.makeText(LoginActivity.this, "Authentication failed .. check password and/or email",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        mPleaseWait.setVisibility(View.GONE);

                                    }

                                    // ...
                                }
                            });

                }

            }
        });

    }

    /**
     * Setting up a listener when the user signs in
     */
    public void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        currentUser =  mAuth.getCurrentUser();

        myAuthenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser =  firebaseAuth.getCurrentUser();
                if (currentUser != null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed in" + currentUser.getUid());
                }
                else if (currentUser == null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed out");

                }

            }
        };
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
    public static void navigationToHomeActivity (Context contextSource) {
        Intent intent = new Intent(contextSource , HomeActivity.class);
        contextSource.startActivity(intent);
    }
}
