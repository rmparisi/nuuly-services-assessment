package com.nuuly.data.response;

import com.nuuly.db.models.Favorites;

import java.util.ArrayList;
import java.util.List;

public class FavoritesResponse {

    List<Favorites> mostPurchased = new ArrayList<Favorites>();
    List<Favorites> leastPurchased = new ArrayList<Favorites>();

    public FavoritesResponse(List<Favorites> mostPurchased, List<Favorites> leastPurchased) {
        this.mostPurchased = mostPurchased;
        this.leastPurchased = leastPurchased;
    }

    public List<Favorites> getMostPurchased() {
        return mostPurchased;
    }

    public void setMostPurchased(List<Favorites> mostPurchased) {
        this.mostPurchased = mostPurchased;
    }

    public List<Favorites> getLeastPurchased() {
        return leastPurchased;
    }

    public void setLeastPurchased(List<Favorites> leastPurchased) {
        this.leastPurchased = leastPurchased;
    }
}
