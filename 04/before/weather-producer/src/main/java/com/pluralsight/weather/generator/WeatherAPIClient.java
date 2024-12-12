package com.pluralsight.weather.generator;

import com.pluralsight.weather.generator.model.InternalWeatherModel;
import kong.unirest.Unirest;

public class WeatherAPIClient {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "1ac3b40b5e7889ad2e698f84a9b274b7";

    public static InternalWeatherModel getCurrentWeather(String city) {
        return Unirest.get(BASE_URL)
                .queryString("q", city)
                .queryString("APPID", API_KEY)
                .asObject(InternalWeatherModel.class)
                .getBody();
    }
}
