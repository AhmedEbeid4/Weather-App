package com.example.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class userLocationDB extends SQLiteOpenHelper {
    userLocationDB(Context context){
        super(context,"userLocation.dp",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(staticTexts.USER_LOCATION_DB_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(staticTexts.REMOVE_IF_EXISTS_USER_LOCATION_DB);
    }
    public String insert(LonLocation l){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("id",1);
        c.put("lon",String.valueOf(l.lon));
        c.put("lat",String.valueOf(l.lat));
        c.put("city",String.valueOf(l.city));
        long g=db.insert("userLocation",null,c);
        if (g != -1){
            return "Successful operation";
        }else {
            return "Failed";
        }
    }
    public ArrayList<LonLocation> getData(){
        ArrayList<LonLocation> locations = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM userLocation",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            LonLocation l = new LonLocation();
            l.city=c.getString(3);
            l.lon=Double.parseDouble(c.getString(1));
            l.lat=Double.parseDouble(c.getString(2));
            locations.add(l);
            c.moveToNext();
        }
        return locations;
    }
    public void update(LonLocation l){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("lon",String.valueOf(l.lon));
        values.put("lat",String.valueOf(l.lat));
        values.put("city",String.valueOf(l.city));
        db.update("userLocation",values,"id=?",new String[]{String.valueOf(1)});
    }
}
