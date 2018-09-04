package com.ecity.cswatersupply.emergency.activity;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
/**
 * 
 * 消息公告详情界面
 * @author 49136
 * 
 */

public class NewsAnnouncementDetail extends Activity{
    private CustomTitleView news_customTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_announcement_detail);
        initUi();
        initDatas();
    }

    private void initDatas() {
        //这里根据不同标示显示不同的详情
        //判断数据的标示
        
    }

    private void initUi() {
        news_customTitleView = (CustomTitleView) findViewById(R.id.news_customTitleView);
        news_customTitleView.setTitleText("详情");
    }
    public void onBackButtonClicked(View v) {
        finish();
    }
}
