package com.martinelli.riccardo.fueltracker;

import android.location.Location;

import java.util.Iterator;

/**
 * Created by Riccardo on 08/04/2016.
 */
public final class MathGPS {

    //Distance between two location
    private static double distance(Location pt1, Location pt2){

        double lat1 = pt1.getLatitude();
        double lat2 = pt2.getLatitude();
        double lon1 = pt1.getLongitude();
        double lon2 = pt2.getLongitude();
        double el1 = 0;
        if (pt1.hasAltitude()){
            el1 = pt1.getAltitude();
        }
        double el2 = 0;
        if (pt2.hasAltitude()){
            el2 = pt2.getAltitude();
        }

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    //Calcola distanza tra tutti i punti presenti nella LocationsList
    public static double calcolateDistanceFromLocationsList(LocationsList locList){
        Iterator<Location> i = locList.iterator();
        double distance = 0.0;
        Location tmp = null;
        do{
            if (i.hasNext())
            {
                if(tmp == null) {
                    tmp = i.next();
                }
                else {
                    Location tmp2 = i.next();
                    distance = distance(tmp, tmp2);
                    tmp = tmp2;
                }
            }

        }while (i.hasNext());


        return distance;
    }

}
