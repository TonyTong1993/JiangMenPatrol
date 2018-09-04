package com.ecity.cswatersupply.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ViewPagerAdapter;

public class GuideActivity extends Activity implements OnPageChangeListener {

    private ViewPager viewPager;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private ImageView[] dots;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_new);
        initViews();
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guide_viewpager_page1, null));
        views.add(inflater.inflate(R.layout.guide_viewpager_page2, null));

        vpAdapter = new ViewPagerAdapter(views, this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1 || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentDot(arg0);
    }

}
