package com.example.android.worldtravellersguide.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.worldtravellersguide.database.DatabaseUtils;
import com.example.android.worldtravellersguide.database.VenueContract;
import com.example.android.worldtravellersguide.model.FourSquareResults;

import java.util.List;

/**
 * Created by Peretz on 2017-05-23.
 */

public class InsertWidgetDataAsyncTask extends AsyncTask<Void,Void,Void> {

    Context context;
    private List<FourSquareResults>fourSquareResultsList;

    public InsertWidgetDataAsyncTask(Context context, List<FourSquareResults> fourSquareResults) {
        this.context = context;
        this.fourSquareResultsList=fourSquareResults;
    }

    @Override
    protected Void doInBackground(Void... params) {
        context.getContentResolver().delete(VenueContract.VenueEntry.CONTENT_URI,null,null);
        for (FourSquareResults fourSquareResult:fourSquareResultsList) {
            ContentValues contentValues = DatabaseUtils.toContentValues(fourSquareResult);
            context.getContentResolver().insert(VenueContract.VenueEntry.CONTENT_URI, contentValues);
        }
        return null;
    }
}
