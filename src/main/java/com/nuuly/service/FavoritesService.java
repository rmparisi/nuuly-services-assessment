package com.nuuly.service;
//findTop3ByCount
import com.nuuly.data.response.FavoritesResponse;
import com.nuuly.db.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
public class FavoritesService {

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns the 3 most and least popular purchased items.
     *
     * Note: This only includes purchased items. Items that have never been purchased will not be in the least favorite
     * list.
     * @return FavoritesResponse
     */
    public FavoritesResponse getFavorites(){
        Query q = entityManager.createQuery("SELECT f FROM Favorites f ORDER BY f.count DESC");
        Query q2 =  entityManager.createQuery("SELECT f FROM Favorites f ORDER BY f.count ASC");
        return new FavoritesResponse(q.setMaxResults(3).getResultList(), q2.setMaxResults(3).getResultList());
    }
}
