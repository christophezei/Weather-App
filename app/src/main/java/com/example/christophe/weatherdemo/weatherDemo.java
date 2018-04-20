package com.example.christophe.weatherdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Interfaces.OpenweatherApi;
import Model.Main;
import Model.jsonHead;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class weatherDemo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView readName;
    SharedPreferences name;

    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle menuToggle;
    private static final int MY_PERMISSION_REQUEST_LOCATION=1;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private OpenweatherApi openweatherApi;
    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_demo);
        readName=(TextView)findViewById(R.id.textViewName);
        
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.dynamicLayout);
        menuDrawer=(DrawerLayout)findViewById(R.id.drawer_menu) ;
        menuToggle=new ActionBarDrawerToggle(this,menuDrawer,R.string.open,R.string.close);
        menuDrawer.addDrawerListener(menuToggle);
        menuToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView=(RecyclerView)findViewById(R.id.forecastViewer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //change backgroundColor based on current time
        calander = Calendar.getInstance();
        simpleDateFormat  = new SimpleDateFormat("HH:mm:ss");
        time = simpleDateFormat.format(calander.getTime());

        int currHour=Integer.parseInt(time.substring(0,2));
        int night=19;


        if(currHour>night){
           //set black background
            li.setBackgroundColor(Color.parseColor("#1f263b"));

        }else{
            //set white background
            li.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        //end













        name=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
        String userName=name.getString("Value","Data Not Found");

        if(ContextCompat.checkSelfPermission(weatherDemo.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(weatherDemo.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(weatherDemo.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }else
            {
                ActivityCompat.requestPermissions(weatherDemo.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
        }else{
            LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            try{

                readName.setText("Hello "+userName+",here's the weather for "+getlocation(location.getLatitude(),location.getLongitude()));

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(weatherDemo.this,"Not found", Toast.LENGTH_SHORT).show();

            }

        }

        //retrofit builder
        Gson gson=new GsonBuilder().create();
        Retrofit retrofit= new Retrofit.Builder().
                                baseUrl("http://api.openweathermap.org/")
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
        openweatherApi =retrofit.create(OpenweatherApi.class);
        try {
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String  cityName =getlocation(location.getLatitude(),location.getLongitude());
        if(cityName!=null)
            cityName =getlocation(location.getLatitude(),location.getLongitude());
        else
            cityName="beirut";
        String Api_KEEY="992eabcc63d60343e0bb8ebc03214ba2";

            Call<jsonHead> tempCall = openweatherApi.getWeatherInfo(cityName, Api_KEEY);


            tempCall.enqueue(new Callback<jsonHead>() {
                @Override
                public void onResponse(Call<jsonHead> call, Response<jsonHead> response) {
                    int statusCode = response.code();//success
                    jsonHead tempData = response.body();


                    adapter = new RecyclerAdapter(tempData);
                    recyclerView.setAdapter(adapter);


                    Log.d("temp", "onResponse:" + statusCode);


                }

                @Override
                public void onFailure(Call<jsonHead> call, Throwable t) {
                    Log.d("temp", "onResponse:" + t.getMessage());


                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       switch (requestCode){

           case MY_PERMISSION_REQUEST_LOCATION:{
               if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   if(ContextCompat.checkSelfPermission(weatherDemo.this,
                           Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED){
                       LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                       Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                       try{
                           name=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
                           String userName=name.getString("Value","Data Not Found");
                           readName.setText("Hello "+userName+",here's the weather for "+getlocation(location.getLatitude(),location.getLongitude()));

                       }catch(Exception e){
                           e.printStackTrace();
                           Toast.makeText(weatherDemo.this,"Not found", Toast.LENGTH_SHORT).show();

                       }


                   }


               }
           }
       }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(menuToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout){
            name.edit().remove("Value").commit();
            Intent myIntent = new Intent(weatherDemo.this,MainActivity.class
            );
            startActivity(myIntent);
        }
        return false;
    }

    public String getlocation(double lat,double lon){
        String curentCity="";

        Geocoder geocoder =new Geocoder(weatherDemo.this, Locale.getDefault());
        java.util.List<Address> addressList ;
        try{
            addressList=geocoder.getFromLocation(lat,lon,1);
            if (addressList!=null & addressList.size()>0){
                curentCity=addressList.get(0).getLocality();


            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return curentCity;

    }


}
