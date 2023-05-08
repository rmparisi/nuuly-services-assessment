package com.nuuly.data.model;

public class PurchaseItem {

    private String sku;

    private int amount;

    public PurchaseItem() {
    }

    public PurchaseItem(String sku, int amount) {
        this.sku = sku;
        this.amount = amount;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
