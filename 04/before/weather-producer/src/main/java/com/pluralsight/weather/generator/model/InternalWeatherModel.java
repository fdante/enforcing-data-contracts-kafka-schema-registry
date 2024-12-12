package com.pluralsight.weather.generator.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InternalWeatherModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("weather")
    private List<WeatherDetails> weatherDetails;

    @SerializedName("main")
    private Main main;


    public InternalWeatherModel() {}

    public InternalWeatherModel(Long id, String name, List<WeatherDetails> weatherDetails, Main main) {
        this.id = id;
        this.name = name;
        this.weatherDetails = weatherDetails;
        this.main = main;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeatherDetails> getWeatherDetails() {
        return weatherDetails;
    }

    public void setWeather(List<WeatherDetails> weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weatherDetails=" + weatherDetails +
                ", main=" + main +
                '}';
    }
}
