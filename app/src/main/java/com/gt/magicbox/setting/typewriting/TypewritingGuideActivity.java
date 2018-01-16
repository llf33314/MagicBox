package com.gt.magicbox.setting.typewriting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.custom_display.IndicatorLayout;
import com.gt.magicbox.custom_display.MyFragmentPagerAdapter;
import com.gt.magicbox.custom_display.NoScrollViewPager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ServiceUtils;
import com.service.CustomerDisplayService;

/**
 * Description:
 * Created by jack-lin on 2017/11/20 0020.
 * Buddha bless, never BUG!
 */

public class TypewritingGuideActivity extends BaseActivity {
    private NoScrollViewPager mViewPager;
    private TypewritingFragmentPagerAdapter myFragmentPagerAdapter;
    private IndicatorLayout indicatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typewriting_guide);
        initViews();
    }
    private void initViews() {
        indicatorLayout=(IndicatorLayout)findViewById(R.id.indicatorLayout);
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new TypewritingFragmentPagerAdapter(getSupportFragmentManager());
        myFragmentPagerAdapter.setViewPager(mViewPager);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangeListener);
        //将TabLayout与ViewPager绑定在一起
    }
    ViewPager.OnPageChangeListener pageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentIndex(position);
            indicatorLayout.updateView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
