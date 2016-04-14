package com.martinelli.riccardo.fueltracker;

import android.content.Context;
import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Riccardo on 09/04/2016.
 */
public final class LocationsList extends ArrayList<Location> {

    public boolean add(Location obj){
        if(super.add(obj)){
            return true;
        }else{
            return false;
        }
    }

    public Location get(int index){
        return super.get(index);
    }

    public void clear(){
        super.clear();
    }

    public boolean store(Context contesto, String filename){
        JSONObject jobj = new JSONObject();
        JSONArray ja = new JSONArray();
        try{
            for (int i = 0; i < this.size(); i++){
                JSONObject jsontmp2 = new JSONObject();
                jsontmp2.put("lat", String.valueOf(this.get(i).getLatitude()));
                jsontmp2.put("lon", String.valueOf(this.get(i).getLongitude()));
                if(this.get(i).hasAltitude()){
                    jsontmp2.put("alt", String.valueOf(this.get(i).getAltitude()));
                }
                if(this.get(i).hasAccuracy()) {
                    jsontmp2.put("acc", String.valueOf(this.get(i).getAccuracy()));
                }
                if(this.get(i).hasSpeed()) {
                    jsontmp2.put("speed", String.valueOf(this.get(i).getSpeed()));
                }
                jsontmp2.put("time", String.valueOf(this.get(i).getTime()));
                ja.put(jsontmp2);
            }
            jobj.put("distance", MathGPS.calcolateDistanceFromLocationsList(this));
            jobj.put("points", ja);
        }catch (Exception e){
            return false;
        }
        return StorageJson.store(contesto, filename, jobj);
    }

    /**
     *
     * @param locList
     * @param delayTime il tempo che deve trascorrere perchè la macchina o la persona venga considerata ferma.
     * @param maxDistance la distanza massima che la macchina o la persona può percorrere prima di fermarsi. (unità metri)
     * @return index of the last useful point. (-1 la macchina è im movimento)
     */
    public static int getLastUsefulPositionBeforeStopping(LocationsList locList, int delayTime, int maxDistance){
        int distance = 0;
        boolean isExit = false;
        int result = -1;

        for(int i = locList.size() - 1; !isExit; i--){
            if(i-1 >= 0){
                distance += MathGPS.distance(locList.get(i), locList.get(i-1));
                if(distance <= maxDistance  && delayTime <= locList.get(locList.size() - 1).getTime() - locList.get(i-1).getTime()){
                    result = i - 1;
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
