package com.example.android.worldtravellersguide.database;

import android.content.ContentValues;

import com.example.android.worldtravellersguide.database.VenueContract.VenueEntry;
import com.example.android.worldtravellersguide.model.FourSquareResults;

/**
 * Created by Peretz on 2017-05-23.
 */

public class DatabaseUtils {

    public static ContentValues toContentValues(FourSquareResults fourSquareResults) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VenueContract.VenueEntry._ID, fourSquareResults.venue.id);
        contentValues.put(VenueContract.VenueEntry.NAME_COLUMN, fourSquareResults.venue.name);
        if (fourSquareResults.photo != null) {
            contentValues.put(VenueContract.VenueEntry.IMAGE_COLUMN, fourSquareResults.photo.getFormattedPhotoUrl());
        }
        contentValues.put(VenueContract.VenueEntry.RATING_COLUMN, fourSquareResults.venue.rating);
        contentValues.put(VenueEntry.LAT_COLUMN, fourSquareResults.venue.location.lat);
        contentValues.put(VenueEntry.LONG_COLUMN, fourSquareResults.venue.location.lng);
        contentValues.put(VenueEntry.ADDRESS_COLUMN, fourSquareResults.venue.location.address);
        return contentValues;
    }

}
