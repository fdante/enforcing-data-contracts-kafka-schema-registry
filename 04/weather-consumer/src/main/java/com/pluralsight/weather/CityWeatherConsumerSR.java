
package com.pluralsight.weather;

import com.pluralsight.avro.weather.City;
import com.pluralsight.avro.weather.Weather;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CityWeatherConsumerSR {

  private static final Logger LOG = LoggerFactory.getLogger(WeatherConsumerSR.class);
  private static final String WEATHER_TOPIC = "city-weather-sr";

  public static void main(String[] args) throws IOException {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "city.weather.consumer.sr");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

    KafkaConsumer<City, Weather> consumer = new KafkaConsumer<>(props);

    Thread shutdownHook = new Thread(consumer::close);
    Runtime.getRuntime().addShutdownHook(shutdownHook);

    consumer.subscribe(Collections.singletonList(WEATHER_TOPIC));

    while (true) {
      ConsumerRecords<City, Weather> records = consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<City, Weather> record : records) {
        Weather weather = record.value();

        LOG.info("Consumed message: \n" + record.key().toString() + " : " + weather.toString());
      }
    }
  }
}

