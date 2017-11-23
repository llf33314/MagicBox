package com.gt.magicbox.custom_display;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Carson_Ho on 16/7/22.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private NoScrollViewPager viewPager;
    private String[] mTitles = new String[]{"第一步", "第二步", "第三步"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new Step02Fragment(viewPager);
        } else if (position == 2) {
            return new Step03Fragment(viewPager);
        }
        return new Step01Fragment(viewPager);
    }
    public void setViewPager(NoScrollViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
