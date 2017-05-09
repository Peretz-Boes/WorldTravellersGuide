package com.example.android.worldtravellersguide.network;

import com.example.android.worldtravellersguide.model.FoursquareRootJSON;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Peretz on 2017-05-04.
 */

public interface VenueSearchApiInterface {
    @GET("venus/recommendations?v=20160504")
    Call<FoursquareRootJSON>searchVenueNearCity(@Query("client_id") String clientID, @Query("client_secret") String clientSecret, @Query("query") String query, @Query("near") String near);
    @GET("venus/recommendations?v=20160504")
    Call<FoursquareRootJSON>searchVenueNearMe(@Query("client_id") String clientID, @Query("client_secret") String clientSecret, @Query("query") String query, @Query("ll") String ll, @Query("llAcc") double llAcc);
}
