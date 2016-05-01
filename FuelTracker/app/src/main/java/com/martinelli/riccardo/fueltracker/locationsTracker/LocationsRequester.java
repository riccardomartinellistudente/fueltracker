package com.martinelli.riccardo.fueltracker.locationsTracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Riccardo on 01/05/2016.
 */
abstract class LocationsRequester implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Context context; //Serve per sapere da quale activity è stato lanciato il LocationsTracker

    private static int noPowerInterval = 0;
    private static int noPowerFastestInterval = 0;
    private static int noPowerPriority = LocationRequest.PRIORITY_NO_POWER;

    private int highAccuracyInterval = 1000;
    private int highAccuracyFastestInterval = 1000;
    private int highAccuracyPriority = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private boolean isHighAccuracyMode = false;

    public LocationsRequester(Context context){
        this.context = context;

        //Inizializza google api client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void start(){
        mGoogleApiClient.connect();
    }

    public void stop(){
        removeLocationUpdates(); // Serve veramente?
        mGoogleApiClient.disconnect();
    }

    public void setHigAccuracyMode(){
        if(!isHighAccuracyMode) {
            updateLocationRequest(highAccuracyInterval, highAccuracyFastestInterval, highAccuracyPriority);
            isHighAccuracyMode = true;
        }
    }

    private void requestLocationUpdates(LocationRequest mLocationRequest){
        if ( ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void removeLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void updateLocationRequest(int interval, int fastestInterval, int priority){
        removeLocationUpdates();
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priority);
        requestLocationUpdates(mLocationRequest);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(noPowerInterval);
        mLocationRequest.setFastestInterval(noPowerFastestInterval);
        mLocationRequest.setPriority(noPowerPriority);

        requestLocationUpdates(mLocationRequest);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location){
        if (isHighAccuracyMode){

            //Gestire high accuracy

        }
        useLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public abstract void useLocation(Location location);
}
