/**
 * Filename: FoursquareResponse.java
 * Author: Matthew Huie
 *
 * FoursquareResponse describes a response object from the Foursquare API.
 */

package com.example.android.worldtravellersguide.model;

import java.util.ArrayList;
import java.util.List;

public class FoursquareResponse {
    public List<FoursquareVenue> venues = new ArrayList<>();
}