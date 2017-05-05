/**
 * Filename: FoursquareLocation.java
 * Author: Matthew Huie
 *
 * FoursquareLocation describes a location object from the Foursquare API.
 */

package com.example.android.worldtravellersguide.model;

import java.io.Serializable;

public class FoursquareLocation implements Serializable {
    public String address;
    public  double lat;
    public double lng;
    public int distance;

}