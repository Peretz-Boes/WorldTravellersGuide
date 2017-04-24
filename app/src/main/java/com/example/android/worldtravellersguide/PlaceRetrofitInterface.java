package com.example.android.worldtravellersguide;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Peretz on 2017-04-19.
 */

public interface PlaceRetrofitInterface {

    @GET("venus/search")
    Call<PlaceListResponse> getPlacesInACity(@Query("near")String near);
    @GET("venus/search")
    Call<PlaceListResponse> getPlacesNearALatitudeAndLongitudeLocation(@Query("ll")double latitude,double longitude);

}
