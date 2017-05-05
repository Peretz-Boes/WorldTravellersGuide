package com.example.android.worldtravellersguide.network;

import com.example.android.worldtravellersguide.model.FoursquareRootJSON;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Peretz on 2017-05-04.
 */

public interface VenueSearchApiInterface {
    @GET("venues/search?v=20160504&limit=20")
    Call<FoursquareRootJSON>searchVenue(@Query("client_id") String clientID,@Query("client_secret") String clientSecret,@Query("query") String query,@Query("near") String near,@Query("ll")String ll,@Query("llAcc")double llAcc);
}
