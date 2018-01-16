package com.gt.magicbox.setting.typewriting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gt.magicbox.custom_display.NoScrollViewPager;
import com.gt.magicbox.custom_display.Step02Fragment;
import com.gt.magicbox.custom_display.Step03Fragment;

/**
 * Created by Carson_Ho on 16/7/22.
 */
public class TypewritingFragmentPagerAdapter extends FragmentPagerAdapter {
    private NoScrollViewPager viewPager;
    private String[] mTitles = new String[]{"第一步", "第二步"};

    public TypewritingFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TypewritingStep01(viewPager);
        } else if (position == 1) {
            return new TypewritingStep02(viewPager);
        }
        return new TypewritingStep01(viewPager);
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
