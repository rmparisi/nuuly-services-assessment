package com.nuuly.db;


import com.nuuly.db.models.Favorites;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends CrudRepository<Favorites, String> {

}
