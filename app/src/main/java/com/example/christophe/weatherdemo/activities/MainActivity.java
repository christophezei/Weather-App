package com.example.christophe.weatherdemo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christophe.weatherdemo.R;
import com.example.christophe.weatherdemo.utils.GetLocation;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button continueBtn;
    EditText editTextUsername;
    SharedPreferences usernameSharedPreferences;
    SharedPreferences nameSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*read name from sharedPreferences*/
        nameSet=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
        String checkName=nameSet.getString("Value","0");
        /*end*/

        /*check if sharedPreference name exists if yes start a new activity*/
        if(checkName!="0"){

            Intent next=new Intent(MainActivity.this,WeatherDemo.class);
            startActivity(next);

        }
        /*end*/



        continueBtn=(Button) findViewById(R.id.btn_Continue);
        editTextUsername=(EditText) findViewById(R.id.editText_Name);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameSharedPreferences=getSharedPreferences("SaveName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=usernameSharedPreferences.edit();
                editor.putString("Value",editTextUsername.getText().toString());
                editor.apply();
                if(editTextUsername.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your name",
                            Toast.LENGTH_LONG).show();
                }else {

                    Intent myIntent = new Intent(MainActivity.this,
                            WeatherDemo.class);
                    startActivity(myIntent);
                }

            }
        });

        GetLocation getLocation=new GetLocation();
        getLocation.checkPermissionAndGetLocation(MainActivity.this);


    }





}
