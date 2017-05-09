package com.example.android.worldtravellersguide.model;

import java.io.Serializable;

/**
 * Created by nadirhussain on 07/05/2017.
 */

public class FourSquareVenuePhoto implements Serializable{
    public String prefix;
    public String suffix;

    public String getFormattedPhotoUrl() {
        return prefix + "100x100" + suffix;
    }
}
