package com.nuuly.data.response;

import com.nuuly.data.model.PurchaseItem;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseResponse {

    private String timeStamp;

    List<PurchaseResponseItem> purchased = new ArrayList<PurchaseResponseItem>();
    List<PurchaseResponseItem> errors = new ArrayList<PurchaseResponseItem>();

    public void addSuccess(PurchaseItem item) {
        purchased.add(new PurchaseResponseItem(item));
    }

    public void addFailure(PurchaseItem item, ResponseStatusException e) {
        errors.add(new PurchaseResponseItem(item, e));
    }

    public void addFailure(PurchaseItem item, Exception e) {
        errors.add(new PurchaseResponseItem(item, e));
    }

    public HttpStatus getHttpStatus() {
        if (errors.size() == 0) {
            return HttpStatus.OK;
        } else if(purchased.size() > 0){
            return HttpStatus.MULTI_STATUS;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ssZ");
        return simpleDateFormat.format(new Date());
    }

    public List<PurchaseResponseItem> getPurchased() {
        return purchased;
    }

    public List<PurchaseResponseItem> getErrors() {
        return errors;
    }

    private class PurchaseResponseItem {
        int status;
        String sku;
        int quantity;
        public PurchaseResponseItem(PurchaseItem item) {
            this.status = 200;
            this.sku = item.getSku();
            this.quantity = item.getAmount();
        }

        public PurchaseResponseItem(PurchaseItem item, ResponseStatusException ex) {
            this.status = ex.getRawStatusCode();
            this.sku = item.getSku();
            this.quantity = item.getAmount();
        }

        public PurchaseResponseItem(PurchaseItem item, Exception ex) {
            this.status = 500;
            this.sku = item.getSku();
            this.quantity = item.getAmount();
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
}
