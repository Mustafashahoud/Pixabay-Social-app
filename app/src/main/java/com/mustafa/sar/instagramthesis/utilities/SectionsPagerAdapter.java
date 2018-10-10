package com.mustafa.sar.instagramthesis.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mustafa.sar.instagramthesis.Home.CameraFragment;
import com.mustafa.sar.instagramthesis.Home.HomeFragment;
import com.mustafa.sar.instagramthesis.Home.MessagesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores fragments for tabs
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> fragmentList = new ArrayList();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment f) {
        fragmentList.add(f);
    }
}
