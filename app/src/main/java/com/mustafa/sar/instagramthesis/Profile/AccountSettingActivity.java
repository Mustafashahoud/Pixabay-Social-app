package com.mustafa.sar.instagramthesis.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.BottomNavigationViewHelper;
import com.mustafa.sar.instagramthesis.utilities.SectionsStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class AccountSettingActivity extends AppCompatActivity {
    Context mContext = AccountSettingActivity.this;
    ListView listView;
    ArrayAdapter<String> adapter;
    SectionsStatePagerAdapter sectionsStatePagerAdapter;
    ViewPager viewPager;
    RelativeLayout relLayout1;
    private static final int ACTIVITY_NUM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetting);
        viewPager = (ViewPager) findViewById(R.id.containerViewPager);
        relLayout1 = (RelativeLayout) findViewById(R.id.relLayout1);
        setupSettingList();
        handlingBackArrowNavigation();
        setupFragment();
        setupBottomNavigationView();


    }

    /**
     * //Set up the back arrow for navigating back to ProfileActivity
     */

    private void handlingBackArrowNavigation() {

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(AccountSettingActivity.this , ProfileActivity.class);
                startActivity(intent);*/
            }
        });

    }


    private void setupSettingList() {
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile_fragment));
        options.add(getString(R.string.sign_out_fragment));
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, options);
        listView = (ListView) findViewById(R.id.lvAccountSetting);


        listView.setAdapter(adapter);

        //add fragments
        setupFragment();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setupViewPager(position);
            }
        });

    }

    /**
     * add the fragment to the adapter to be populated in the listView
     * Here we can add as many fragments as we want
     */
    private void setupFragment() {

        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        // add the fragments that represent the listView items
        sectionsStatePagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment));
        sectionsStatePagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out_fragment));

    }

    private void setupViewPager(int position) {

        relLayout1.setVisibility(View.GONE);
        viewPager.setAdapter(sectionsStatePagerAdapter);
        viewPager.setCurrentItem(position);


    }

    private void setupBottomNavigationView() {

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


    }

}
