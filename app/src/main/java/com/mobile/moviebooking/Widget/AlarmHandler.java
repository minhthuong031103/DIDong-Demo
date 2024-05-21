package com.mobile.moviebooking.Widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmHandler {

    private final Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

    public void setAlarmManager() {
        Intent intent = new Intent(context, WidgetService.class);
        PendingIntent sender;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sender = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            sender = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //get current time and add 10 seconds
        Calendar c = Calendar.getInstance();
        long l = c.getTimeInMillis() + 5000;

        //set the alarm for 10 seconds in the future
        if (am != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, l, sender);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, l, sender);
            }
        }
    }

    public void cancelAlarmManager() {
        Intent intent = new Intent(context, WidgetService.class);
        PendingIntent sender;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sender = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            sender = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(sender);
        }
    }
}
