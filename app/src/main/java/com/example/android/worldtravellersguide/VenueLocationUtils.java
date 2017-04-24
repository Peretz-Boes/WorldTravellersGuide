package com.example.android.worldtravellersguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Peretz on 2017-04-23.
 */

public class VenueLocationUtils {

    private static double[] getVenueLocationCoordinates(Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        double[] venueCoordinates=new double[2];
        return null;//to prevent an error
    }

}
