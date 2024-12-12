package com.pluralsight.weather;

import com.pluralsight.avro.weather.Weather;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class WeatherConsumerSR {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherConsumerSR.class);
    private static final String WEATHER_TOPIC = "weather-sr";

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "weather.consumer.sr");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        KafkaConsumer<String, Weather> consumer = new KafkaConsumer<>(props);

        Thread shutdownHook = new Thread(consumer::close);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        consumer.subscribe(Collections.singletonList(WEATHER_TOPIC));

        while(true) {
            ConsumerRecords<String, Weather> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, Weather> record : records) {
                Weather weather = record.value();

                LOG.info("Consumed message: \n" + record.key() + " : " + weather.toString());
            }
        }
    }
}
