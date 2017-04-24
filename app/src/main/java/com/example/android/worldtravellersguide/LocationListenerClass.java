package com.example.android.worldtravellersguide;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Peretz on 2017-04-21.
 */

public class LocationListenerClass implements LocationListener {

    PlaceRetrofitInterface placeRetrofitInterface;
    PlaceAdapter placeAdapter;
    Context context;

    @Override
    public void onLocationChanged(Location location) {

        if (location!=null){
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            getPlaceDataByLocation(latitude,longitude);
        }else {
            Toast.makeText(context.getApplicationContext(), R.string.location_error_message,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void getPlaceDataByLocation(double latitudeLocation,double longitudeLocation){
        Call<PlaceListResponse> placeListResponseCall=placeRetrofitInterface.getPlacesNearALatitudeAndLongitudeLocation(latitudeLocation,longitudeLocation);
        placeListResponseCall.enqueue(new Callback<PlaceListResponse>() {
            @Override
            public void onResponse(Response<PlaceListResponse> response) {
                if(response!=null&&response.body()!=null){
                    List<Place> placeList=response.body().getResults();
                    placeAdapter.clear();
                    for(Place place:placeList){
                        placeAdapter.add(place);
                    }
                }else {
                    Toast.makeText(context.getApplicationContext(),"Unable to get places list",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context.getApplicationContext(),R.string.internet_connection_message,Toast.LENGTH_LONG).show();
            }
        });
    }

}
