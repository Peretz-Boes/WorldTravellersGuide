package com.example.android.worldtravellersguide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editText=(EditText)findViewById(R.id.city_edit_text);
    ListView listView=(ListView)findViewById(R.id.place_list_view);
    Button searchByCityNameButton=(Button)findViewById(R.id.search_by_city_button);
    Button searchByLatitudeAndLongitudeLocationButton=(Button)findViewById(R.id.search_by_latitude_and_longitude_button);
    Button showVenueLocationInMapButton=(Button)findViewById(R.id.show_venue_in_map_button);
    PlaceRetrofitInterface placeRetrofitInterface;
    PlaceAdapter placeAdapter;
    private List<Place> places;
    LocationManager locationManager;
    LocationListenerClass locationListenerClass=new LocationListenerClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        AdView adView=(AdView)findViewById(R.id.banner_ad_view);
        AdRequest adRequest=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
        placeRetrofitInterface=PlaceClient.createService(PlaceRetrofitInterface.class);
        places=new ArrayList<>();
        placeAdapter=new PlaceAdapter(getApplicationContext(),places);
        listView.setAdapter(placeAdapter);
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(getApplicationContext(), R.string.location_permission_explanation_message,Toast.LENGTH_LONG).show();

        searchByCityNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetServiceAvailable()){
                    getPlaceData();
                }else {
                    Toast.makeText(getApplicationContext(),R.string.internet_connection_message,Toast.LENGTH_LONG).show();
                }
            }
        });

        searchByLatitudeAndLongitudeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetServiceAvailable()){
                    final int REQUEST_CODE_ACCESS_PERMISSIONS=123;
                    int hasAccessFineLocationPermission= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if(hasAccessFineLocationPermission!= PackageManager.PERMISSION_GRANTED){
                        if(!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ACCESS_PERMISSIONS);
                            if (hasAccessFineLocationPermission==PackageManager.PERMISSION_DENIED){
                                Toast.makeText(getApplicationContext(),R.string.location_permission_explanation_message,Toast.LENGTH_LONG).show();
                                return;
                            }
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListenerClass);
                        }
                    }else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListenerClass);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),R.string.internet_connection_message,Toast.LENGTH_LONG).show();
                }
            }
        });

        showVenueLocationInMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isInternetServiceAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){
            Toast.makeText(getApplicationContext(), R.string.internet_connection_message,Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    public void getPlaceData(){
        Call<PlaceListResponse> placeListResponseCall=placeRetrofitInterface.getPlacesInACity(String.valueOf(editText.getText()));
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
                    Toast.makeText(getApplicationContext(),"Unable to get places list",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(),R.string.internet_connection_message,Toast.LENGTH_LONG).show();
            }
        });
    }

}
