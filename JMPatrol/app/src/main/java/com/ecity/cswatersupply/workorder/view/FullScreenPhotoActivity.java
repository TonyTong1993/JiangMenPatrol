/**
 * 文件名：FullScreenPhotoActivity.java
 *
 * 版本信息：
 * 日期：2016年6月27日
 * Copyright Ecity 2016 
 * 版权所有
 *
 */

package com.ecity.cswatersupply.workorder.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ecity.cswatersupply.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 此类描述的是：全屏展示照片，不能编辑
 * 
 * @author: gaokai
 * @version: 2016年6月27日 下午5:27:42
 */
@SuppressWarnings("deprecation")
public class FullScreenPhotoActivity extends BaseActivity {
    private int initalId;
    private ViewPager pager;
    private ArrayList<String> paths;
    private MyPageAdapter adapter;
    public static final String KEY_PATH = "PATH";
    public static final String KEY_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        initData();
        initView();
    }

    private void initData() {
        this.initalId = getIntent().getExtras().getInt(KEY_POSITION);
        this.paths = getIntent().getExtras().getStringArrayList(KEY_PATH);
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setOnPageChangeListener(pageChangeListener);
        adapter = new MyPageAdapter(paths);
        pager.setAdapter(adapter);
        pager.setCurrentItem(initalId);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);// 底部圆形指示器
        indicator.setViewPager(pager);
    }

    private class MyPageAdapter extends PagerAdapter {
        private ArrayList<String> paths;

        public MyPageAdapter(ArrayList<String> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = paths.get(position);
            ImageView image = new ImageView(FullScreenPhotoActivity.this);
            Glide.with(FullScreenPhotoActivity.this).load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).fallback(R.drawable.ic_launcher).into(image);
            image.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            container.addView(image);
            return image;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
