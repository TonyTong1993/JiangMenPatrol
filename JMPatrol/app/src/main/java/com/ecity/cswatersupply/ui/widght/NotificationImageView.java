package com.ecity.cswatersupply.ui.widght;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.PropertyChangeModel;
import com.ecity.cswatersupply.utils.ResourceUtil;

/**
 * tag 0 是key tag 1 是隐含的工单数量
 * 
 * @author gaokai
 *
 */
public class NotificationImageView extends ImageView implements
        PropertyChangeListener {
    public state iconState;

    public enum state {
        /**
         * Initial state
         */
        DOWN,
        /**
         * When the PopupWindow is showing, meaning the user click the title
         */
        UP,
        /**
         * When new work order came
         */
        NEWCOUNT
    }

    public NotificationImageView(Context context) {
        super(context);
    }

    public NotificationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Object oldObj = event.getOldValue();
        Object newObj = event.getNewValue();
        if (oldObj instanceof PropertyChangeModel) {
            PropertyChangeModel newModel = (PropertyChangeModel) newObj;
            PropertyChangeModel oldModel = (PropertyChangeModel) oldObj;
            // 只更新指定badgeView,用来区分工单处置和计划任务
            if (contain(newModel.key, (String) this.getTag(R.id.tag_key))) {
                // 这里不能直接更新数量，因为每次传过来的value都绑定了工单key
                // 而无论是哪个key，title上的NotificationImageView都要改变
                // 所以只能计算工单改变的数量，然后更新这个数字
                int oldCount = oldModel.value;
                int newCount = newModel.value;
                int hide_oldCount = (Integer) getTag(R.id.tag_count);// ImageView上次隐含的数量
                int resultCount;
                // 工单数量减少
                if (oldCount > newCount) {
                    int count = oldCount - newCount;
                    resultCount = hide_oldCount - count;

                } else {// 工单数量增多
                    int count = newCount - oldCount;
                    resultCount = hide_oldCount + count;
                }
                setTag(R.id.tag_count, resultCount);
                if (resultCount > 0) {// 有新消息时，显示红点
                    iconState = state.NEWCOUNT;
                    setImageDrawable(ResourceUtil
                            .getDrawableResourceById(R.drawable.tips_red_no_text));
                } else {
                    iconState = state.DOWN;
                    // 当消息为0时，恢复下拉图标
                    setImageDrawable(ResourceUtil
                            .getDrawableResourceById(R.drawable.icon_navbar_drop_down));
                }
            }
        }
    }

    /**
     * key和badgeView的tag只要是包含关系就更新badgeView 因为当有新工单时，workorderPopup和九宫格都要更新
     * 约定它们的key为包含关系{@link #Constants}
     */
    public boolean contain(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.contains(str2) || str2.contains(str1);
    }

}
