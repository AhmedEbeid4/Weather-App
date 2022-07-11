package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import pl.droidsonroids.gif.GifImageView;



public class Home extends AppCompatActivity {
    private Double longitude;
    private Double latitude;
    private String city;
    private String country;
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
    private Button retryButton;
    private BottomNavigationView bar;
    private TextView _1;
    private TextView _2;
    private TextView _3;
    private TextView _4;
    private TextView _5;
    private TextView _6;
    private boolean fromAct=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        if (this.getIntent() != null){
            longitude=this.getIntent().getDoubleExtra("lon",-1);
            latitude=this.getIntent().getDoubleExtra("lat",-1);
            city=this.getIntent().getStringExtra("city");
            if (getIntent().getStringExtra("sender") != null){
                fromAct=true;
                assignComponents();
                if (getIntent().getStringExtra("sender").equals(staticTexts.FROM_FORECAST_REPORT_ACTIVITY) || getIntent().getStringExtra("sender").equals(staticTexts.FROM_SEARCH_ACTIVITY)){
                    bar.setVisibility(View.GONE);
                    modifyComponents(getIntent().getParcelableExtra("weather"));
                }
            }
        }
        if (longitude ==-1 && latitude == -1){
            Toast.makeText(this,"Something went wrong , please restart the app",Toast.LENGTH_SHORT).show();
        }
        if (!fromAct){
            assignComponents();
            actions();
            loadJson();
        }
    }
    private void actions(){
        retryBtnAction();
        navigationBottomBarAction();
    }
    private void retryBtnAction(){
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJson();
            }
        });
    }
    private void navigationBottomBarAction(){
        bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                DataPasser dataPasser=new DataPasser(Home.this,latitude,longitude,city,country);
                if (item.getItemId() == R.id.home) {
                    Toast.makeText(Home.this, "You are already in home!", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.forecast) {
                    dataPasser.goToAnotherActivity(ForecastActivity.class);
                } else if (item.getItemId() == R.id.search) {
                    dataPasser.goToAnotherActivity(searchActivity.class);
                }
                return false;
            }
        });
    }
    private void assignComponents(){
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
        internetConnectionAnimation=(GifImageView) findViewById(R.id.internet_connection_animation_home);

        bar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bar.setSelectedItemId(R.id.home);
        retryButton=(Button) findViewById(R.id.retryBtn);
    }
    private void NoInternetConnectionAction(){
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
        internetConnectionAnimation.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }
    private void loadJson(){

        Ion.with(Home.this).load(staticTexts.dailyDataURL(city)).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null){
                            Toast.makeText(Home.this,"Server Error",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Home.this,"Server Error",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }
    private void modifyComponents(Weather w){
        country=w.getCountry();
        locationTextView.setText(w.getCityName()+", "+w.getCountry());
        dataTextView.setText(w.getDate());
        Ion.with(this).load(w.getIconResource()).intoImageView(WeatherStatueIcon);
        tempTextView.setText((w.getTempWithC()));
        statueTextView.setText(w.getDesc());
        HumidityTextView.setText(""+w.getHumidity());
        windDegreeTextView.setText(""+w.getWindDegree());
        windSpeedTextView.setText(""+w.getWindSpeed());
    }

    @Override
    public void onBackPressed() {
        String sender =getIntent().getStringExtra("sender");
        if (sender != null){
            super.onBackPressed();
        }
//        else {
//            if(sender.equals(staticTexts.FROM_SEARCH_ACTIVITY)){
//                goToForecastActivity();
//            }else if(sender.equals(staticTexts.FROM_SEARCH_ACTIVITY)){
//                goToSearchActivity();
//            }
//        }
    }
}