
import com.google.gson.Gson;
import dao.Sql2oFoodtypeDao;
import dao.Sql2oRestaurantDao;
import dao.Sql2oReviewDao;
import exceptions.ApiException;
import models.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;



public class App {

    public static void main(String[] args) {
        Sql2oFoodtypeDao foodtypeDao;
        Sql2oRestaurantDao restaurantDao;
        Sql2oReviewDao reviewDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:h2:~/jadle.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");

        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        conn = sql2o.open();

        // Restaurant

        post("/restaurants/new", "application/json", (req, res) -> {
            Restaurant restaurant = gson.fromJson(req.body(), Restaurant.class);
            restaurantDao.add(restaurant);
            res.status(201);
            return gson.toJson(restaurant);
        });

        get("/restaurants", "application/json", (req, res) -> {
            return gson.toJson(restaurantDao.getAll());
        });

        get("/restaurants/:id", "application/json", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params("id"));

            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);

            if (restaurantToFind == null) {
                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }

            return gson.toJson(restaurantDao.findById(restaurantId));
        });

        // Foodtype

        post("/foodtypes/new", "application/json", (req, res) -> {
            Foodtype foodtype = gson.fromJson(req.body(), Foodtype.class);
            foodtypeDao.add(foodtype);
            res.status(201);
            return gson.toJson(foodtype);
        });

        get("/foodtypes", "application/json", (request, response) -> {
            return gson.toJson(foodtypeDao.getAll());
        });

        post("/restaurant/:id/foodtype/:foodtypeId", "application/json", (request, response) -> {
            int restaurantId = Integer.parseInt(request.params("id"));
            int foodtypeId = Integer.parseInt(request.params("foodtypeId"));

            Restaurant restaurant = restaurantDao.findById(restaurantId);
            Foodtype foodtype = foodtypeDao.findById(foodtypeId);

            if (restaurant != null && foodtype != null) {
                foodtypeDao.addFoodtypeToRestaurant(foodtype,restaurant);
                response.status(201);
                return gson.toJson(String.format("Restaurant '%s' and FoodType '%s' have been associated", foodtype.getName(),restaurant.getName()));
            }
            else {
                throw new ApiException(404,String.format("Restaurant or Foodtype does not exist"));
            }

        });

        get("/foodtypes/:id/restaurants", "application/json", (req, res) -> {
            int foodtypeId = Integer.parseInt(req.params("id"));
            Foodtype foodtype = foodtypeDao.findById(foodtypeId);
            if (foodtype == null) {
                throw new ApiException(404, String.format("No foodtype with the id: %s exists", req.params("id")));
            }
            else if (foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId).size()==0){
                return "{\"message\":\"I'm sorry, but no restaurants are listed for this foodtype.\"}";
            }
            else {
                return gson.toJson(foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId));
            }
        });

        get("/restaurants/:id/foodtypes", "application/json", (request, response) -> {
            int restaurantId = Integer.parseInt(request.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            if (restaurantToFind == null) {
                throw new ApiException(404, String.format("No restaurant with the id: %s exists", request.params("id")));
            } else if (restaurantDao.getAllFoodtypesForARestaurant(restaurantId).size()==0){
                return "{\"message\":\"I'm sorry, but no foodtyes are listed for this restaurant.\"}";
            }
            else {
                return gson.toJson(restaurantDao.getAllFoodtypesForARestaurant(restaurantId));
            }
        });




        //Reviews


        post("restaurants/:restaurantId/reviews/new", "application/json", (req, res) -> {
            int restaurantId =Integer.parseInt(req.params("restaurantId"));
            Review review = gson.fromJson(req.body(), Review.class);
            review.setCreatedat();
            review.setFormattedCreatedAt();
            review.setRestaurantId(restaurantId);
            reviewDao.add(review);
            res.status(201);
            return gson.toJson(review);
        });

        get("/reviews", "application/json", (request, response) -> {

            return gson.toJson(reviewDao.getAll());
        });

        get("/restaurant/:id/reviews", "application/json", (request, response) -> {
            int restaurantId = Integer.parseInt(request.params("id"));

            Restaurant restaurant = restaurantDao.findById(restaurantId);
            List<Review> reviews = reviewDao.getAllReviewsByRestaurant(restaurantId);

            return gson.toJson(reviews);

        });

        get("/restaurants/:id/sortedReviews", "application/json", (req,res) -> {
            int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            List<Review> allReviews;
            if (restaurantToFind == null){
                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", restaurantId));
            }
            allReviews = reviewDao.getAllReviewsByRestaurantSortedNewestToOldest(restaurantId);
            return gson.toJson(allReviews);
        });

        get("reviews/:id/delete", "application/json", (req, res) -> {
            int reviewId = Integer.parseInt(req.params("id"));
            reviewDao.deleteById(reviewId);
            return 0;
        });

        get("/foodtypes/:id/delete", "application/json", (request, response) -> {
            int foodtypeId = Integer.parseInt(request.params("id"));
            foodtypeDao.deleteById(foodtypeId);
            String str = "success";
            return gson.toJson(str);
        });



        //exception
        exception(ApiException.class, (exc, req, res) -> {
            ApiException err = (ApiException) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatusCode());
            res.body(gson.toJson(jsonMap));
        });




        //Filters
        after((req, res) -> {
            res.type("application/json");
        });



    }






}
