package com.example.bv_mart.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * tab的适配器
 */
public class MyTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    String[] Title = new String[]{"Order","Reviews"};

    public MyTabAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }


}
