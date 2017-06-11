package com.example.android.worldtravellersguide.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.worldtravellersguide.R;
import com.example.android.worldtravellersguide.VenueItemDetailFragment;
import com.example.android.worldtravellersguide.database.VenueContract.VenueEntry;
import com.example.android.worldtravellersguide.model.FourSquareResults;
import com.example.android.worldtravellersguide.model.FoursquareLocation;
import com.example.android.worldtravellersguide.model.FoursquareVenue;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peretz on 2017-04-05.
 */

class VenueWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    public static final String LOG_TAG=VenueWidgetDataProvider.class.getSimpleName();
    public static final int CURSOR_LOADER_ID=1;
    private Context mContext = null;
    private Cursor cursor = null;
    private int appWidgetId;
    List<Venue> venues=new ArrayList<>();

    VenueWidgetDataProvider(Context context, int widgetId) {
        mContext = context;
        appWidgetId = widgetId;
    }

    @Override
    public void onCreate() {
        Loader<Cursor> loader=new CursorLoader(mContext,VenueEntry.CONTENT_URI, VenueEntry.VENUE_COLUMNS,null,null,VenueEntry.NAME_COLUMN);
        loader.startLoading();
        loader.registerListener(CURSOR_LOADER_ID, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                if (data.moveToFirst()){
                    while (data.moveToNext()){
                        Venue venue=new Venue();
                        venue.setName(data.getString(data.getColumnIndex("name")));
                        venue.setAddress(data.getString(data.getColumnIndex("address")));
                        venue.setImageUrl(data.getString(data.getColumnIndex("image")));
                        venue.setRating(data.getDouble(data.getColumnIndex("rating")));
                        venue.setLatitude(data.getDouble(data.getColumnIndex("latitude")));
                        venue.setLongitude(data.getDouble(data.getColumnIndex("longitude")));
                        venues.add(venue);
                    }
                }
                onDataSetChanged();
            }
        });
    }

    @Override
    public void onDataSetChanged() {
        Log.d(LOG_TAG,"onDataSetChanged: ");
        for (Venue venue:venues){
            Log.d(LOG_TAG,"onDataSetChanged: venue: "+venue.toString());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        int count=venues.size();
        Log.d(LOG_TAG,"getCount: count: "+count);
        return count;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.venue_item_list_content);
        Venue venue = venues.get(i);
        String name = venue.getName();
        String imageUrl = venue.getImageUrl();
        double rating = venue.getRating();
        double latitude = venue.getLatitude();
        double longitude = venue.getLongitude();
        String address = venue.getAddress();

        remoteViews.setTextViewText(R.id.itemNameView, name);
        remoteViews.setTextViewText(R.id.itemAddressView, address);
        remoteViews.setTextViewText(R.id.itemRatingView, String.valueOf(rating));

        if (!TextUtils.isEmpty(imageUrl)) {
            try {
                Bitmap b = Picasso.with(mContext).load(imageUrl).get();
                remoteViews.setImageViewBitmap(R.id.itemIconView, b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FoursquareLocation location = new FoursquareLocation();
        location.lat = latitude;
        location.lng = longitude;
        location.address = address;

        FoursquareVenue foursquareVenue = new FoursquareVenue();
        foursquareVenue.name = name;
        foursquareVenue.rating = rating;
        foursquareVenue.location = location;

        FourSquareResults fourSquareResult = new FourSquareResults();
        fourSquareResult.venue = foursquareVenue;

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(VenueItemDetailFragment.ARG_ITEM_ID, fourSquareResult);
        remoteViews.setOnClickFillInIntent(R.id.venue_list_item, fillInIntent);


        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.venue_item_list_content);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class Venue{
        String name;
        String address;
        double rating;
        double latitude;
        double longitude;
        String imageUrl;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

}
