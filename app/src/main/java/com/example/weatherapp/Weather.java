package com.example.weatherapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather implements Parcelable {
    private double lon;
    private double lat;
    private String main;
    private String desc;
    private String iconResource;
    private double temp;
    private double humidity;
    private int windDegree;
    private double windSpeed;
    private String date;
    private String country;
    private String cityName;

    public Weather(double lon, double lat, String main, String desc, String iconResource, double temp, double humidity, int windDegree, double windSpeed, String date, String country, String cityName) {
        this.lon = lon;
        this.lat = lat;
        this.main = main;
        this.desc = desc;
        this.iconResource = iconResource;
        this.temp = Double.parseDouble(kelvinToCelsius(temp));
        this.humidity = humidity;
        this.windDegree = windDegree;
        this.windSpeed = windSpeed;
        Date d=new Date(Long.parseLong(date)*1000);
        DateFormat dateFormat=new SimpleDateFormat("EEE dd,MMM,yyyy");
        this.date=(dateFormat.format(d));
        this.country = country;
        this.cityName = cityName;
    }
    protected Weather(Parcel in) {
        lon = in.readDouble();
        lat = in.readDouble();
        main = in.readString();
        desc = in.readString();
        iconResource = in.readString();
        temp = in.readDouble();
        humidity = in.readDouble();
        windDegree = in.readInt();
        windSpeed = in.readDouble();
        date = in.readString();
        country = in.readString();
        cityName = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDate(String date) {
        Date d=new Date(Long.parseLong(date)*1000);
        DateFormat dateFormat=new SimpleDateFormat("EEE dd, MMM, yyyy");
        this.date = dateFormat.format(d);
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getMain() {
        return main;
    }

    public String getDesc() {
        return desc;
    }

    public String getIconResource() {
        return iconResource;
    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getWindDegree() {
        return windDegree;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getCityName() {
        return cityName;
    }
    public String getDay(){
        return date.charAt(0)+""+date.charAt(1)+""+date.charAt(2);
    }
    public String getDayAndMonth(){
        String o="";
        for (int i=4 ; i<10;i++){
            o=o+date.charAt(i);
        }
        return o;
    }
    private String kelvinToCelsius(double d){
        d=d-273.15;
        String a="";
        String doubleToString=String.valueOf(d);
        int t=doubleToString.indexOf('.');
        for (int i =0 ; i<t+2;i++){
            a=a+doubleToString.charAt(i);

        }
        return a;
    }
    public String getTempWithC(){
        return this.temp+"°C";
    }
    public Integer changeTempToInteger(){
        String o="";
        for (int i=0;i<String.valueOf(temp).length();i++){
            if (String.valueOf(temp).charAt(i) == '.'){
                break;
            }
            o=o+String.valueOf(temp).charAt(i);
        }
        return Integer.parseInt(o);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeString(main);
        dest.writeString(desc);
        dest.writeString(iconResource);
        dest.writeDouble(temp);
        dest.writeDouble(humidity);
        dest.writeInt(windDegree);
        dest.writeDouble(windSpeed);
        dest.writeString(date);
        dest.writeString(country);
        dest.writeString(cityName);
    }
}
