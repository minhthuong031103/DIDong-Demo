package com.mobile.moviebooking.Widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class WidgetService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //wake the device
        WakeLocker.acquire(context);

        //increase the number in the widget
        SharedPreferences preferences = context.getSharedPreferences("PREFS", 0);
        int value = preferences.getInt("value", 0);
        value++;
        if (value >= preferences.getInt("max", 0)) {
            value = 0;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("value", value);
        editor.apply();

        //force widget update
        Intent widgetIntent = new Intent(context, AppWidget.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AppWidget.class));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(widgetIntent);

        Log.d("WIDGET", "Widget set to update!");

        //go back to sleep
        WakeLocker.release();
    }
}
