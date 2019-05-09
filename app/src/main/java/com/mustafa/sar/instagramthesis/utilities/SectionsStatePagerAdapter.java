package com.mustafa.sar.instagramthesis.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {
    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    ArrayList<Fragment> arrayListFragment = new ArrayList<>();
    HashMap<Fragment, Integer> mFragments = new HashMap<>();
    HashMap<String, Integer> mFragmentNumbers = new HashMap<>();
    HashMap<Integer, String> mFragmentNames = new HashMap<>();

    public void addFragment(Fragment fragment, String fragmentName) {
        arrayListFragment.add(fragment);
        mFragmentNames.put(arrayListFragment.size() - 1, fragmentName);
        mFragmentNumbers.put(fragmentName, arrayListFragment.size() - 1);
        mFragments.put(fragment, arrayListFragment.size() - 1);
    }


    @Override
    public int getCount() {
        return arrayListFragment.size();
    }

    @Override
    public Fragment getItem(int position) {
        return arrayListFragment.get(position);
    }

    /**
     * Returns The Fragment number using the name
     *
     * @param fragmentName
     * @return
     */
    public Integer getFragmentNumber(String fragmentName) {
        if (mFragmentNumbers.containsKey(fragmentName)) {

            return mFragmentNumbers.get(fragmentName);

        } else {
            return null;
        }
    }

    /**
     * Returns The Fragment number using the fragment
     *
     * @param fragment
     * @return
     */
    public Integer getFragmentNumbers(Fragment fragment) {
        if (mFragmentNumbers.containsKey(fragment)) {

            return mFragmentNumbers.get(fragment);

        } else {
            return null;
        }
    }

    /**
     * Returns The Fragment number using the name
     *
     * @param fragmentNumber
     * @return
     */
    public String getFragmentName(Integer fragmentNumber) {
        if (mFragmentNames.containsKey(fragmentNumber)) {

            return mFragmentNames.get(fragmentNumber);

        } else {
            return null;
        }
    }
}





