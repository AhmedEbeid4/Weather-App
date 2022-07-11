package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class ForecastActivity extends AppCompatActivity {
    private double latitude;
    private double longitude;
    private String city;
    private String country;
    private TextView header;
    private RecyclerView forecastReportRecyclerView;
    private weatherAdapter adapter;
    private ArrayList<Weather> weatherArrayList;
    private GifImageView internetConnectionFailedAnimation;
    private Button retryButton;
    private BottomNavigationView bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        getSupportActionBar().hide();
        if(this.getIntent() != null){
            latitude=this.getIntent().getDoubleExtra("lat",0);
            longitude=this.getIntent().getDoubleExtra("lon",0);
            city=this.getIntent().getStringExtra("city");
            country=this.getIntent().getStringExtra("country");
        }

        assignComponents();
        actions();
        loadJson();
    }
    private void actions(){
        barActions();
        retryButtonAction();
        onItemClickAction();
    }
    private void assignComponents(){
        bar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bar.setSelectedItemId(R.id.forecast);
        weatherArrayList=new ArrayList<>();
        header=(TextView) findViewById(R.id.forecastReportTextView);
        forecastReportRecyclerView=(RecyclerView) findViewById(R.id.forecastReportsRecyclerView);
        adapter=new weatherAdapter(weatherArrayList,this,staticTexts.FROM_FORECAST_REPORT_ACTIVITY);
        forecastReportRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        forecastReportRecyclerView.setAdapter(adapter);
        internetConnectionFailedAnimation=(GifImageView) findViewById(R.id.internet_connection_animation_forecast);
        retryButton=(Button) findViewById(R.id.retryBtn);
    }
    private void onItemClickAction(){
        adapter.setOnClickListener(new weatherAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                goToShowWeather(weatherArrayList.get(position));
            }
        });
    }
    private void retryButtonAction(){
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJson();
            }
        });
    }
    private void NoInternetConnection(){
        header.setVisibility(View.GONE);
        weatherArrayList.clear();
        adapter.notifyDataSetChanged();
        forecastReportRecyclerView.setVisibility(View.GONE);
        internetConnectionFailedAnimation.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }
    private void InternetConnectionRestored(){
        header.setVisibility(View.VISIBLE);
        forecastReportRecyclerView.setVisibility(View.VISIBLE);
        internetConnectionFailedAnimation.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }
    private void barActions(){
        bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                DataPasser dataPasser=new DataPasser(ForecastActivity.this,latitude,longitude,city,country);
                if (item.getItemId() == R.id.home) {
                    dataPasser.goToAnotherActivity(Home.class);
                } else if (item.getItemId() == R.id.forecast) {
                    Toast.makeText(ForecastActivity.this, "You are already in Forecast!", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.search) {
                    dataPasser.goToAnotherActivity(searchActivity.class);
                }
                return false;
            }
        });
    }
    private void loadJson(){
        Ion.with(this).load(staticTexts.weeklyDataURL(latitude,longitude))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null){
                            NoInternetConnection();
                        }else {
                            if(retryButton.getVisibility() == View.VISIBLE){
                                InternetConnectionRestored();
                            }
                            ArrayList<Weather> weatherArrayList=new ArrayList<>();
                            JsonArray days=result.get("daily").getAsJsonArray();
                            for(int i=0; i<days.size();i++){
                                JsonObject day=days.get(i).getAsJsonObject();
                                String date=day.get("dt").getAsString();

                                JsonObject tempObject=day.get("temp").getAsJsonObject();

                                double temperature=tempObject.get("day").getAsDouble();

                                double humidity=day.get("humidity").getAsDouble();

                                double windSpeed=day.get("wind_speed").getAsDouble();
                                int windDegree=day.get("wind_deg").getAsInt();

                                JsonObject weatherObject=day.get("weather").getAsJsonArray().get(0).getAsJsonObject();

                                String mainStatue=weatherObject.get("main").getAsString();
                                String desc=weatherObject.get("description").getAsString();
                                String icon=staticTexts.getImageResource(weatherObject.get("icon").getAsString());
                                Weather w=new Weather(longitude,latitude,mainStatue,desc,icon,temperature,humidity,windDegree,windSpeed,date,city,country);
                                weatherArrayList.add(w);
                            }

                            modifyComponents(weatherArrayList);
                        }
                    }
                });
    }
    private void modifyComponents(ArrayList<Weather> weatherArrayList1){
        weatherArrayList=weatherArrayList1;
//        adapter.notifyDataSetChanged();

        adapter.setWeatherArrayList(weatherArrayList1);
        forecastReportRecyclerView.setAdapter(adapter);
    }
    private void goToShowWeather(Weather weather){
        Intent i = new Intent(this, Home.class);
        i.putExtra("lat", latitude);
        i.putExtra("lon", longitude);
        i.putExtra("city", city);
        i.putExtra("country", country);
        i.putExtra("sender",staticTexts.FROM_FORECAST_REPORT_ACTIVITY);
        i.putExtra("weather",weather);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        DataPasser dataPasser=new DataPasser(ForecastActivity.this,latitude,longitude,city,country);
        dataPasser.goToAnotherActivity(Home.class);
    }
}