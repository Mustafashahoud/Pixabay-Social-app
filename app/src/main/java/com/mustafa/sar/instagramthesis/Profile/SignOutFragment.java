package com.mustafa.sar.instagramthesis.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mustafa.sar.instagramthesis.Login.LoginActivity;
import com.mustafa.sar.instagramthesis.R;

import java.lang.reflect.Array;

import static android.content.ContentValues.TAG;

public class SignOutFragment extends Fragment {

    FirebaseAuth  mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    TextView signingOutTv ;
    Button btnConfirmSignOut ;
    ProgressBar signingOutProgress ;
    FirebaseUser currentUser ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signout, container, false);

        signingOutTv = (TextView) view.findViewById(R.id.signingOutTv);
        signingOutProgress = (ProgressBar) view.findViewById(R.id.signingOutProgress);

        btnConfirmSignOut = (Button) view.findViewById(R.id.btnConfirmSignOut);

        setupFirebaseAuth();


        signingOutProgress.setVisibility(View.GONE);
        signingOutTv.setVisibility(View.GONE);

        btnConfirmSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signingOutProgress.setVisibility(View.VISIBLE);
                signingOutTv.setVisibility(View.VISIBLE);

                mAuth.signOut();

                signingOutProgress.setVisibility(View.GONE);
                signingOutTv.setVisibility(View.GONE);

                getActivity().finish();

            }
        });

        return view;
    }

    /**
     * This method checks if the user is logged in or not and the only method
     * can give us the currentUser nt null
     */
    public void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 currentUser =  firebaseAuth.getCurrentUser();

                if (currentUser != null){
                    Log.d(TAG, "SignOutFragment: User is signed in" + currentUser.getUid());
                    //mAuth.signOut();

                }
                else if (currentUser == null){
                    Log.d(TAG, "SignOutFragment: User is signed out" );

                    Log.d(TAG, "onAuthStateChanged: User is null after signing out so Naviating back to the Login Activity ");

                    Intent intent = new Intent(getActivity() , LoginActivity.class);

                    /*
                    Intent Flags in java OR launch modes of activity in manifest
                     */
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: checking if the user is logged in or out");
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
