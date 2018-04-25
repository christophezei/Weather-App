package com.example.christophe.weatherdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.Locale;

public class GetLocation {

    private static final int MY_PERMISSION_REQUEST_LOCATION=1;

    public String getlocation(double lat,double lon,Context mContext){
        String curentCity="";

        Geocoder geocoder =new Geocoder(mContext, Locale.getDefault());
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

    public String checkPermissionAndGetLocation(Context mContext){
        String cityLocation="";
        if(ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }else
            {
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
        }else{
            LocationManager locationManager=(LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            try{

               cityLocation=getlocation(location.getLatitude(),location.getLongitude(),mContext);

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(mContext,"Not found", Toast.LENGTH_SHORT).show();

            }

        }


        return cityLocation;
    }
    }



