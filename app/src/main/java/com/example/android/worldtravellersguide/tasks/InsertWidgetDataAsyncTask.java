package com.example.android.worldtravellersguide.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.worldtravellersguide.database.DatabaseUtils;
import com.example.android.worldtravellersguide.database.VenueContract;
import com.example.android.worldtravellersguide.model.FourSquareResults;

/**
 * Created by Peretz on 2017-05-23.
 */

public class InsertWidgetDataAsyncTask extends AsyncTask<Void,Void,Void> {

    Context context;
    private FourSquareResults fourSquareResults;

    public InsertWidgetDataAsyncTask(Context context, FourSquareResults fourSquareResults) {
        this.context = context;
        this.fourSquareResults = fourSquareResults;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentValues contentValues= DatabaseUtils.toContentValues(fourSquareResults);
        context.getContentResolver().insert(VenueContract.VenueEntry.CONTENT_URI,contentValues);
        return null;
    }
}
