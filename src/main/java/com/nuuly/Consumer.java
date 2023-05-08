package com.nuuly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuuly.data.Constants;
import com.nuuly.data.model.PurchaseItem;
import com.nuuly.db.FavoritesRepository;
import com.nuuly.db.models.Favorites;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * This class will consume the messages that are produced on the given topic.
 * It will then perform some action on that message.
 */
@Component
public class Consumer {

    Logger logger = LogManager.getLogger(getClass());

    final String INVENTORY_TOPIC = "inventory_updates";

    @Autowired
    private FavoritesRepository favoritesRepository;

    @KafkaListener(topics = INVENTORY_TOPIC)
    public void processMessage(ConsumerRecord<String, String> content) {
        if(content.key().equals(Constants.PURCHASE_KEY)){
            processPurchaseMessage(content.value());
        }
    }

    /**
     * Method to Process a Purchase Event. This method takes the purchase event
     * and increments the Favorites row with the amount of purchased items. If
     * an exception is thrown we will log it to the error logs.
     * @param value
     */
    private void processPurchaseMessage(String value) {
        ObjectMapper om = new ObjectMapper();
        try {
            //deserialize
            PurchaseItem item = om.convertValue(value, PurchaseItem.class);
            // Get current row value
            Optional<Favorites> favoritesOptional = favoritesRepository.findById(item.getSku());
            Favorites favorites = favoritesOptional.orElse(new Favorites(item.getSku(), 0));
            // Increment count by amount purchased
            favorites.setCount(favorites.getCount()+item.getAmount());
            // Persist to DB
            favoritesRepository.save(favorites);
        } catch (Exception e){
            logger.error("Purchase Message Failure - Message Value: {}\nException: {}", value, e.getMessage());
        }

    }
}
