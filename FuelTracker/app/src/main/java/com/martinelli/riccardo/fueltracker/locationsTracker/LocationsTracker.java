package com.martinelli.riccardo.fueltracker.locationsTracker;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.martinelli.riccardo.fueltracker.R;
import com.martinelli.riccardo.fueltracker.locationsTracker.data.LocationsList;
import com.martinelli.riccardo.fueltracker.locationsTracker.listener.LocationsListener;
import com.martinelli.riccardo.fueltracker.locationsTracker.listener.VehicleRecognition;

/**
 * Created by Riccardo on 07/04/2016.
 */
public abstract class LocationsTracker {

    private Context context; //Serve per sapere da quale activity è stato lanciato il LocationsTracker
    LocationsList mLocationsList = new LocationsList(); //Lista per lo store di tutte le locations.
    VehicleRecognition vehicleRecognition;
    LocationsListener locationsRequester;

    public LocationsTracker(Context contesto) {
        context = contesto;

        locationsRequester = new LocationsListener(context) {
            @Override
            public void useLocation(Location location){
                mLocationsList.add(location);
            }

            @Override
            public void onLocationRequesterReady(){

            }
        };

        vehicleRecognition = new VehicleRecognition(context){
            @Override
            public void vehicleRecognized(){
                locationsRequester.setHigAccuracyMode();
                creaNotifica("Sei in macchina?", "Guida con prudenza!", 001);
            }

            @Override
            public void stillRecognized(){
                locationsRequester.setNoPowerMode();
                creaNotifica("Sono immobile", "Zzzzz...", 002);
            }

            @Override
            public void onVehicleRecognitionReady(){

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

        mLocationsList.store(context, "output.json");
        onStop();
    }

    //Test
    private void creaNotifica(String title, String text, int id){
        //Test
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);
        NotificationManager mNotifyMgr =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(id, mBuilder.build());
    }

    public abstract void onStop();
}