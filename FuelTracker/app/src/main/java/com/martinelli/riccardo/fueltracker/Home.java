package com.martinelli.riccardo.fueltracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.gms.location.LocationRequest;

import org.json.JSONObject;


//This is only a test app.


public class Home extends Activity {

    DistanceTracker dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void OnClickStart(View v){
        switch (((SeekBar) findViewById(R.id.seekAccuracy)).getProgress()){
            case 0: dt = new DistanceTracker(this, Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_NO_POWER); break;
            case 1: dt = new DistanceTracker(this, Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_LOW_POWER); break;
            case 2: dt = new DistanceTracker(this, Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); break;
            case 3: dt = new DistanceTracker(this, Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_HIGH_ACCURACY); break;
        }

        dt.start();
    }

    public void OnClickStop(View v){
        dt.stop();
    }

    public void OnClickExport(View v){
        //Export
        JSONObject jsontmp = StorageJson.read(this, "output.json");
        if(jsontmp != null){
            String shareBody = jsontmp.toString();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    public void OnClickUpdateLocationRequest(View v){

        switch (((SeekBar) findViewById(R.id.seekAccuracy)).getProgress()){
            case 0: dt.updateLocationRequest(Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_NO_POWER); break;
            case 1: dt.updateLocationRequest(Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_LOW_POWER); break;
            case 2: dt.updateLocationRequest(Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); break;
            case 3: dt.updateLocationRequest(Integer.parseInt(((EditText) findViewById(R.id.txtInterval)).getText().toString()), Integer.parseInt(((EditText) findViewById(R.id.txtMaxInterval)).getText().toString()), LocationRequest.PRIORITY_HIGH_ACCURACY); break;
        }


    }
}
