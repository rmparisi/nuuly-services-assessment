package com.nuuly.db.models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This table tracks the item (SKU) currently in inventory and the number of items in inventory.
 */
@Entity
public class Inventory {

    @Id
    private String sku;
    private int count;

    protected Inventory() {}

    public Inventory(String sku, int count) {
        this.sku = sku;
        this.count = count;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format(
                "Inventory[sku='%s', amount=%d]",
                sku, count
        );
    }
}

