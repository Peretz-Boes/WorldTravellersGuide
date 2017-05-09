/**
 * Filename: FoursquareVenue.java
 * Author: Matthew Huie
 *
 * FoursquareVenue describes a venue object from the Foursquare API.
 */

package com.example.android.worldtravellersguide.model;

import java.io.Serializable;

public class FoursquareVenue implements Serializable {
    public String id;
    public String name;
    public double rating;
    public FoursquareLocation location;
}