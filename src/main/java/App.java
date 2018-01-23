
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

        get("/restaurant/:id/foodtype/:foodtypeId", "application/json", (request, response) -> {
            int restaurantId = Integer.parseInt(request.params("id"));
            int foodtypeId = Integer.parseInt(request.params("foodtypeId"));

            Restaurant restaurant = restaurantDao.findById(restaurantId);
            Foodtype foodtype = foodtypeDao.findById(foodtypeId);
            foodtypeDao.addFoodtypeToRestaurant(foodtype, restaurant);
            return gson.toJson(restaurant);

        });

        get("/restaurants/foodtypes/:foodtypeId", "application/json", (req, res) -> {
            int foodtypeId = Integer.parseInt(req.params("foodtypeId"));
            return gson.toJson(foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId));
        });




        //Reviews


        post("restaurants/:restaurantId/reviews/new", "application/json", (req, res) -> {
            int restaurantId =Integer.parseInt(req.params("restaurantId"));
            Review review = gson.fromJson(req.body(), Review.class);
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
