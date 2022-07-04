package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class searchActivity extends AppCompatActivity {
    private Double longitude;
    private Double latitude;
    private String city;
    private String country;
    private String city1;
    private String country1;
    private TextInputEditText searchField;
    private Button searchButton;
    private BottomNavigationView bottomBar;
    private TextView locationTextView;
    private TextView dataTextView;
    private ImageView WeatherStatueIcon;
    private TextView tempTextView;
    private TextView statueTextView;
    private TextView HumidityTextView;
    private TextView windDegreeTextView;
    private TextView windSpeedTextView;
    private GifImageView internetConnectionAnimation;
    private GifImageView windMillAnimation;
    private TextView _1;
    private TextView _2;
    private TextView _3;
    private TextView _4;
    private TextView _5;
    private TextView _6;
    private Button retryButton;
    private Weather w;
    private RecyclerView weeklyWeather;
    private cityWeatherAdapter adapter;
    private ArrayList<Weather> weatherArrayList;
    private boolean b;
    private double iu;
    private double iiu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        if(this.getIntent() != null){
            latitude=this.getIntent().getDoubleExtra("lat",0);
            longitude=this.getIntent().getDoubleExtra("lon",0);
            city=this.getIntent().getStringExtra("city");
            country=this.getIntent().getStringExtra("country");
        }

        assignComponents();
        actions();
    }
    private void assignComponents(){
        searchField=(TextInputEditText) findViewById(R.id.cityTextField);
        searchButton=(Button) findViewById(R.id.searchBtn);

        locationTextView=(TextView) findViewById(R.id.locationTextView_Home);
        dataTextView=(TextView) findViewById(R.id.DateTextView_home);
        WeatherStatueIcon=(ImageView) findViewById(R.id.iconImageView_home);
        tempTextView=(TextView) findViewById(R.id.TempTextView_home);
        statueTextView=(TextView) findViewById(R.id.statueTextView_home);
        HumidityTextView =(TextView) findViewById(R.id.HumidityTextView_home);
        windDegreeTextView =(TextView) findViewById(R.id.windDegreeTextView_home);
        windSpeedTextView =(TextView) findViewById(R.id.WindSpeedTextView_home);
        _1=(TextView) findViewById(R.id.temp);
        _2=(TextView) findViewById(R.id.statue);
        _3=(TextView) findViewById(R.id.Hum);
        _4=(TextView) findViewById(R.id.degeree);
        _5=(TextView) findViewById(R.id.speed);
        _6=(TextView) findViewById(R.id.km);
        windMillAnimation=(GifImageView) findViewById(R.id.windMill);
        weeklyWeather=(RecyclerView) findViewById(R.id.daysWeatherRecyclerView);
        weatherArrayList=new ArrayList<>();
        adapter=new cityWeatherAdapter(weatherArrayList,this);
        weeklyWeather.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        weeklyWeather.setAdapter(adapter);
        bottomBar=(BottomNavigationView) findViewById(R.id.bottomBar);
        internetConnectionAnimation=(GifImageView) findViewById(R.id.internet_connection_animation_search);
        retryButton=(Button) findViewById(R.id.retryBtn);

        weeklyWeather.setVisibility(View.GONE);
        locationTextView.setVisibility(View.GONE);
        dataTextView.setVisibility(View.GONE);
        WeatherStatueIcon.setVisibility(View.GONE);
        tempTextView.setVisibility(View.GONE);
        statueTextView.setVisibility(View.GONE);
        HumidityTextView.setVisibility(View.GONE);
        _1.setVisibility(View.GONE);
        _2.setVisibility(View.GONE);
        _3.setVisibility(View.GONE);
        _4.setVisibility(View.GONE);
        _5.setVisibility(View.GONE);
        _6.setVisibility(View.GONE);
        windMillAnimation.setVisibility(View.GONE);
        windSpeedTextView.setVisibility(View.GONE);
        windDegreeTextView.setVisibility(View.GONE);
    }
    private void actions(){
        barActions();
        retryButtonAction();
        searchButtonAction();
        onItemClickAction();
    }
    private void searchButtonAction(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchField.getText().toString().length()>0){
                        getDailyWeather(searchField.getText().toString());

                } else {
                    Toast.makeText(searchActivity.this,"You have to enter the city that you want before clicking on search button",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void retryButtonAction(){
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetConnectionRestoredAction();
            }
        });
    }
    private void barActions(){
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    goToHome();
                } else if (item.getItemId() == R.id.forecast) {
                    goToForecastActivity();
                } else if (item.getItemId() == R.id.search) {
                    Toast.makeText(searchActivity.this, "You are already in Search!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
    private void goToForecastActivity(){
        Intent i = new Intent(this, ForecastActivity.class);
        i.putExtra("lat", latitude);
        i.putExtra("lon", longitude);
        i.putExtra("city", city);
        i.putExtra("country", country);
        startActivity(i);
        overridePendingTransition(0, 0);

    }
    private void goToHome(){
        Intent i = new Intent(this, Home.class);
        i.putExtra("lat", latitude);
        i.putExtra("lon", longitude);
        i.putExtra("city", city);
        i.putExtra("country", country);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    private void NoInternetConnectionAction(){
        searchField.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        weeklyWeather.setVisibility(View.GONE);
        locationTextView.setVisibility(View.GONE);
        dataTextView.setVisibility(View.GONE);
        WeatherStatueIcon.setVisibility(View.GONE);
        tempTextView.setVisibility(View.GONE);
        statueTextView.setVisibility(View.GONE);
        HumidityTextView.setVisibility(View.GONE);
        _1.setVisibility(View.GONE);
        _2.setVisibility(View.GONE);
        _3.setVisibility(View.GONE);
        _4.setVisibility(View.GONE);
        _5.setVisibility(View.GONE);
        _6.setVisibility(View.GONE);
        windMillAnimation.setVisibility(View.GONE);
        windSpeedTextView.setVisibility(View.GONE);
        windDegreeTextView.setVisibility(View.GONE);
        internetConnectionAnimation.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }
    private void InternetConnectionRestoredAction(){
        searchField.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        internetConnectionAnimation.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        assignComponents();
    }
    private void getDailyWeather(String city){
        Ion.with(searchActivity.this).load(staticTexts.dailyDataURL(city)).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null){
                            Toast.makeText(searchActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                            NoInternetConnectionAction();
                        }else {
                            try {
                                if (retryButton.getVisibility() == View.VISIBLE){
                                    InternetConnectionRestoredAction();
                                }
                                JsonObject coordinate=result.getAsJsonObject("coord");
                                double lon=coordinate.get("lon").getAsDouble();
                                double lat=coordinate.get("lat").getAsDouble();
                                JsonObject weather=result.getAsJsonArray("weather").get(0).getAsJsonObject();
                                String mainStatue=weather.get("main").getAsString();
                                String des=weather.get("description").getAsString();
                                String icon=weather.get("icon").getAsString();
                                icon=staticTexts.getImageResource(icon);
                                JsonObject main=result.get("main").getAsJsonObject();
                                double temp=main.get("temp").getAsDouble();
                                double humidity=main.get("humidity").getAsDouble();
                                JsonObject wind=result.get("wind").getAsJsonObject();
                                double windSpeed=wind.get("speed").getAsDouble();
                                int windDegree=wind.get("deg").getAsInt();

                                String date=result.get("dt").getAsString();

                                JsonObject sys=result.get("sys").getAsJsonObject();
                                String country=sys.get("country").getAsString();

                                String city=result.get("name").getAsString();

                                Weather todayWeather=new Weather(lon,lat,mainStatue,des,icon,temp,humidity,windDegree,windSpeed,date,country,city);

                                modifyComponents(todayWeather);

                            }catch (Exception e1){
                                Toast.makeText(searchActivity.this,"This city doesn\'t exist in tha database",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

    }
    private void modifyComponents(Weather w){
        country=w.getCountry();
        iu=w.getLon();
        iiu=w.getLat();
        city1=w.getCityName();
        country1=w.getCountry();
        locationTextView.setText(w.getCityName()+", "+w.getCountry());
        dataTextView.setText(w.getDate());
        Ion.with(this).load(w.getIconResource()).intoImageView(WeatherStatueIcon);
        tempTextView.setText((w.getTempWithC()));
        statueTextView.setText(w.getDesc());
        HumidityTextView.setText(""+w.getHumidity());
        windDegreeTextView.setText(""+w.getWindDegree());
        windSpeedTextView.setText(""+w.getWindSpeed());
        weeklyWeather.setVisibility(View.VISIBLE);
        locationTextView.setVisibility(View.VISIBLE);
        dataTextView.setVisibility(View.VISIBLE);
        WeatherStatueIcon.setVisibility(View.VISIBLE);
        tempTextView.setVisibility(View.VISIBLE);
        statueTextView.setVisibility(View.VISIBLE);
        HumidityTextView.setVisibility(View.VISIBLE);
        _1.setVisibility(View.VISIBLE);
        _2.setVisibility(View.VISIBLE);
        _3.setVisibility(View.VISIBLE);
        _4.setVisibility(View.VISIBLE);
        _5.setVisibility(View.VISIBLE);
        _6.setVisibility(View.VISIBLE);
        windMillAnimation.setVisibility(View.VISIBLE);
        windSpeedTextView.setVisibility(View.VISIBLE);
        windDegreeTextView.setVisibility(View.VISIBLE);
        Ion.with(this).load(staticTexts.weeklyDataURL(w.getLat(),w.getLon()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null){
                            NoInternetConnectionAction();
                        }else {
                            if(retryButton.getVisibility() == View.VISIBLE){
                                InternetConnectionRestoredAction();
                            }
                            ArrayList<Weather> weatherArrayList1=new ArrayList<>();
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
                                Weather w=new Weather(iu,iiu,mainStatue,desc,icon,temperature,humidity,windDegree,windSpeed,date,country1,city1);
                                weatherArrayList1.add(w);
                            }
                            weatherArrayList=weatherArrayList1;
                            adapter.setWeatherArrayList(weatherArrayList1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    private void onItemClickAction(){
        adapter.setOnClickListener(new cityWeatherAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                goToShowWeather(weatherArrayList.get(position));
            }
        });
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
        goToHome();
    }
}