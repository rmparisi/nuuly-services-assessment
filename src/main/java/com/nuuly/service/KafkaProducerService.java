package com.nuuly.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class KafkaProducerService<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String key, T value) throws Exception {
        ProducerRecord<String, T> producerRecord = producerRecord(topic, key, value);
        int PRODUCER_TIMEOUT = 30000;
        kafkaTemplate.send(producerRecord).get(PRODUCER_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private ProducerRecord<String, T> producerRecord(String topic, String key, T value) {
        return new ProducerRecord<>(topic, key, value);
    }
}