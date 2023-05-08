package com.nuuly.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuuly.data.Constants;
import com.nuuly.data.model.PurchaseItem;
import com.nuuly.db.models.Inventory;
import com.nuuly.db.InventoryRepository;
import com.nuuly.data.exception.InvalidParameterException;
import com.nuuly.data.exception.InventoryNotFoundException;
import com.nuuly.data.response.InventoryResponse;
import com.nuuly.data.response.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProducerService producerService;


    /**
     * Helper method to validate the request data.
     *
     * @param sku
     * @param quantity
     */
    private void validateRequestData(String sku, int quantity){
        //Validate Data - return 400 and message
        if(!StringUtils.hasLength(sku)){
            throw new InvalidParameterException("sku cannot be blank");
        }
        if (quantity < 1){
            throw new InvalidParameterException("receiptAmount has to be greater than 0");
        }
    }

    /**
     * Method to add sku items to the inventory table. If the item already exists the count of the sku will increase
     * by the receiptAmount.
     *
     * @param sku String Sku value.
     * @param receiptAmount int Quantity of Sku being added to inventory
     * @return InventoryResponse returns sku and total count from inventory
     */
    public InventoryResponse addInventory(String sku, int receiptAmount) {
        validateRequestData(sku, receiptAmount);

        //Check if we already have existing inventory for sku
        Inventory inventory = inventoryRepository.findBySku(sku);
        if(inventory == null) {
            inventory = new Inventory(sku, 0);
        }
        inventory.setCount(inventory.getCount()+receiptAmount);

        // Persist Data to postgres
        inventoryRepository.save(inventory);

        // Return 201 after postgres save
        return new InventoryResponse(inventory);
    }


    /**
     * Method to reduce the count of Inventory given a list of items. This method also produces message to the
     * Inventory topic for each successful item purchased.
     *
     * @param items
     * @return PurchaseResponse Contains two lists. One for successful purchases and one for failures. Each item has a
     * status value to better understand the item failed.
     */
    public PurchaseResponse purchaseInventory(List<PurchaseItem> items) {
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        ObjectMapper om = new ObjectMapper();
        for(PurchaseItem item : items){
            try{
                if(purchaseItem(item)){
                    //Add item to response
                    purchaseResponse.addSuccess(item);
                    producerService.sendInventoryMessage(Constants.PURCHASE_KEY, om.writeValueAsString(item));
                }
            }catch (ResponseStatusException e){
                //Add item to error response
                purchaseResponse.addFailure(item, e);
            }catch (Exception e){
                purchaseResponse.addFailure(item, e);
            }
        }

        return purchaseResponse;
    }

    /**
     * Helper method that decrements the count in the Inventory table.
     *
     * @param item
     * @return boolean true if the item is purchased
     * @throws InventoryNotFoundException when sku is not found in the inventory table
     * @throws ResponseStatusException when there is not enough count of items in the db to be purchased.
     */
    private boolean purchaseItem(PurchaseItem item){
        validateRequestData(item.getSku(), item.getAmount());

        Inventory inventory =  inventoryRepository.findBySku(item.getSku());
        if(inventory == null){
            throw new InventoryNotFoundException(item.getSku());
        }
        if(inventory.getCount() < item.getAmount()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        inventory.setCount(inventory.getCount() - item.getAmount());

        inventoryRepository.save(inventory);
        return true;
    }
}
