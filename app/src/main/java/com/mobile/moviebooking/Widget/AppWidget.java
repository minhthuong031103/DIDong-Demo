package com.mobile.moviebooking.Widget;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.RemoteViews;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.rocketreserver.GetPosterWidgetQuery;
import com.example.rocketreserver.MovieDetailsQuery;
import com.example.rocketreserver.MovieHomaePageQuery;
import com.mobile.moviebooking.Activity.HomePage;
import com.mobile.moviebooking.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    private static final int NOTIFICATION_ID = 1;
    private static AppWidgetTarget appWidgetTarget;
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, AppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent intent = new Intent(context, HomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Add intent to a view (like a button)
        views.setOnClickPendingIntent(R.id.image, pendingIntent);
        appWidgetTarget = new AppWidgetTarget(context, R.id.image, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        SharedPreferences preferences = context.getSharedPreferences("PREFS", 0);
        int posterIndex = preferences.getInt("value", 0);

        String Token = "Bearer " +
                "893a13fa53a2ff80efe5b37c1fd5942434971882b53655b0542e4ccdb7ab76bbd28fbbac96939f04f01bdb1c098492f91d908e8dc38b3092f348bf2d190ffa91354f451a38afadd4063f6fcbb256e84a7b9ad7e7c8775be390ba32a68d2c393bca77d6a2031dfd3358a9760dad48ca115b7086103cd355c140aa99451fd510c0";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();

        apolloClient.query(new GetPosterWidgetQuery())
                .enqueue(response -> {
                    List<String> moviePosters = response.data.movies.data
                            .stream()
                            .map(movie -> movie.attributes.poster.data.attributes.url).collect(Collectors.toList());

                    preferences.edit().putInt("max", moviePosters.size()).apply();

                    Glide
                            .with(context.getApplicationContext())
                            .asBitmap()
                            .load(moviePosters.get(posterIndex))
                            .into(appWidgetTarget);

                    appWidgetManager.updateAppWidget(appWidgetId, views);
                    AlarmHandler alarmHandler = new AlarmHandler(context);
                    alarmHandler.cancelAlarmManager();
                    alarmHandler.setAlarmManager();
                });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.setAlarmManager();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();
    }
}