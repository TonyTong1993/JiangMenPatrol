package com.ecity.cswatersupply.workorder;

import android.content.Context;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.xg.INotificationProcesser;
import com.ecity.cswatersupply.xg.model.Notification;

/**A在值班时，B点签到，成功后，会将A挤掉。A会收到一个通知.
 * @author jonathanma
 *
 */
public class OffDutyNotificationProcesser implements INotificationProcesser {

    @Override
    public void processNotification(Context context, Notification notification) {
        UIEvent event = new UIEvent(UIEventStatus.NOTIFICATION_SIGN_OUT);
        event.setData(notification);
        EventBusUtil.post(event);
    }
}
