package com.nuuly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is an example ProducerService that can publish a message to a topic. This can be used if it is autowired to
 * another class.
 * An example of using this is as follows:
 *
 *      <pre>
 *      this.producer.sendInventoryMessage(
 *          key = key,
 *          value = value
 *     )
 *     </pre>
 */
@Service
public class ProducerService {
    private final KafkaProducerService<String> kafkaProducerService;

    final String INVENTORY_TOPIC = "inventory_updates";

    @Autowired
    public ProducerService(KafkaProducerService<String> kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendInventoryMessage(String key, String message) throws Exception {
        kafkaProducerService.sendMessage(INVENTORY_TOPIC, key, message);
    }
}
