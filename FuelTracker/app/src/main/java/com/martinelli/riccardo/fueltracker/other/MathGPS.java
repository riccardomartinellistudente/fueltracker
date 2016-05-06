package com.martinelli.riccardo.fueltracker.other;

import android.location.Location;

import com.martinelli.riccardo.fueltracker.locationsTracker.data.LocationsList;

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

    /**
     * metodo per restituire l'ultima posizione interesante della macchina.
     * @param locList
     * @param delayTime il tempo che deve trascorrere perchè la macchina o la persona venga considerata ferma.
     * @param maxDistanceBeforeStopping la distanza massima che la macchina o la persona può percorrere prima di fermarsi. (unità metri)
     * @return index of the last useful point. (-1 la macchina è im movimento)
     */
    public static int getLastUsefulPositionBeforeStopping(LocationsList locList, int delayTime, double maxDistanceBeforeStopping){
        double distance = 0;
        boolean isExit = false;
        int result = -1;

        LocationsList myLocationList = new LocationsList();
        for(int i = locList.size() - 1; !isExit; i--){
            if(i-1 >= 0){
                myLocationList.add(locList.get(i));
                if(delayTime <= locList.get(locList.size() - 1).getTime() - locList.get(i).getTime()){
                   if(calcolateDistanceFromLocationsList(myLocationList) <= maxDistanceBeforeStopping){
                        result = i-1;
                   }else{

                   }
                    isExit = true;
                }
            }
            else{
                isExit = true;
            }

        }

        return result;
    }
}
