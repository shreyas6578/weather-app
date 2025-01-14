package com.example.wheatherapp;

import com.example.wheatherapp.Root;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api_call {
    @GET("weather")
    Call<Root> getWeather(
            @Query("q") String city,
            @Query("appid") String apiKey
    );
}
