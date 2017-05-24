package com.example.android.worldtravellersguide.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Peretz on 2017-05-23.
 */

public class VenueContract {
    public static final String CONTENT_AUTHORITY="com.example.android.worldtravellersguide.database.VenueProvider";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class VenueEntry implements BaseColumns{
        public static final String TABLE_NAME="random_venue_results";
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static Uri buildVenueUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static final String CONTENT_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_NAME;
        public static final String NAME_COLUMN="name";
        public static final String IMAGE_COLUMN="image";
        public static final String RATING_COLUMN="rating";

        public static String getVenueIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

}
