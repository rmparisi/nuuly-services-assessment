package com.nuuly.data.response;

import com.nuuly.db.models.Inventory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InventoryResponse {

    private String timeStamp;

    private String sku;

    private int quantity;

    public InventoryResponse(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public InventoryResponse(Inventory inventory){
        this.sku = inventory.getSku();
        this.quantity = inventory.getCount();
    }

    public String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ssZ");
        return simpleDateFormat.format(new Date());
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
