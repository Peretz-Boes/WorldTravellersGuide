package com.example.android.worldtravellersguide.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Peretz on 2016-12-29.
 */
public class VenueWidgetIntentService extends RemoteViewsService {
    public static final String KEY_WIDGET_ID = "KEY_WIDGET_ID";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(KEY_WIDGET_ID, 0);
        return new VenueWidgetDataProvider(this, appWidgetId);
    }

}
