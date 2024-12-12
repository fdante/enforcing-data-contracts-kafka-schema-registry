package com.pluralsight.weather.generator.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class InternalWeatherModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("weather")
    private List<WeatherDetails> weatherDetails;

    @SerializedName("main")
    private Main main;
}
