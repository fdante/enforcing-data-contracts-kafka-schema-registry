
package com.pluralsight.weather;

import com.pluralsight.avro.weather.City;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.avro.generic.GenericRecord;
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

public class RecordNameStrategyConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(RecordNameStrategyConsumer.class);
  private static final String WEATHER_TOPIC = "city-weather-rns";

  public static void main(String[] args) throws IOException {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "city.weather.consumer.rns");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
    props.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class.getName());
    KafkaConsumer<City, GenericRecord> consumer = new KafkaConsumer<>(props);

    Thread shutdownHook = new Thread(consumer::close);
    Runtime.getRuntime().addShutdownHook(shutdownHook);

    consumer.subscribe(Collections.singletonList(WEATHER_TOPIC));

    while (true) {
      ConsumerRecords<City, GenericRecord> records = consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<City, GenericRecord> record : records) {
        GenericRecord genericRecord = record.value();

        LOG.info("Consumed message: \n" + record.key().toString() + " : " + genericRecord.toString());
      }
    }
  }
}

