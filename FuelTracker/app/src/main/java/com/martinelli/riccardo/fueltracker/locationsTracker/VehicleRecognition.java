package com.martinelli.riccardo.fueltracker.locationsTracker;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Riccardo on 01/05/2016.
 */
abstract class VehicleRecognition implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private Context context; //Serve per sapere da quale activity è stato lanciato il LocationsTracker
    private long detectionIntervalMillis = 0;

    private PendingIntent callbackIntent;

    public VehicleRecognition(Context context){
        this.context = context;

        //Inizializza google api client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(ActivityRecognition.API)
                    .build();
        }
    }

    public void start(){
        mGoogleApiClient.connect();
    }

    public void stop(){
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, callbackIntent);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle){
        callbackIntent = PendingIntent.getService(context, 0, new Intent(context, ActivityRecognitionService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, detectionIntervalMillis, callbackIntent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private class ActivityRecognitionService extends IntentService{

        public ActivityRecognitionService(){
            super("ActivityRecognitionService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            if (ActivityRecognitionResult.hasResult(intent)) {
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                if (result.getMostProbableActivity().getType() == DetectedActivity.IN_VEHICLE){
                    vehicleRecognized();
                }
            }
        }
    }


    public abstract void vehicleRecognized();
}
