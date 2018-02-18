package com.tpoint.io.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tpoint.data.TPoint;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Location API related
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder mLocationRequestSettings;
    private PendingResult<LocationSettingsResult> mResult;
    // latest user's location
    private TPoint mCurrentPoint;
    // Location Manager Callback
    private LocationManagerCallback mCallback;
    // location manager availability flag
    private boolean enabled;


    /*
    Constructor
     */
    public LocationManager(LocationManagerCallback callback) {
        this.mCallback = callback;
    }


    /*
    Public method to init Google Location API connection
     */
    public void connect(Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.reconnect();
        }
    }


    /*
    Release memory refs
     */
    public void destroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        mLocationRequest = null;
        mLocationRequestSettings = null;
        mResult = null;
        mCallback = null;
        mCurrentPoint = null;
    }


    /*
    Location API Callback Methods
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // create the location request
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // updating location every half second.
        mLocationRequest.setInterval(200);
        mLocationRequestSettings = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        mResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationRequestSettings.build());
        mResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationManager.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // UserLocation settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        if (mCallback != null) {
                            mCallback.onLocationResolution(status);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // UserLocation settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // init current point
            if (mCurrentPoint == null) {
                mCurrentPoint = new TPoint();
                mCurrentPoint.setFirebaseID("current");
                mCurrentPoint.setArchivedDate(0);
                mCurrentPoint.setDistance(0);
            }
            // set location and creation date for current location
            mCurrentPoint.setLat(location.getLatitude());
            mCurrentPoint.setLon(location.getLongitude());
            mCurrentPoint.setCreationDate();
            // update availability flag
            if (!enabled) {
                enabled = true;
                // inform user location manager is enabled
                if (mCallback != null) {
                    mCallback.onLocationTrackingStarted(true);
                }
            }
        }
    }


    /*
    Getter Methods
     */
    public TPoint getCurrentPoint() {
        return mCurrentPoint;
    }
}
