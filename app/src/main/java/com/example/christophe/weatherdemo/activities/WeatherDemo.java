package com.example.christophe.weatherdemo.activities;


import android.content.Intent;
import android.content.SharedPreferences;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christophe.weatherdemo.R;
import com.example.christophe.weatherdemo.adapters.RecyclerAdapter;
import com.example.christophe.weatherdemo.rest.ApiClient;
import com.example.christophe.weatherdemo.rest.OpenweatherApi;
import com.example.christophe.weatherdemo.models.JsonHead;
import com.example.christophe.weatherdemo.utils.ChangeBackgroundColor;
import com.example.christophe.weatherdemo.utils.GetLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDemo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle menuToggle;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private OpenweatherApi openweatherApi;
    private final static String API_KEY = "";
    TextView readName;
    SharedPreferences usernameSharedPreferences;
    String  cityName="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_demo);


        /*Drawer and actionbar config*/
        menuDrawer=(DrawerLayout)findViewById(R.id.drawer_menu) ;
        menuToggle=new ActionBarDrawerToggle(this,menuDrawer,R.string.open,R.string.close);
        menuToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));
        menuDrawer.addDrawerListener(menuToggle);
        menuToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Home</font>"));
        NavigationView navigationView= findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        /*end*/

        /*Initialise Recycler Viewer */
        recyclerView= findViewById(R.id.forecastViewer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        /*end*/

        //change backgroundColor based on current time
        ConstraintLayout li=(ConstraintLayout)findViewById(R.id.dynamicLayout);
        ChangeBackgroundColor backgroundColor=new ChangeBackgroundColor();
        backgroundColor.ChnageBgColorBasedOnTime(li);
        //end

        /*read username stored in SharedPreferences*/
        readName=(TextView)findViewById(R.id.textViewName);
        usernameSharedPreferences=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
        String userName=usernameSharedPreferences.getString("Value","Data Not Found");
        /*end*/

        /*get location */
        GetLocation getLocation=new GetLocation();
        cityName =getLocation.checkPermissionAndGetLocation(WeatherDemo.this);
        if(cityName!=null){
            readName.setText("Hello "+userName+",here's the weather for "
                    + cityName);
        }else
        readName.setText("Hello "+userName+",here's the weather for Beirut"
                );
        /*end get location */


        /*check if Api key exists*/
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from openweathermap.org", Toast.LENGTH_LONG).show();
            return;
        }
        /*end check if Api key exists*/



        try {
            /*call retrofit weather Api*/
            openweatherApi = ApiClient.getClient().create(OpenweatherApi.class);

        /*check if network provider finds city if not take beirut as default*/
        if(cityName!=null)
            cityName =getLocation.checkPermissionAndGetLocation(WeatherDemo.this);
        else
            cityName="beirut";
        /*end*/


            Call<JsonHead> apiCall = openweatherApi.getWeatherInfo(cityName,API_KEY);


            apiCall.enqueue(new Callback<JsonHead>() {
                @Override
                public void onResponse(Call<JsonHead> call, Response<JsonHead> response) {
                    int statusCode = response.code();//success
                    JsonHead tempData = response.body();


                    adapter = new RecyclerAdapter(tempData);
                    recyclerView.setAdapter(adapter);


                    Log.d("temp", "onResponse:" + statusCode);


                }

                @Override
                public void onFailure(Call<JsonHead> call, Throwable t) {
                    Log.d("temp", "onResponse:" + t.getMessage());


                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }




    }
    /*end call retrofit weather Api*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        readName=(TextView)findViewById(R.id.textViewName);
        usernameSharedPreferences=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
        String userName=usernameSharedPreferences.getString("Value","Data Not Found");
        GetLocation getLocation=new GetLocation();
        cityName =getLocation.checkPermissionAndGetLocation(WeatherDemo.this);
        if(cityName!=null){
            readName.setText("Hello "+userName+",here's the weather for "
                    + cityName);
        }else
            readName.setText("Hello "+userName+",here's the weather for Beirut"
            );

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
            usernameSharedPreferences.edit().remove("Value").commit();
            Intent myIntent = new Intent(WeatherDemo.this,MainActivity.class
            );
            startActivity(myIntent);
        }
        return false;
    }




}



