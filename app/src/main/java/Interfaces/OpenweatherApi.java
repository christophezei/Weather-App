package Interfaces;



import java.util.List;

import Model.Main;
import Model.jsonHead;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenweatherApi {
    @GET("data/2.5/forecast")

    Call<jsonHead> getWeatherInfo(
            @Query("q") String cityName,
            @Query("appid") String appid
    );

}
