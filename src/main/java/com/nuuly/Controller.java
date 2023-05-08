package com.nuuly;

import com.nuuly.data.model.PurchaseItem;
import com.nuuly.data.response.FavoritesResponse;
import com.nuuly.data.response.InventoryResponse;
import com.nuuly.data.response.PurchaseResponse;
import com.nuuly.service.FavoritesService;
import com.nuuly.service.InventoryService;
import com.nuuly.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is our base API server to run HTTP requests against. Currently, these are set up to just return HTTP responses
 * and not actually do anything. Your goal is to make changes here using the rest of the example code to complete the
 * objectives.
 */
@RestController
public class Controller {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProducerService producer;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    public Controller(ProducerService producer) {
        this.producer = producer;
    }

    /**
     * When garments are ready to be sold, a purchase order is issued to a seller that a certain
     * agreed upon quantity of a product is wanted. These products get allocated into our inventory.
     *
     * @param sku: The stock keeping unit as alphanumeric digits assigned to a product
     * @param receiptAmount: The number of SKUs that are received by this purchase order
     * @return That the purchase order was created
     */
    @PostMapping("/create")
    @ResponseBody
    public InventoryResponse createPurchaseOrder(
            @RequestParam("sku") String sku,
            @RequestParam("receiptAmount") int receiptAmount
    ) {
        return inventoryService.addInventory(sku, receiptAmount);
    }

    /**
     * When a garment is actually purchased by us, we want to decrement the inventory to represent that the item was
     * purchased. This method takes a JSON array in the request body to process multiple purchases at once.
     *
     * If a garment is not able to be purchased it will be logged and the endpoint will continue with the next time.
     *
     * @param items: JSON array of PurchaseItems, EX: [{
     * 			"sku": "abc123",
     * 			"amount": 1
     *                },
     *        {
     * 			"sku": "abc1234",
     * 			"amount": 1
     *        }
     * 	]
     * @return A list of successful and failed purchases. Each failure indicates why that purchase failed via the status
     * code.
     */
    @PostMapping("/purchase")
    @ResponseBody
    public ResponseEntity purchase(
            @RequestBody List<PurchaseItem> items
    ) {
        PurchaseResponse pr = inventoryService.purchaseInventory(items);
        return new ResponseEntity<PurchaseResponse>(pr, pr.getHttpStatus()) ;
    }

    /**
     * From a business perspective, we want to understand what our customers like and don't like. We want to get a list
     * of favorite items ranked by how many were purchased. This data only includes items that have been purchased.
     *
     * @return Lists of the 3 most and least favorite items
     */
    @GetMapping("/favorites")
    @ResponseBody
    public FavoritesResponse favorites() {
        return favoritesService.getFavorites();
    }
}
