package com.ecity.cswatersupply.ui.widght;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.PropertyChangeModel;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.PreferencesUtil;
import com.zzz.ecity.android.applibrary.view.BadgeView;

//九宫格气泡显示
@SuppressLint("NewApi")
public class NotificationBadgeView extends BadgeView implements PropertyChangeListener {

    public NotificationBadgeView(Context context, View target) {
        super(context, target);
        setBackground(ResourceUtil.getDrawableResourceById(R.drawable.workorder_popup_notification));
        setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示在右上角
        setGravity(Gravity.CENTER);// 数字显示在中间
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        PropertyChangeModel newModel = (PropertyChangeModel) event.getNewValue();
        if (!newModel.key.equals(this.getTag())) {
            return;
        }

        hide();
        setText(String.valueOf(newModel.value));
        if (newModel.value > 0) {
            show();
        }
    }

    public void propertyChange1(PropertyChangeEvent event) {
        Object oldObj = event.getOldValue();
        Object newObj = event.getNewValue();
        if (oldObj instanceof PropertyChangeModel) {
            PropertyChangeModel newModel = (PropertyChangeModel) newObj;
            PropertyChangeModel oldModel = (PropertyChangeModel) oldObj;
            // 只更新指定badgeView,用来区分工单处置和计划任务
            if (newModel.key.equals(this.getTag())) {
                hide();
                // 这里不能直接更新数量，因为每次传过来的value都绑定了工单key
                // 而无论是哪个key，工单处置上的badgeView都要改变
                // 所以只能计算工单改变的数量，然后更新这个数字
                int oldCount = oldModel.value;
                int newCount = newModel.value;
                //将气泡清空后，再根据新的工单委派增加数目
                String txt_oldCount = "0";
                int resultCount;
                // badgeView数量减少
                if (oldCount > newCount) {
                    int count = oldCount - newCount;
                    resultCount = Integer.valueOf(txt_oldCount) - count;

                } else {// badgeView数量增多
                    int count = newCount - oldCount;
                    resultCount = Integer.valueOf(txt_oldCount) + count;
                }
                setText(String.valueOf(resultCount));
                // 保存，如果关闭软件，读取该值
                PreferencesUtil.putInt(HostApplication.getApplication().getApplicationContext(),
                        this.getTag() + HostApplication.getApplication().getCurrentUser().getId(), resultCount);
                if (resultCount > 0) {
                    show();
                }
            }
        }
    }
}
