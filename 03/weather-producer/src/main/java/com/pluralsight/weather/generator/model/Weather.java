package com.pluralsight.weather.generator.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Weather {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("weather")
    private List<WeatherDetails> weatherDetails;

    @SerializedName("main")
    private Main main;

    public Weather() {}

    public Weather(Long id, String name, List<WeatherDetails> weatherDetails, Main main) {
        this.id = id;
        this.name = name;
        this.weatherDetails = weatherDetails;
        this.main = main;
    }
}
