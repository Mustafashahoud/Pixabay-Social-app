package com.mustafa.sar.pixabayscoialapp.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mustafa.sar.pixabayscoialapp.Login.LoginActivity;
import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.models.Photo;
import com.mustafa.sar.pixabayscoialapp.post.ViewCommentFragment;
import com.mustafa.sar.pixabayscoialapp.utilities.BottomNavigationViewHelper;
import com.mustafa.sar.pixabayscoialapp.utilities.SectionsPagerAdapter;
import com.mustafa.sar.pixabayscoialapp.utilities.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;
    SectionsPagerAdapter mAdapter;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener myAuthenListener;

    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager = (ViewPager) findViewById(R.id.containerViewPager);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutParent);

        //This is for Firebase Authentication
        setupFirebaseAuth();

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
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    /**
     * This method initialises the Image loader which must be initialised once before the first usage
     * and it should be defined in HomeActivity cuz it the main activity in the app
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    public void hideLayout(){
        Log.d(TAG, "hideLayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
    }

    public void showLayout(){
        Log.d(TAG, "hideLayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finishAffinity();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Responsible for adding the 3 tabs fragments : Camera , Home , Messages
     */
    private void setupViewPager() {
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new HomeFragment());  // index 1
        mAdapter.addFragment(new PixabayFragment());// index 2
       // mAdapter.addFragment(new MessagesFragment()); // index 3

        mViewPager.setAdapter(mAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);

        //link The ViewPager to the TabLayout
      /*  When adding the TabLayout to the viewPager
        Dynamically the tabLayout will take the ViewPager fragments so we must not Do tabLayout.addTab*/
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_social);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_pixabay);
        //add the second tab Using a custom layout{ImageView}
       /* View view = getLayoutInflater().inflate(R.layout.justforpixabayicon, null);
        view.setBackgroundResource(R.drawable.ic_pixabay);*/
        //tabLayout.addTab(tabLayout.newTab().setCustomView(view));
       // tabLayout.getTabAt(1).setCustomView(view);

    }
    public void onCommentSelected(Photo photo, String calling){
        Log.d(TAG, "onCommentSelected: selected a comment");
        ViewCommentFragment fragment  = new ViewCommentFragment();
        Bundle args = new Bundle();
        args.putParcelable("getPhoto", photo);
        args.putString("Home Activity", calling);
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();

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
                    Log.d(TAG, "setupFirebaseAuth: User is signed in " + currentUser.getUid());
                   //mAuth.signOut();
                }
                //System.out.println("Tokens revoked at: " + revocationSecond);
                else if (currentUser == null){
                    Log.d(TAG, "setupFirebaseAuth: User is signed out" );
                    Intent intent = new Intent(mContext , LoginActivity.class);
                    startActivity(intent);

                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
         // set the Home Fragment first
        Log.d(TAG, "onStart: checking if the user is logged in or out");
        mAuth.addAuthStateListener(myAuthenListener);

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(mContext , LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            mViewPager.setCurrentItem(0);
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
