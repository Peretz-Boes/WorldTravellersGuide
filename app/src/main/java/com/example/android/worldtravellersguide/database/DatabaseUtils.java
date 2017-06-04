package com.example.android.worldtravellersguide.database;

import android.content.ContentValues;
import android.util.Log;

import com.example.android.worldtravellersguide.model.FourSquareResults;

/**
 * Created by Peretz on 2017-05-23.
 */

public class DatabaseUtils {

    public static final String LOG_TAG=DatabaseUtils.class.getSimpleName();

    public static ContentValues toContentValues(FourSquareResults fourSquareResults){
        ContentValues contentValues=new ContentValues();
        contentValues.put(VenueContract.VenueEntry._ID,fourSquareResults.venue.id);
        contentValues.put(VenueContract.VenueEntry.NAME_COLUMN,fourSquareResults.venue.name);
        if (fourSquareResults.photo!=null){
            String photoUrl=fourSquareResults.photo.getFormattedPhotoUrl();
            contentValues.put(VenueContract.VenueEntry.IMAGE_COLUMN,photoUrl);
            Log.d(LOG_TAG,"Photo url "+photoUrl);
            Log.d(LOG_TAG,"Image inserted");
        }
        contentValues.put(VenueContract.VenueEntry.RATING_COLUMN,fourSquareResults.venue.rating);
        contentValues.put(VenueContract.VenueEntry.LATITUDE_COLUMN,fourSquareResults.venue.location.lat);
        contentValues.put(VenueContract.VenueEntry.LONGITUDE_COLUMN,fourSquareResults.venue.location.lng);
        if (fourSquareResults.venue.location.address!=null) {
            contentValues.put(VenueContract.VenueEntry.ADDRESS_COLUMN, fourSquareResults.venue.location.address);
        }
        return contentValues;
    }

}
