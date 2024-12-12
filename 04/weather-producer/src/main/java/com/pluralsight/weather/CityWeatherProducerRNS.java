package com.pluralsight.weather;

import com.pluralsight.avro.weather.City;
import com.pluralsight.avro.weather.Main;
import com.pluralsight.avro.weather.Weather;
import com.pluralsight.avro.weather.WeatherDetails;
import com.pluralsight.weather.generator.WeatherAPIClient;
import com.pluralsight.weather.generator.model.InternalWeatherModel;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class CityWeatherProducerRNS {

  private static final Logger LOG = LoggerFactory.getLogger(CityWeatherProducerRNS.class);
  private static final String WEATHER_TOPIC = "city-weather-rns";
  private static final String CITY = "Amsterdam";

  public static void main(String[] args) throws IOException, InterruptedException {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    props.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class.getName());

    KafkaProducer<City, Weather> producer = new KafkaProducer<>(props);

    Thread shutdownHook = new Thread(producer::close);
    Runtime.getRuntime().addShutdownHook(shutdownHook);

    while (true) {
      InternalWeatherModel currentWeather = WeatherAPIClient.getCurrentWeather(CITY);

      City key = City.newBuilder()
        .setCity(CITY)
        .build();
      Weather value = serializeWeatherInformation(currentWeather);

      LOG.info("Sending to Kafka on the " + WEATHER_TOPIC + " topic the following message: \n" + CITY + " : "
               + currentWeather);

      ProducerRecord<City, Weather> producerRecord =
        new ProducerRecord<>(WEATHER_TOPIC, key, value);
      producer.send(producerRecord);

      Thread.sleep(1000);
    }
  }

  public static Weather serializeWeatherInformation(InternalWeatherModel weather) throws IOException {
    return Weather.newBuilder()
      .setId(weather.getId())
      .setName(weather.getName())
      .setMain(buildAvroMain(weather))
      .setWeatherDetailsList(buildAvroWeatherDetails(weather))
      .build();
  }

  public static Main buildAvroMain(InternalWeatherModel weather) {
    return Main.newBuilder()
      .setHumidity(weather.getMain().getHumidity())
      .setPressure(weather.getMain().getPressure())
      .setTemp(weather.getMain().getTemp())
      .setTempMax(weather.getMain().getTempMax())
      .setTempMin(weather.getMain().getTempMin())
      .build();
  }

  private static List<WeatherDetails> buildAvroWeatherDetails(InternalWeatherModel weather) {
    return weather.getWeatherDetails()
      .stream()
      .map(weatherDetails -> WeatherDetails.newBuilder()
        .setId(weatherDetails.getId())
        .setMain(weatherDetails.getMain())
        .setDescription(weatherDetails.getDescription())
        .setIcon(weatherDetails.getIcon())
        .build())
      .collect(Collectors.toList());
  }

}

