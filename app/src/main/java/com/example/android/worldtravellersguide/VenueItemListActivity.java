package com.example.android.worldtravellersguide;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.worldtravellersguide.model.FourSquareResults;
import com.example.android.worldtravellersguide.model.FoursquareRootJSON;
import com.example.android.worldtravellersguide.network.RetroApiClient;
import com.example.android.worldtravellersguide.network.VenueSearchApiInterface;
import com.example.android.worldtravellersguide.tasks.InsertWidgetDataAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION.SDK_INT;
import static android.util.Log.d;


public class VenueItemListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int VENUE_LOADER_ID=0;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    public static final String LOG_TAG = VenueItemListActivity.class.getSimpleName();
    private boolean mTwoPane;
    private GoogleApiClient googleApiClient;
    private boolean isConnected = false;
    private ProgressDialog progressDialog;
    private Timer searchDelayTimer;
    private EditText searchTextEditor;
    private EditText nearbyEditor;
    private SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;

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

        searchTextEditor = (EditText) findViewById(R.id.searchInputEditor);
        nearbyEditor = (EditText) findViewById(R.id.nearbyEditor);
        searchTextEditor.addTextChangedListener(new SearchTextWatcher());

        if (SDK_INT >= Build.VERSION_CODES.M) {
            handleRuntimePermission();
        }

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        Log.d(LOG_TAG,"onCreate executed");
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(new ArrayList<FourSquareResults>());
        recyclerView.setAdapter(simpleItemRecyclerViewAdapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnected = false;
    }

    private class SearchTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable editable) {
            final String inputText = editable.toString();
            searchDelayTimer = new Timer();
            searchDelayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    doSearchApiCallToFoursquare(inputText);
                }
            }, 500);
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
        private List<FourSquareResults> mValues;

        public SimpleItemRecyclerViewAdapter(List<FourSquareResults> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final FourSquareResults fourSquareResults = mValues.get(position);
            if (fourSquareResults.venue != null) {
                holder.itemNameView.setText(fourSquareResults.venue.name);
                holder.itemAddressView.setText(fourSquareResults.venue.location.address);
                holder.itemRatingView.setText(getString(R.string.rating_view_additional_text) + fourSquareResults.venue.rating);
            }

            if (fourSquareResults.photo != null) {
                String photoUrl=fourSquareResults.photo.getFormattedPhotoUrl();
                if (!photoUrl.isEmpty()) {
                    Picasso.with(VenueItemListActivity.this).load(photoUrl).into(holder.itemIconView);
                }
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(VenueItemDetailFragment.ARG_ITEM_ID, fourSquareResults);
                        VenueItemDetailFragment fragment = new VenueItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.venue_item_detail_container, fragment).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(VenueItemListActivity.this, VenueItemDetailActivity.class);
                        intent.putExtra(VenueItemDetailFragment.ARG_ITEM_ID, fourSquareResults);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageView transitionImageView = (ImageView) findViewById(R.id.itemIconView);
                            String iconViewTransitionName = getString(R.string.transition_tag) + String.valueOf(getItemId(holder.getAdapterPosition()));
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(VenueItemListActivity.this, transitionImageView, iconViewTransitionName);
                            ActivityCompat.startActivity(VenueItemListActivity.this, intent, options.toBundle());
                            d(LOG_TAG, "Transition complete");
                        } else {
                            context.startActivity(intent);
                        }
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

        private void refreshItems(List<FourSquareResults> items) {
            this.mValues = items;
            notifyDataSetChanged();
            InsertWidgetDataAsyncTask insertWidgetDataAsyncTask = new InsertWidgetDataAsyncTask(getApplicationContext(), items);
            insertWidgetDataAsyncTask.execute();
            Log.d(LOG_TAG, "Result data inserted into database");
        }

    }

    private void handleRuntimePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    private void cancelTimer() {
        if (searchDelayTimer != null) {
            searchDelayTimer.cancel();
            searchDelayTimer = null;
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.progress_dialog_searching_text));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showMessageAlertWithOkButton(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setGravity(Gravity.CENTER);
        }
    }

    private void doSearchApiCallToFoursquare(String searchText) {
        VenueSearchApiInterface apiService = RetroApiClient.getClient().create(VenueSearchApiInterface.class);
        Call<FoursquareRootJSON> apiInvokeCall = null;
        String cityInputText = nearbyEditor.getText().toString();
        if (!cityInputText.isEmpty()) {
            apiInvokeCall = apiService.searchVenueNearCity(getString(R.string.foursquare_client_id), getString(R.string.foursquare_client_secret), searchText, cityInputText);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                String ll = "" + lastLocation.getLatitude() + "," + lastLocation.getLongitude();
                apiInvokeCall = apiService.searchVenueNearMe(getString(R.string.foursquare_client_id), getString(R.string.foursquare_client_secret), searchText, ll, 1000);
            }
        }

        if (apiInvokeCall == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.location_unavailable_error_message, Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                showProgressDialog();
            }
        });
        apiInvokeCall.enqueue(new Callback<FoursquareRootJSON>() {
            @Override
            public void onResponse(Call<FoursquareRootJSON> call, Response<FoursquareRootJSON> response) {
                hideProgressDialog();
                FoursquareRootJSON foursquareRootJSON = response.body();
                if (foursquareRootJSON != null && foursquareRootJSON.response != null) {
                    simpleItemRecyclerViewAdapter.refreshItems(foursquareRootJSON.response.group.results);
                }
            }

            @Override
            public void onFailure(Call<FoursquareRootJSON> call, Throwable t) {
                hideProgressDialog();
                showMessageAlertWithOkButton(getString(R.string.error_message_title), getString(R.string.error_in_search_error_message));
            }
        });
    }

}
