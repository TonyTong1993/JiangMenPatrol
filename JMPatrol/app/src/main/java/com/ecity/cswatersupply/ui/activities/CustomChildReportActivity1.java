package com.ecity.cswatersupply.ui.activities;

import java.io.Serializable;

import android.content.Intent;
import android.view.View;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.CustomViewInflater;

/**
 * 
 * @author SunShan'ai
 * CusomtReportActivity子类,当InspectItem是Group类型时,启动这个类(一般情况下,不需要使用者调用)
 */
public class CustomChildReportActivity1 extends CustomReportActivity1 {
    @Override
    protected void initSingleToolbar(int imgResId, int strResId) {
        super.initSingleToolbar(R.drawable.flow_finish, R.string.finish);
    }

    /**
     * 这是第一级菜单上面的 “提交”按钮绑定事件，第二级菜单为“完成”按钮绑定事件 
     */
    @Override
    public void submitInfo(View view) {
        Intent intent = new Intent();
        intent.putExtra(CustomViewInflater.REPORT_TITLE_PARENT, mInspectItemParent);
        intent.putExtra(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) mInspectItems);
        intent.putExtra(CustomViewInflater.REPORT_MULTI_CHILD_IDENTIFY, mMulltiChildId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
