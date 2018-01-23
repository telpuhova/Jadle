package dao;

import models.Foodtype;
import models.Restaurant;

import java.util.List;


public interface RestaurantDao {
    //create
    void add(Restaurant restaurant); //I
    void addRestaurantToFoodtype(Restaurant restaurant, Foodtype foodtype); // sets Many-to-Many relationship and write to the join table

    //read
    List<Restaurant> getAll();
    List<Foodtype> getAllFoodtypesForARestaurant(int restaurantId); //gets Many-to-Many relationship and retrieves info from join table

    Restaurant findById(int id);

    //update
    void update(int id, String name, String address, String zipcode, String phone, String website, String email);

    //delete
    void deleteById(int id);
}
