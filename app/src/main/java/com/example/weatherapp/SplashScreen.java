package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GifImageView animation;
    private Button retryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();
        animation=(GifImageView) findViewById(R.id.firstAnimation);
        retryBtn=(Button) findViewById(R.id.retryBtn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.setImageResource(R.drawable.earth_moon_and_sun);
                retryBtn.setVisibility(View.GONE);
                start();
            }
        });
        start();
    }
    private void start(){
        if(isGPSOpened()){
            operationWithGettingLocation();
        }else if (!isGPSOpened() && new userLocationDB(this).getData().size() >0){
            Toast.makeText(this,"GPS is not opened !",Toast.LENGTH_SHORT).show();
            operation();
        }else {
            Toast.makeText(this,"GPS is not opened !, We should take your location, The app will not be opened",Toast.LENGTH_SHORT).show();
        }
    }
    private void operationWithGettingLocation(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestLocationPermission();
                } else {
                    getCurrentLocation();
                }
            }
        }, 3000);
    }
    private void operation(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userLocationDB db=new userLocationDB(SplashScreen.this);
                LonLocation l=db.getData().get(0);
                goToHome(l);
            }
        }, 2500);
    }
    private void goToHome(LonLocation l) {
        Intent i = new Intent(this, Home.class);
        i.putExtra("lat", l.lat);
        i.putExtra("lon", l.lon);
        i.putExtra("city", l.city);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
    private boolean isGPSOpened(){
        LocationManager l=(LocationManager) getSystemService(LOCATION_SERVICE);
        return l.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        LocationServices.getFusedLocationProviderClient(SplashScreen.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(
                                SplashScreen.this
                        ).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() != 0) {
                            int lastLocationIndex = locationResult.getLocations().size() - 1;
                            List<Location> locations = locationResult.getLocations();
                            double localLatitude = locations.get(lastLocationIndex).getLatitude();
                            double localLongitude = locations.get(lastLocationIndex).getLongitude();
                            LonLocation l = new LonLocation();
                            l.lat=localLatitude;
                            l.lon=localLongitude;
                            Ion.with(SplashScreen.this)
                                    .load(staticTexts.weeklyDataURL(l.lat,l.lon))
                                    .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e != null){
                                        Toast.makeText(SplashScreen.this,"Server Error",Toast.LENGTH_SHORT).show();
                                        animation.setImageResource(R.drawable.internet_access);
                                        retryBtn.setVisibility(View.VISIBLE);
                                    }else {
                                        try {
                                            String timezone=result.get("timezone").getAsString();
                                            l.city=getCity(timezone);
                                            userLocationDB db= new userLocationDB(SplashScreen.this);
                                            if (db.getData().size() >0){
                                                LonLocation fromDataBase = new userLocationDB(SplashScreen.this).getData().get(0);
                                                if (fromDataBase.lon != l.lon || fromDataBase.lat != fromDataBase.lat){
                                                    db.update(l);
                                                }
                                            }else {
                                                db.insert(l);
                                            }
                                            goToHome(l);
                                        }catch (Exception e1){
                                            Toast.makeText(SplashScreen.this,"Server Error2",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(SplashScreen.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(SplashScreen.this, "We have to get your location !", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requestLocationPermission();
            }

        }
    }

    private String getCity(String timezone){
        int o=timezone.indexOf('/');
        String city="";
        for (int i=o+1;i<timezone.length();i++){
            city=city+timezone.charAt(i);
        }
        return city.toLowerCase();
    }
}
