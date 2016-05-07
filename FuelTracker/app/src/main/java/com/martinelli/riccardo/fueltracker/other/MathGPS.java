package com.martinelli.riccardo.fueltracker.other;

import android.location.Location;

import com.martinelli.riccardo.fueltracker.locationsTracker.data.LocationsList;
import com.martinelli.riccardo.fueltracker.locationsTracker.data.MyLocation;

import java.util.Iterator;

/**
 * Created by Riccardo on 08/04/2016.
 */
public final class MathGPS {

    private MathGPS(){}
    //Distance between two distintc locations
    public static double distance(Location pt1, Location pt2){

        double lat1 = pt1.getLatitude();
        double lat2 = pt2.getLatitude();
        double lon1 = pt1.getLongitude();
        double lon2 = pt2.getLongitude();
        double el1 = 0;
        if (pt1.hasAltitude()){
     //       el1 = pt1.getAltitude();
        }
        double el2 = 0;
        if (pt2.hasAltitude()){
     //      el2 = pt2.getAltitude();
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

        //Controllo approssimato se i due punti si potrebbero toccare.
        double result = Math.sqrt(distance);
        if(result < pt1.getAccuracy() + pt2.getAccuracy())
            result = 0;

        return result;
    }

    //Calcola distanza tra tutti i punti presenti distinti della LocationsList prediligendo quelli con un migliore accuracy.
    //Da rivedere. Bug delle distanze consecutive.
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
                    double tmpDistance = distance(tmp, tmp2);
                    if(tmpDistance > 0.0){
                        distance += tmpDistance;
                        tmp = tmp2;
                    }
                    else{
                        //Caso in cui il calcolo della distanza sarebbe troppo impreciso sceglie per il prossimo calcolo quello con una migliore accuracy.
                        if(tmp.getAccuracy() < tmp2.getAccuracy())
                            tmp = tmp2;
                    }
                }
            }

        }while (i.hasNext());


        return distance;
    }

    //da rivedre
    public static LocationsList clearLocationsList(LocationsList locList){
        LocationsList newLocList = new LocationsList();

        Location bestLocationWhenIsOther = null;
        Location tmp = null;

        for (int i = 0; i < locList.size(); i++){
            tmp = locList.get(i);
            if(tmp instanceof MyLocation){
                MyLocation ml = (MyLocation)tmp;
                if(ml.getsituation() == MyLocation.TAG_IN_VEHICLE){
                    if(bestLocationWhenIsOther!= null){
                        newLocList.add(bestLocationWhenIsOther);
                        bestLocationWhenIsOther = null;
                    }
                    newLocList.add(ml);
                }
                if(ml.getsituation() == MyLocation.TAG_OTHER){
                    if (bestLocationWhenIsOther == null)
                        bestLocationWhenIsOther = tmp;
                    if(tmp.getAccuracy() < bestLocationWhenIsOther.getAccuracy())
                        bestLocationWhenIsOther = tmp;
                }
            }
            else{
                newLocList.add(tmp); // Non dovrebbe accadere, ma nn si sa mai.
            }

        }

        if(tmp != null && bestLocationWhenIsOther != null )
            newLocList.add(bestLocationWhenIsOther);

        return newLocList;
    }
}
