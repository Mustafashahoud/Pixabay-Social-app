package com.mustafa.sar.instagramthesis.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mustafa.sar.instagramthesis.Login.LoginActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.BottomNavigationViewHelper;
import com.mustafa.sar.instagramthesis.utilities.SectionsPagerAdapter;
import com.mustafa.sar.instagramthesis.utilities.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.zip.Inflater;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context context = HomeActivity.this;
    SectionsPagerAdapter mAdapter;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener myAuthenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Disabling shifting mood for the BottomNavigationView
        setupBottomNavigationView();
        setupViewPager();
        //This is for Universal Image Loader we have to instantiate it once at the first Activity
        initImageLoader();
        //This is for Firebase Authentication
        setupFirebaseAuth();
        

    }


    /**
     * Sitting up the BottomNavigationView and disabling the animation and shifting
     */
    private void setupBottomNavigationView() {

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


    }

    /**
     * This method initialises the Image loader which must be initialised once before the first usage
     * and it should be defined in HomeActivity cuz it the main activity in the app
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(context);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }


    /**
     * Responsible for adding the 3 tabs fragments : Camera , Home , Messages
     */
    private void setupViewPager() {

        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mAdapter.addFragment(new CameraFragment());// index 1
        mAdapter.addFragment(new HomeFragment()); // index 2
        mAdapter.addFragment(new MessagesFragment()); // index 3

        ViewPager mPager = (ViewPager) findViewById(R.id.containerViewPager);
        mPager.setAdapter(mAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);

        //link The ViewPager to the TabLayout
      /*  When adding the TabLayout to the viewPager
        Dynamically the tabLayout will take the ViewPager fragments so we must not Do tabLayout.addTab*/
        tabLayout.setupWithViewPager(mPager);

        //add the first tab.
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_camera));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);

        //add the second tab Using a custom layout{ImageView}
        View view = getLayoutInflater().inflate(R.layout.justforinstagramicon, null);
        view.setBackgroundResource(R.drawable.iconinsta);
        //tabLayout.addTab(tabLayout.newTab().setCustomView(view));
        tabLayout.getTabAt(1).setCustomView(view);


        //add the third tab.
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_arrow));
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);


    }
    
    // -------------------------------Firebase Stuff -----------------------

    /**
     * This method checks if the user is logged in or not and the only method
     * can give us the currentUser nt null
     */
    public void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();


        myAuthenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser =  firebaseAuth.getCurrentUser();


                if (currentUser != null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed in" + currentUser.getUid());
                    //mAuth.signOut();

                }
                else if (currentUser == null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed out" );
                    Intent intent = new Intent(context , LoginActivity.class);
                    startActivity(intent);

                }



            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: checking if the user is logged in or out");
        mAuth.addAuthStateListener(myAuthenListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(context , LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (myAuthenListener != null) {
            mAuth.removeAuthStateListener(myAuthenListener);
        }
    }
}
