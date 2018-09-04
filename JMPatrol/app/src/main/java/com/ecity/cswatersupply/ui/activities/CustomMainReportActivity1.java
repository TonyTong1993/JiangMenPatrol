package com.ecity.cswatersupply.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ecity.cswatersupply.R;

/**
 * @author SunShan'ai
 * 这是通用上报界面的主逻辑,凡是使用通用上报界面时,统一开启这个CustomMainReportActivity
 */
public class CustomMainReportActivity1 extends CustomReportActivity1 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initSingleToolbar(int imgResId, int strResId) {
        super.initSingleToolbar(R.drawable.flow_report, R.string.event_report_submit);
    }

    /**
     * 这是第一级菜单上面的 “提交”按钮绑定事件，第二级菜单为“完成”按钮绑定事件 
     */
    @Override
    public void submitInfo(View view) {
        super.submitInfo(view);
    }
}
