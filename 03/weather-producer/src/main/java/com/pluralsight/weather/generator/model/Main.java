package com.pluralsight.weather.generator.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Main {

    @SerializedName("temp")
    private Float temp;

    @SerializedName("pressure")
    private Integer pressure;

    @SerializedName("humidity")
    private Integer humidity;

    @SerializedName("temp_min")
    private Float tempMin;

    @SerializedName("temp_max")
    private Float tempMax;
}
