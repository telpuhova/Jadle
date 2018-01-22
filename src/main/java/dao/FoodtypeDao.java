package dao;


import models.Foodtype;
import models.Restaurant;

import java.util.List;

public interface FoodtypeDao {
    //create
    void add(Foodtype foodtype);

    //read
    //Foodtype findById(int id);

    List<Foodtype> getAll();

    //List<Restaurant> getAllRestaurantsForAFoodtype(int id);

    //update

    //delete
    void deleteById(int id);
}
