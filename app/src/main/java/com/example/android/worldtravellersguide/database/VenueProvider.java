package com.example.android.worldtravellersguide.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        int match=uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase=venueDatabaseHelper.getWritableDatabase();
        if (selection==null){
            selection="1";
        }
        switch (match){
            case VENUE:
                rowsDeleted=sqLiteDatabase.delete(VenueContract.VenueEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }

        if (rowsDeleted!=0){
            if (getContext()!=null&&getContext().getContentResolver()!=null){
                getContext().getContentResolver().notifyChange(uri,null);
            }
        }
        sqLiteDatabase.close();
        return rowsDeleted;
    }

    @Override
    public boolean onCreate() {
        venueDatabaseHelper=new VenueDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match=uriMatcher.match(uri);
        switch (match){
            case VENUE:
                retCursor=venueDatabaseHelper.getReadableDatabase().query(VenueContract.VenueEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case VENUE_WITH_ID:
                String venueIdSelection=VenueContract.VenueEntry.TABLE_NAME+"."+VenueContract.VenueEntry._ID+"=?";
                String[]venueIdSelectionArgs=new String[]{VenueContract.VenueEntry.getVenueIdFromUri(uri)};
                retCursor=venueDatabaseHelper.getReadableDatabase().query(VenueContract.VenueEntry.TABLE_NAME,projection,venueIdSelection,venueIdSelectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        if (getContext()!=null&&getContext().getContentResolver()!=null){
            retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match=uriMatcher.match(uri);
        String type;
        switch (match){
            case VENUE:
                type=VenueContract.VenueEntry.CONTENT_TYPE;
                break;
            case VENUE_WITH_ID:
                type=VenueContract.VenueEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri returnUri;
        int match=uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase=venueDatabaseHelper.getWritableDatabase();
        switch (match){
            case VENUE:
                long venueId=sqLiteDatabase.insert(VenueContract.VenueEntry.TABLE_NAME,null,contentValues);
                if (venueId!=-1){
                    returnUri=VenueContract.VenueEntry.buildVenueUri(venueId);
                }else {
                    returnUri=null;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri "+uri);
        }
        sqLiteDatabase.close();
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        int match=uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase=venueDatabaseHelper.getWritableDatabase();
        switch (match){
            case VENUE:
                rowsUpdated=sqLiteDatabase.update(VenueContract.VenueEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }
        if (rowsUpdated!=0){
            if (getContext()!=null&&getContext().getContentResolver()!=null){
                getContext().getContentResolver().notifyChange(uri,null);
            }
        }
        sqLiteDatabase.close();
        return rowsUpdated;
    }
}
