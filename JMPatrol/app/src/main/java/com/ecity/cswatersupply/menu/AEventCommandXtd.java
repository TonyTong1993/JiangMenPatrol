package com.ecity.cswatersupply.menu;

import android.os.Bundle;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.event.EventType;
import com.ecity.cswatersupply.ui.activities.EventListActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.UIHelper;

/**
 * 主界面四种事件菜单的基类。
 * @author jonathanma
 *
 */
public abstract class AEventCommandXtd extends AMenuCommand {

    protected abstract int getScreenTitleId();
    protected abstract EventType getEventType();
    public int taskid;
    
    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }
    @Override
    public int getMenuIconResourceId(String iconName) {
        return ResourceUtil.getDrawableResourceId(iconName);
    }

    @Override
    public boolean execute() {
        SessionManager.reportType = 0;//选择上报类型为事件上报
        Bundle extra = new Bundle();
        extra.putInt(EventListActivity.INTENT_KEY_TASK_ID, taskid);
        extra.putInt(EventListActivity.INTENT_KEY_TITLE_ID, getScreenTitleId());
        extra.putInt(EventListActivity.INTENT_KEY_EVENT_TYPE, Integer.valueOf(String.valueOf(getEventType().getValue())));
        UIHelper.startActivityWithExtra(EventListActivity.class, extra);

        return true;
    }
}
