package com.martinelli.riccardo.fueltracker.locationsTracker;

import android.content.Context;
import android.location.Location;

import com.martinelli.riccardo.fueltracker.other.MathGPS;
import com.martinelli.riccardo.fueltracker.other.StorageJson;
import com.martinelli.riccardo.fueltracker.other.UsefulMethods;

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
                jsontmp2.put("time", UsefulMethods.getFormattedTime(this.get(i).getTime()));
                ja.put(jsontmp2);
            }
            jobj.put("distance", MathGPS.calcolateDistanceFromLocationsList(this));
            jobj.put("points", ja);
        }catch (Exception e){
            return false;
        }
        return StorageJson.store(contesto, filename, jobj);
    }


}
