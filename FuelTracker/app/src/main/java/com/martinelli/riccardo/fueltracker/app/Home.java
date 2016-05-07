package com.martinelli.riccardo.fueltracker.app;

import android.app.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

//import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.martinelli.riccardo.fueltracker.R;
import com.martinelli.riccardo.fueltracker.locationsTracker.ServiceTracker;
import com.martinelli.riccardo.fueltracker.other.StorageJson;
import com.martinelli.riccardo.fueltracker.locationsTracker.LocationsTracker;

import org.json.JSONObject;


//This is only a test app.


public class Home extends Activity {

    LocationsTracker locationsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Richiesta accesso Posizione
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS){
            Toast.makeText(getApplicationContext(), "GooglePlayServices isn't available.", Toast.LENGTH_LONG).show();
            return;
        }


        //locationsTracker = new LocationsTracker(this.getApplicationContext()){
            /*@Override
            public void onStop(){

            }*/
        //};
    }

    public void OnClickStart(View v){
        //locationsTracker.start();
        Intent ServiceIntent = new Intent(this, ServiceTracker.class);
        startService(ServiceIntent);
    }

    public void OnClickStop(View v){
        //locationsTracker.stop();
        Intent ServiceIntent = new Intent(this, ServiceTracker.class);
        stopService(ServiceIntent);
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

    }

    public void onClickInMaps(View v){
        JSONObject jsontmp = StorageJson.read(this, "output.json");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("LocationsTracker", jsontmp.toString()));

        Uri webpage = Uri.parse("http://wpdiary.altervista.org/maps.html");

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
