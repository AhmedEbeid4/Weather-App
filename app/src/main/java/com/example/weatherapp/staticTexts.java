package com.example.weatherapp;

public class staticTexts {
    private static final  String API_KEY=BuildConfig.API_KEY;
    public static final String USER_LOCATION_DB_CREATION="CREATE TABLE userLocation(id INTEGER PRIMARY KEY,lon VARCHAR(30) NOT NULL, lat VARCHAR(30) NOT NULL,city VARCHAR(15) NOT NULL);";
    public static final String REMOVE_IF_EXISTS_USER_LOCATION_DB="DROP TABLE IF EXISTS userLocation";
    public static final String FROM_FORECAST_REPORT_ACTIVITY="FORECAST";
    public static final String FROM_SEARCH_ACTIVITY="SEARCH";
    public static String weeklyDataURL(double lat, double lon){
        return "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutly&appid="+API_KEY;
    }
    public static String dailyDataURL(String city){
        return "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY;
    }
    public static String getImageResource(String icon){
        return "https://api.openweathermap.org/img/w/"+icon+".png";
    }
}
