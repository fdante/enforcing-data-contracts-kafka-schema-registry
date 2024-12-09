package com.pluralsight.weather;

import com.pluralsight.avro.weather.Main;
import com.pluralsight.avro.weather.WeatherDetails;
import com.pluralsight.weather.generator.WeatherAPIClient;
import com.pluralsight.weather.generator.model.Weather;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class WeatherProducer {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherProducer.class);
    private static final String WEATHER_TOPIC = "weather";
    private static final String CITY = "Amsterdam";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);
        Thread shutdownHook = new Thread(producer::close);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        while(true) {
            Weather currentWeather = WeatherAPIClient.getCurrentWeather(CITY);

            byte[] value = serializeWeatherInformation(currentWeather);

            LOG.info("Sending to Kafka on the " + WEATHER_TOPIC + " topic the following message " + CITY + " : " + currentWeather);

            ProducerRecord<String, byte[]> producerRecord =
                    new ProducerRecord<>(WEATHER_TOPIC, CITY, value);
            producer.send(producerRecord);
            Thread.sleep(1000);
        }
    }


    public static byte[] serializeWeatherInformation(Weather weather) throws IOException {
        com.pluralsight.avro.weather.Weather weatherAvro = com.pluralsight.avro.weather.Weather.newBuilder()
          .setId(weather.getId())
          .setName(weather.getName())
          .setMain(buildAvroMain(weather))
          .setWeatherDetailsList(buildAvroWeatherDetails(weather))
          .build();

        return weatherAvro.toByteBuffer()
          .array();
    }

    public static Main buildAvroMain(Weather weather) {
        return Main.newBuilder()
          .setHumidity(weather.getMain().getHumidity())
          .setPressure(weather.getMain().getPressure())
          .setTemp(weather.getMain().getTemp())
          .setTempMax(weather.getMain().getTempMax())
          .setTempMin(weather.getMain().getTempMin())
          .build();
    }

    public static List<WeatherDetails> buildAvroWeatherDetails(Weather weather) {
        return weather.getWeatherDetails()
          .stream()
          .map( weatherDetails -> WeatherDetails.newBuilder()
            .setId(weatherDetails.getId())
            .setMain(weatherDetails.getMain())
            .setDescription(weatherDetails.getDescription())
            .setIcon(weatherDetails.getIcon())
            .build())
          .collect(Collectors.toList());
    }
}
