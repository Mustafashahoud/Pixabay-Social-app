package com.mustafa.sar.instagramthesis.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mustafa.sar.instagramthesis.utilities.Permission;
import com.mustafa.sar.instagramthesis.utilities.SectionsPagerAdapter;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ShareActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context context = ShareActivity.this;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ShareActivity");
        setContentView(R.layout.activity_share);

        // Checks if all permissions are confirmed
        if (checkPermissionsArray(Permission.PERMISSION)){

            setupViewPager();

        }else{
            verifyPermission(Permission.PERMISSION);
        }

        //setupBottomNavigationView();


    }

    public int getIntentFlag(){
        return getIntent().getFlags();
    }

    /**
     *
     * @return  CurrentTabNumber
     */
    public int getCurrentTabNumber(){
        return  mViewPager.getCurrentItem();
    }

    /**
     * check an array of permission
     * @param permission
     * @return
     */
    public boolean checkPermissionsArray(String[] permission){

        for (int i = 0 ; i < permission.length; i++) {
            String check =  permission[i];
            if (!checkPermission(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * check a singal permission if it is verified
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission){

        int permissionRequest = ActivityCompat.checkSelfPermission(context , permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: Permission was not granted for " + permission);
            return false;
        }else{
            Log.d(TAG, "checkPermission: Permission was not granted for " + permission);
            return true;
        }
    }
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        adapter.addFragment(new VideoFragment());
        ;
        mViewPager = (ViewPager) findViewById(R.id.containerViewPager);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.bottomTabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("GALLERY");
        tabLayout.getTabAt(1).setText("PHOTO");
        tabLayout.getTabAt(2).setText("VIDEO");
    }
    /**
     * verify all the permission passed to the array
     * @param permission
     */
    public void verifyPermission(String[] permission){

        ActivityCompat.requestPermissions(ShareActivity.this , permission , 1 );
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