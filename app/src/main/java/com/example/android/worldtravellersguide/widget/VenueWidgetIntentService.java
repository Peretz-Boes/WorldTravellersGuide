package com.example.android.worldtravellersguide.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Peretz on 2016-12-29.
 */
public class VenueWidgetIntentService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new VenueWidgetDataProvider(this);
    }

}
