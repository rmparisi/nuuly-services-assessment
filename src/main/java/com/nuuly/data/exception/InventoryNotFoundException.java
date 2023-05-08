package com.nuuly.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryNotFoundException extends ResponseStatusException {

    public InventoryNotFoundException(String sku) {
        super(HttpStatus.NOT_FOUND, "Sku ("+sku+") not found");
    }

}
