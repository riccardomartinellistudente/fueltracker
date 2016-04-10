package com.martinelli.riccardo.fueltracker;

import android.content.Context;
import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Riccardo on 09/04/2016.
 */
public final class LocationsList extends ArrayList {

    private double distance = 0.0;

    public boolean add(Object obj){
        if(super.add(obj)){
            if(this.size() != 1) {
                distance += MathGPS.distance(this.get(this.size() - 1), (Location) obj); // calcola la distanza quando viene aggiunto un nuovo punto. (dopo il primo)
            }
            return true;
        }else{
            return false;
        }
    }

    public Location get(int index){
        return (Location) super.get(index);
    }

    public void clear(){
        super.clear();
        distance = 0.0;
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
            jobj.put("distance", distance);
            jobj.put("points", ja);
        }catch (Exception e){
            return false;
        }
        return StorageJson.store(contesto, filename, jobj);
    }
}