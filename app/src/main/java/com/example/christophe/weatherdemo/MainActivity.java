package com.example.christophe.weatherdemo;

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

import java.util.Locale;
import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {

    Button continueBtn;
    EditText editTextUsername;
    SharedPreferences sharedPreferences;
    SharedPreferences nameSet;
    private static final int MY_PERMISSION_REQUEST_LOCATION=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameSet=getApplication().getSharedPreferences("SaveName", MODE_PRIVATE);
        String checkName=nameSet.getString("Value","0");
        if(checkName!="0"){

            Intent next=new Intent(MainActivity.this,weatherDemo.class);
            startActivity(next);

        }



        continueBtn=(Button) findViewById(R.id.btn_Continue);
        editTextUsername=(EditText) findViewById(R.id.editText_Name);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences=getSharedPreferences("SaveName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Value",editTextUsername.getText().toString());
                editor.apply();
                if(editTextUsername.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your name",
                            Toast.LENGTH_LONG).show();
                }else {

                    Intent myIntent = new Intent(MainActivity.this,
                            weatherDemo.class);
                    startActivity(myIntent);
                }

            }
        });
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
        }else{
            LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            try{

                getlocation(location.getLatitude(),location.getLongitude());

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"Not found", Toast.LENGTH_SHORT).show();

            }

        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){

            case MY_PERMISSION_REQUEST_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED){
                        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{


                            getlocation(location.getLatitude(),location.getLongitude());

                        }catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Not found", Toast.LENGTH_SHORT).show();

                        }


                    }


                }
            }
        }
    }

    public String getlocation(double lat,double lon){
        String curentCity="";

        Geocoder geocoder =new Geocoder(MainActivity.this, Locale.getDefault());
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
