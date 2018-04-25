package com.example.christophe.weatherdemo.rest;



import com.example.christophe.weatherdemo.models.JsonHead;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenweatherApi {
    @GET("data/2.5/forecast")

    Call<JsonHead> getWeatherInfo(
            @Query("q") String cityName,
            @Query("appid") String appid
    );


}
