package com.lz.serial.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lz.serial.R;
import com.lz.serial.SerialApp;

import java.util.List;

/**
 * @author KCrason
 * @date 2018/1/21
 */
public class FragmentViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private String[] mStrings ;

    private List<Fragment> mFragments;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;

        mStrings = SerialApp.getmContext().getResources().getStringArray(R.array.fragment);
    }

    public void update(List<Fragment> fragments){
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mStrings != null && position < mStrings.length) {
            return mStrings[position];
        }
        return "默认栏目";
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
