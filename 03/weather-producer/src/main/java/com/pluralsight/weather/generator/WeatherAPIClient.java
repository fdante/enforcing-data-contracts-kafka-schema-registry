package com.pluralsight.weather.generator;

import com.pluralsight.weather.generator.model.Weather;
import kong.unirest.Unirest;

public class WeatherAPIClient {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "b9fde981ab667a7b57a8747e0f941b1c";

    public static Weather getCurrentWeather(String city) {
        return Unirest.get(BASE_URL)
                .queryString("q", city)
                .queryString("APPID", API_KEY)
                .asObject(Weather.class)
                .getBody();
    }
}
