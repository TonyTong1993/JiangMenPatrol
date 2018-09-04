package com.ecity.cswatersupply.xg.util;

import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.LoginActivity;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.xg.model.Notification;

public class NotificationUtil {

    public  static int SPECIAL_NOTIFICATION = 911;
    public static void clearNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    public static void clearNotificationById(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(id);
    }

    public static void showNotificationInStatusBar(Context context, Notification notification) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (HostApplication.getApplication().getCurrentUser() == null) { //User has not login
            intent.setClass(context.getApplicationContext(), LoginActivity.class);
        } else {
            intent.setClass(context.getApplicationContext(), MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(MainActivity.INTENT_KEY_NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.logo_main);
        builder.setContentTitle(notification.getTitle());
        builder.setContentText(notification.getContent());
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        if (notification.getNewType() == 1) {
            //Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {1000, 10000, 1000, 10000, 1000, 10000, 1000, 10000};//隔1秒震动10秒 循环震动四次 总共震动40秒
           // vibrator.vibrate(pattern, -1);
            builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_remind));
            builder.setVibrate(pattern);
        } else {
            builder.setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_VIBRATE);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id;
        if (notification.getNewType() == 1) {
            id = SPECIAL_NOTIFICATION;
        } else {
            id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        }
        manager.notify(id, builder.build());
    }
}
