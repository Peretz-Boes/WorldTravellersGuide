package com.example.android.worldtravellersguide;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.worldtravellersguide.model.FoursquareVenue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Build.VERSION.SDK_INT;


public class VenueItemListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private static final int PERMISSION_ACCESS_FINE_LOCATION=1;
    private boolean mTwoPane;
    private GoogleApiClient googleApiClient;
    private boolean isConnected=false;
    private ProgressDialog progressDialog;
    private Timer searchDelayTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_item_list);

        View recyclerView = findViewById(R.id.venue_item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.venue_item_detail_container) != null) {
            mTwoPane = true;
        }

        if(SDK_INT>=Build.VERSION_CODES.M){
            handleRuntimePermission();
        }

        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(new ArrayList<FoursquareVenue>()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected=true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnected=false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnected=false;
    }

    private class SearchTextWatcher implements TextWatcher{
        @Override
        public void afterTextChanged(Editable editable) {
            final String inputText=editable.toString();
            searchDelayTimer=new Timer();
            searchDelayTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            },500);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            cancelTimer();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<FoursquareVenue> mValues;

        public SimpleItemRecyclerViewAdapter(List<FoursquareVenue> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        // arguments.putString(VenueItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        VenueItemDetailFragment fragment = new VenueItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.venue_item_detail_container, fragment).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, VenueItemDetailActivity.class);
                        // intent.putExtra(VenueItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView itemIconView;
            public final TextView itemNameView;
            public final TextView itemAddressView;
            public final TextView itemRatingView;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                itemIconView = (ImageView) view.findViewById(R.id.itemIconView);
                itemNameView = (TextView) view.findViewById(R.id.itemNameView);
                itemAddressView = (TextView) view.findViewById(R.id.itemAddressView);
                itemRatingView = (TextView) view.findViewById(R.id.itemRatingView);
            }


        }
    }

    private void handleRuntimePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    private void cancelTimer(){
        if(searchDelayTimer!=null){
            searchDelayTimer.cancel();
            searchDelayTimer=null;
        }
    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Searching");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
        }
    }

    private void hideProgressDialog(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    private void showMessageAlertWithOkButton(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.show();
        TextView messageView=(TextView)dialog.findViewById(android.R.id.message);
        if(messageView!=null){
            messageView.setGravity(Gravity.CENTER);
        }
    }

}
