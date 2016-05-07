package com.martinelli.riccardo.fueltracker.locationsTracker.data;

import android.location.Location;

/**
 * Created by Riccardo on 07/05/2016.
 */
public class MyLocation extends Location {

    public static String TAG_IN_VEHICLE = "IN_VEHICLE";
    public static String TAG_OTHER = "OTHER";

    public MyLocation(Location superLocation, String situation){
        super(superLocation);
        this.situation = situation;
    }

    private String situation;

    public String getsituation(){
        return situation;
    }

    public void setSituation(String situation){
        this.situation = situation;
    }
}
