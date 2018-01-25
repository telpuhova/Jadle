package dao;


import models.Review;

import java.util.List;

public interface ReviewDao {

    //create
    void add(Review review);

    //read
    List<Review> getAll();

    List<Review> getAllReviewsByRestaurant(int restaurantId);

    List<Review> getAllReviewsByRestaurantSortedNewestToOldest (int restaurantId);

    //update

    //delete
    void deleteById(int id);
}
