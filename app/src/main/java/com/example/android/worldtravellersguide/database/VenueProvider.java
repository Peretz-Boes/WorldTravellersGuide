package com.example.android.worldtravellersguide.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Peretz on 2017-05-23.
 */

public class VenueProvider extends ContentProvider {

    public static final UriMatcher uriMatcher=buildUriMatcher();
    private VenueDatabaseHelper venueDatabaseHelper;
    static final int VENUE=100;
    static final int VENUE_WITH_ID=101;

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher1=new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuthority=VenueContract.CONTENT_AUTHORITY;
        uriMatcher1.addURI(contentAuthority,VenueContract.VenueEntry.TABLE_NAME,VENUE);
        uriMatcher1.addURI(contentAuthority,VenueContract.VenueEntry.TABLE_NAME+"/#",VENUE_WITH_ID);
        return uriMatcher1;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public boolean onCreate() {
        venueDatabaseHelper=new VenueDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
