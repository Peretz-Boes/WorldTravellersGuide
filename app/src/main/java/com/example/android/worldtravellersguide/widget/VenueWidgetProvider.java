package com.example.android.worldtravellersguide.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.android.worldtravellersguide.R;
import com.example.android.worldtravellersguide.VenueItemDetailActivity;
import com.example.android.worldtravellersguide.VenueItemListActivity;

/**
 * Created by Peretz on 2016-12-29.
 */
public class VenueWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.venue_collection_widget);

        // Create an Intent to launch MainActivity
        Intent intent = new Intent(context, VenueItemListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteView.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);

        // Set up the collection
        setRemoteAdapter(context, remoteView, appWidgetId);

        // Set up collection items
        Intent clickIntentTemplate = new Intent(context, VenueItemDetailActivity.class);
        PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(context, 0, clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

        return remoteView;
    }

    private  void setRemoteAdapter(Context context, @NonNull final RemoteViews remoteViews, int appWidgetId) {
        Intent intent = new Intent(context, VenueWidgetIntentService.class);
        intent.putExtra(VenueWidgetIntentService.KEY_WIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(R.id.widget_list, intent);
       // remoteViews.setEmptyView(R.id.widget_list, R.id.empty_view);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }


}
