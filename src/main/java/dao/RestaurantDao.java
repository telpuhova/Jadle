package dao;

import models.Restaurant;

import java.util.List;


public interface RestaurantDao {
    //create
    void add(Restaurant restaurant); //I

    //read
    List<Restaurant> getAll();

    Restaurant findById(int id);

    //update
    void update(int id, String name, String address, String zipcode, String phone, String website, String email);

    //delete
    void deleteById(int id);
}
