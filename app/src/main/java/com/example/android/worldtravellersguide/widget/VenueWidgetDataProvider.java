package com.example.android.worldtravellersguide.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.worldtravellersguide.R;
import com.example.android.worldtravellersguide.VenueItemDetailFragment;
import com.example.android.worldtravellersguide.database.VenueContract;
import com.example.android.worldtravellersguide.model.FourSquareResults;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Peretz on 2017-04-05.
 */

class VenueWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext = null;
    private Cursor cursor = null;
    private GoogleApiClient googleApiClient;
    private FourSquareResults mItem;

    VenueWidgetDataProvider(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public void onDataSetChanged() {
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
        return (cursor == null) ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(i)) {
            return null;
        }

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.venue_item_list_content);

        String name=null;
        if (this.cursor.moveToPosition(i)){
            name=cursor.getString(VenueContract.VenueEntry.POSITION_NAME);
            String image=cursor.getString(VenueContract.VenueEntry.POSITION_IMAGE);
            double rating=cursor.getDouble(VenueContract.VenueEntry.POSITION_RATING);
            view.setTextViewText(R.id.itemNameView,name);
            view.setImageViewUri(R.id.itemIconView, Uri.parse(image));
            view.setTextViewText(R.id.itemRatingView,String.valueOf(rating));
        }

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(VenueItemDetailFragment.ARG_ITEM_ID,name);
        view.setOnClickFillInIntent(R.id.venue_list_item,fillInIntent);
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
