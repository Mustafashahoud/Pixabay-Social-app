package com.mustafa.sar.instagramthesis.Search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.nearbyMessaging.MainActivity;
//import com.mustafa.sar.instagramthesis.nearbyMessaging.NearbyMessagesActivity;
import com.mustafa.sar.instagramthesis.utilities.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context context = SearchActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: SearchActivity");
        setContentView(R.layout.activity_home);

        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);

        setupBottomNavigationView();


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
        //menuItem.getIcon().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);


    }
}