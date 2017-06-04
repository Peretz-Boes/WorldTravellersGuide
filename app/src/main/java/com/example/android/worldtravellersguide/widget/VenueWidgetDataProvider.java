package com.example.android.worldtravellersguide.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.worldtravellersguide.R;
import com.example.android.worldtravellersguide.VenueItemDetailFragment;
import com.example.android.worldtravellersguide.database.VenueContract;
import com.example.android.worldtravellersguide.model.FourSquareResults;
import com.example.android.worldtravellersguide.model.FoursquareLocation;
import com.example.android.worldtravellersguide.model.FoursquareVenue;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Peretz on 2017-04-05.
 */

class VenueWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG=VenueWidgetDataProvider.class.getSimpleName();
    private Context mContext = null;
    private Cursor cursor = null;
    private int appWidgetId;

    VenueWidgetDataProvider(Context context,int widgetId){
        mContext=context;
        appWidgetId=widgetId;
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public void onDataSetChanged() {
        Log.d(LOG_TAG,"onDataSetChanged=");
        if (cursor!=null){
            cursor.close();
        }
        final long identityToken= Binder.clearCallingIdentity();
        cursor=mContext.getContentResolver().query(VenueContract.VenueEntry.CONTENT_URI,VenueContract.VenueEntry.VENUE_COLUMNS,null,null,VenueContract.VenueEntry.NAME_COLUMN);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        int count=(cursor==null)?0:cursor.getCount();
        Log.d(LOG_TAG,"Count="+count);
        return count;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(i)) {
            return null;
        }

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.venue_item_list_content);

        if (this.cursor.moveToPosition(i)) {
            String name = cursor.getString(VenueContract.VenueEntry.POSITION_NAME);
            String imageUrl = cursor.getString(VenueContract.VenueEntry.POSITION_IMAGE);
            double rating = cursor.getDouble(VenueContract.VenueEntry.POSITION_RATING);
            double latitude = cursor.getDouble(VenueContract.VenueEntry.POSITION_LATITUDE);
            double longitude = cursor.getDouble(VenueContract.VenueEntry.POSITION_LONGITUDE);
            String address = cursor.getString(VenueContract.VenueEntry.POSITION_ADDRESS);
            view.setTextViewText(R.id.itemNameView, String.valueOf(name));
            view.setTextViewText(R.id.itemIconView, String.valueOf(address));
            view.setTextViewText(R.id.itemRatingView, String.valueOf(rating));
            if (!TextUtils.isEmpty(imageUrl)) {
                try {
                    Bitmap bitmap = Picasso.with(mContext).load(imageUrl).get();
                    view.setImageViewBitmap(R.id.itemIconView, bitmap);
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
            view.setOnClickFillInIntent(R.id.venue_list_item, fillInIntent);
        }
        return view;
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
        if (cursor.moveToPosition(i)) {
            return cursor.getInt(VenueContract.VenueEntry.POSITION_ID);
        }
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
