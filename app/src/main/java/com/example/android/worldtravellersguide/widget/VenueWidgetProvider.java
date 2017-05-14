package com.example.android.worldtravellersguide.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.worldtravellersguide.R;
import com.example.android.worldtravellersguide.VenueItemDetailActivity;
import com.example.android.worldtravellersguide.VenueItemListActivity;
import com.example.android.worldtravellersguide.model.FoursquareRootJSON;
import com.example.android.worldtravellersguide.network.RetroApiClient;
import com.example.android.worldtravellersguide.network.VenueSearchApiInterface;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Random;

import retrofit2.Call;

/**
 * Created by Peretz on 2016-12-29.
 */
public class VenueWidgetProvider extends AppWidgetProvider {

    private GoogleApiClient googleApiClient;
    private Context mContext=null;

    public VenueWidgetProvider(Context context){
        mContext=context;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.venue_collection_widget);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, VenueItemListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);

            // Set up the collection
            setRemoteAdapter(context, views);

            // Set up collection items
            Intent clickIntentTemplate = new Intent(context, VenueItemDetailActivity.class);

            PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(context, 0,
                    clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Called on broadcast created on item touch from the collection widget.
     */
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        String randomQueries[]={"hotels","auto garages","hospitals","medical offices","restaurants"};
        Random random=new Random();
        doSearchApiCallToFoursquareWithRandomQuery(randomQueries[random.nextInt()]);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context,VenueWidgetIntentService.class));
    }

    private void doSearchApiCallToFoursquareWithRandomQuery(String query){
        VenueSearchApiInterface apiService=RetroApiClient.getClient().create(VenueSearchApiInterface.class);
        Call<FoursquareRootJSON>apiInvokeCall=null;
        try {
            Location lastLocation=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation!=null){
                String ll=lastLocation.getLatitude()+","+lastLocation.getLongitude();
                apiInvokeCall=apiService.searchRandomVenueNearMe(String.valueOf(R.string.foursquare_client_id),String.valueOf(R.string.foursquare_client_secret),query,ll,1000);
                if (apiInvokeCall==null){
                    Toast.makeText(mContext,"Unable to access location",Toast.LENGTH_LONG).show();
                }
            }
        }catch (SecurityException e){
            Toast.makeText(mContext,"You need to enable location for this feature to work",Toast.LENGTH_LONG).show();
        }
    }

}
