package dao;


import models.Foodtype;
import models.Restaurant;

import java.util.List;

public interface FoodtypeDao {
    //create
    void add(Foodtype foodtype);
    void addFoodtypeToRestaurant(Foodtype foodtype, Restaurant restaurant); // sets Many-to-Many relationship and write to the join table

    //read
    Foodtype findById(int id);

    List<Foodtype> getAll();
    List<Restaurant> getAllRestaurantsForAFoodtype(int id); //gets Many-to-Many relationship and retrieves info from join table

    //List<Restaurant> getAllRestaurantsForAFoodtype(int id);

    //update

    //delete
    void deleteById(int id);
}
