package com.martinelli.riccardo.fueltracker.locationsTracker;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

/**
 * Created by Riccardo on 07/04/2016.
 */
public final class LocationsTracker {

    private Context context; //Serve per sapere da quale activity è stato lanciato il LocationsTracker
    LocationsList mll = new LocationsList(); //Lista per lo store di tutte le locations.
    VehicleRecognition vehicleRecognition;
    LocationsRequester locationsRequester;

    public LocationsTracker(Context contesto) {
        context = contesto;

        locationsRequester = new LocationsRequester(contesto) {
            @Override
            public void useLocation(Location location){
                mll.add(location);
            }
        };

        vehicleRecognition = new VehicleRecognition(contesto){
            @Override
            public void vehicleRecognized(){
                locationsRequester.setHigAccuracyMode();
            }
        };


    }

    //Metodo pubblico per arrivare il LocationsTracker.
    public void start() {
        locationsRequester.start();
        vehicleRecognition.start();
    }

    //Metodo pubblico per disattivare il LocationsTracker.
    public void stop() {
        locationsRequester.stop();
        vehicleRecognition.stop();
    }


}