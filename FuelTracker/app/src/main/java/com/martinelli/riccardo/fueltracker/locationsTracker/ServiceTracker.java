package com.martinelli.riccardo.fueltracker.locationsTracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Filippo on 07/05/2016.
 */
public class ServiceTracker  extends Service {

    LocationsTracker locationsTracker;

    @Override
    public void onCreate() {
        locationsTracker = new LocationsTracker(this.getApplicationContext());

        locationsTracker.creaNotifica("Fuel tracker", "Servizio creato", 003);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        locationsTracker.creaNotifica("Fuel tracker", "Servizio partito", 003);
        locationsTracker.start();

        return 0;
    }

    @Override
    public void onDestroy() {
        locationsTracker.creaNotifica("Fuel tracker", "Servizio stoppato", 003);
        locationsTracker.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
