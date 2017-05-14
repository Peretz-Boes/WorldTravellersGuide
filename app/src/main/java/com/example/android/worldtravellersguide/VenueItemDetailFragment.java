package com.example.android.worldtravellersguide;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.worldtravellersguide.model.FourSquareResults;
import com.example.android.worldtravellersguide.model.FoursquareLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueItemDetailFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {
    public static final String ARG_ITEM_ID = "item_id";
    private FourSquareResults mItem;
    private FoursquareLocation venueLocation;
    private GoogleMap map;

    public VenueItemDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem=(FourSquareResults)getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Your selected venue");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.venue_item_detail, container, false);
        bindMap();
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        LatLng venueLatLng=new LatLng(mItem.venue.location.lat,mItem.venue.location.lng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLatLng,16));
        Marker marker=map.addMarker(new MarkerOptions().position(venueLatLng).title(mItem.venue.name).snippet("View on Foursquare"));
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            map.setMyLocationEnabled(true);
        }
        marker.showInfoWindow();
        map.setOnInfoWindowClickListener(this);
    }

    private void bindMap(){
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        SupportMapFragment fragment=new SupportMapFragment();
        transaction.add(R.id.map_frame_layout,fragment);
        transaction.commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://foursquare.com/v/"+mItem.venue.id));
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException exception){
            Toast.makeText(getContext(),"You need to install a web browser to use this feature",Toast.LENGTH_LONG).show();
        }
    }
}
