package com.example.christophe.weatherdemo.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChangeBackgroundColor {
    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;

    /*this function is for constraintLayout*/
    public void ChnageBgColorBasedOnTime(ConstraintLayout li){
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
    }
    /*end*/
}
