package com.martinelli.riccardo.fueltracker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Riccardo on 07/04/2016.
 */
public class DistanceTracker implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Context _contesto; //Serve per sapere da quale activity è stato lanciato il DistanceTracker

    private LocationsList mll = new LocationsList(); //Lista per lo store di tutte le locations.

    private boolean isStarted = false;

    private int indexLastLocation = 0;

    public DistanceTracker(Context contesto, int interval, int fastestInterval, int priority) {

        _contesto = contesto;

        //Inizializza google api client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(_contesto)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Inizializa una nuova LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priority);
    }

    public void start(){
        if(!isStarted) {
            Toast.makeText(_contesto, "Start", Toast.LENGTH_SHORT).show();
            mGoogleApiClient.connect();
            isStarted = true;
        }
    }

    public void stop(){
        if(isStarted) {
            StopLocationUpdates();

            mll.store(_contesto, "output.json"); //Salva sulla memoria del telefono tutte le location.
            mll.clear();

            mGoogleApiClient.disconnect();
            Toast.makeText(_contesto, "Stop", Toast.LENGTH_SHORT).show();
            isStarted = false;
        }
    }

    //da rivedere
    private void isPrepareToStop(){
        Location lastLocation = mll.get((mll.size() - 1));
        if((lastLocation.hasSpeed() && lastLocation.getSpeed() < 4.0) || (true)){
            indexLastLocation = (mll.size() - 1);

        }
    }

    //da rivedere
    private void isPrepareToRestart(){

    }

    public void updateLocationRequest(int interval, int fastestInterval, int priority){
        //Stop
        StopLocationUpdates();

        //Inizializa una nuova LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priority);

        //Avvia location Updates
        StartLocationUpdates();

        Toast.makeText(_contesto, "LocationRequest Updated. Priority: " + priority, Toast.LENGTH_SHORT).show();
    }

    private void StartLocationUpdates(){
        //Richiesta accesso Posizione
        if ( ContextCompat.checkSelfPermission(_contesto, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


    }

    private void StopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        StartLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(_contesto, "Update: " + location.toString(), Toast.LENGTH_LONG).show();
        mll.add(location);
        //AddOutput(location);
    }

    //Error method -------------
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //Error method -------------
    @Override
    public void onConnectionSuspended(int i) {

    }

    //SOLO TEST! --------------
    /*private void Output(Location location){
        _dataOutput.setText("Lat: " + String.valueOf(location.getLatitude()) + "\nLon: " + String.valueOf(location.getLongitude()) + "\nAlt: " + String.valueOf(location.getAltitude()) + "\nTime: " + DateFormat.getTimeInstance().format(new Date()));
    } */

    //SOLO TEST!
    private void AddOutput(Location location){

        JSONObject jsontmp = StorageJson.read(_contesto, "output.json");
        if(jsontmp == null){
            Log.e("err", "errore read");
            jsontmp = new JSONObject();
        }
        try {
            JSONArray ja;
            try {
                ja = (JSONArray)jsontmp.get("points");
            } catch (Exception e){
                e.toString();
                ja = new JSONArray();
            }
            JSONObject jsontmp2 = new JSONObject();
            jsontmp2.put("lat", String.valueOf(location.getLatitude()));
            jsontmp2.put("lon", String.valueOf(location.getLongitude()));
            jsontmp2.put("alt", String.valueOf(location.getAltitude()));
            jsontmp2.put("acc", String.valueOf(location.getAccuracy()));
            jsontmp2.put("speed", String.valueOf(location.getSpeed()));
            jsontmp2.put("time", String.valueOf(location.getTime()));
            ja.put(jsontmp2);
            jsontmp.put("points", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!StorageJson.store(_contesto, "output.json", jsontmp)){
            Log.e("err", "errore store");
        }
    }

   /* @Override
    protected void onPause() {
        super.onPause();
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    } */
}