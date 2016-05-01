package com.martinelli.riccardo.fueltracker.other;

import android.content.Context;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Riccardo on 07/04/2016.
 */
public final class StorageJson {
    private StorageJson(){}

    public static boolean store(Context context, String filename, JSONObject obj){
        FileOutputStream outputStream;
        boolean result = false;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(obj.toString().getBytes());
            outputStream.close();
            result = true;
        } catch (Exception e) {

        }

        return result;
    }

    public static JSONObject read(Context context, String filename){
        FileInputStream inputStream;
        JSONObject obj = null;
        try {
            inputStream = context.openFileInput(filename);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String str = new String(buffer, StandardCharsets.UTF_8);
            obj = new JSONObject(str);
        } catch (Exception e){
            //Restituisce un null
        }

        return obj;
    }
}
