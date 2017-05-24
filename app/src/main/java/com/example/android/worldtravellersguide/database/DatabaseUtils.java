package com.example.android.worldtravellersguide.database;

import android.content.ContentValues;

import com.example.android.worldtravellersguide.model.FourSquareResults;

/**
 * Created by Peretz on 2017-05-23.
 */

public class DatabaseUtils {

    public static ContentValues toContentValues(FourSquareResults fourSquareResults){
        ContentValues contentValues=new ContentValues();
        contentValues.put(VenueContract.VenueEntry.NAME_COLUMN,fourSquareResults.venue.name);
        contentValues.put(VenueContract.VenueEntry.IMAGE_COLUMN,fourSquareResults.photo.getFormattedPhotoUrl());
        contentValues.put(VenueContract.VenueEntry.RATING_COLUMN,fourSquareResults.venue.rating);
        return contentValues;
    }

}
