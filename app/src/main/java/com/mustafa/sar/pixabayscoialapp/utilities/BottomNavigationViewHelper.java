package com.mustafa.sar.pixabayscoialapp.utilities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.mustafa.sar.pixabayscoialapp.Home.HomeActivity;
import com.mustafa.sar.pixabayscoialapp.Profile.ProfileActivity;
import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.Search.SearchActivity;
import com.mustafa.sar.pixabayscoialapp.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mustafa.sar.pixabayscoialapp.nearbyConnections.MainActivity;

public class BottomNavigationViewHelper {

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx bottomNavigationViewEx) {

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity.class); // ACTIVITY_NUM 0
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class);// ACTIVITY_NUM 1
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_circle:
                        Intent intent3 = new Intent(context, ShareActivity.class); // ACTIVITY_NUM 2
                        context.startActivity(intent3);
                        break;


                    case R.id.ic_nearby:
                        Intent intent4 = new Intent(context, MainActivity.class); //  ACTIVITY_NUM 3
                        context.startActivity(intent4);
                        break;


                    case R.id.ic_profile:
                        Intent intent5 = new Intent(context, ProfileActivity.class); // ACTIVITY_NUM 4
                        context.startActivity(intent5);
                        break;

                }


                return false;
            }
        });


    }

}