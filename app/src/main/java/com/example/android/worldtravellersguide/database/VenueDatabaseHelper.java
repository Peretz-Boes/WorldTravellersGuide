package com.example.android.worldtravellersguide.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Peretz on 2017-05-23.
 */

public class VenueDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="random_venue_results.db";

    public VenueDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_VENUE_TABLE="CREATE TABLE "+ VenueContract.VenueEntry.TABLE_NAME+" ("+VenueContract.VenueEntry.NAME_COLUMN+" TEXT NOT NULL,"+VenueContract.VenueEntry.IMAGE_COLUMN+" TEXT NOT NULL,"+VenueContract.VenueEntry.RATING_COLUMN+" REAL TEXT NOT NULL"+")";
        sqLiteDatabase.execSQL(CREATE_VENUE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+VenueContract.VenueEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
