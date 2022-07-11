package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


public class DataPasser{

    private AppCompatActivity sender;

    private double latitude;
    private double longitude;
    private String city;
    private String country;

    public DataPasser() {
    }

    public DataPasser(AppCompatActivity sender,  double latitude, double longitude, String city, String country) {
        this.sender = sender;

        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public void goToAnotherActivity(Class destination){
        Intent i = new Intent(sender,destination);
        i.putExtra("lat", latitude);
        i.putExtra("lon", longitude);
        i.putExtra("city", city);
        i.putExtra("country", country);
        sender.startActivity(i);
        sender.overridePendingTransition(0, 0);
    }
}
