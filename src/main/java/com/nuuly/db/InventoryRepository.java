package com.nuuly.db;

import com.nuuly.db.models.Inventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, String> {

    Inventory findBySku(String sku);
}

