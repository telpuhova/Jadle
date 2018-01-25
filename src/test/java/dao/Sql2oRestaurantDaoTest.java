package dao;

import com.sun.org.apache.regexp.internal.RE;
import models.Foodtype;
import models.Restaurant;
import models.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;


public class Sql2oRestaurantDaoTest {

    Connection con;
    Sql2oRestaurantDao restaurantDao;
    Sql2oFoodtypeDao foodtypeDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        con = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        con.close();
    }

    @Test
    public void add() throws Exception {
        Restaurant restaurant = setupRestaurant();
        restaurantDao.add(restaurant);
        assertEquals(1, restaurant.getId());
    }

    @Test
    public void getAll() throws Exception {
        Restaurant restaurant = setupRestaurant();
        restaurantDao.add(restaurant);
        Restaurant restaurant2 = setupRestaurant();
        restaurantDao.add(restaurant2);
        assertEquals(2, restaurantDao.getAll().size());
    }

    @Test
    public void findById() throws Exception {
        Restaurant restaurant = setupRestaurant();
        restaurantDao.add(restaurant);
        Restaurant restaurant2 = setupRestaurant();
        restaurantDao.add(restaurant2);
        Restaurant restaurant3 = setupRestaurant();
        restaurantDao.add(restaurant3);
        assertEquals(restaurant2, restaurantDao.findById(restaurant2.getId()));
    }

    @Test
    public void update() throws Exception {
        Restaurant restaurant = setupRestaurant();
        restaurantDao.add(restaurant);
        restaurantDao.update(restaurant.getId(), "new name", "", "", "", "", "");
        assertEquals("new name", restaurantDao.findById(restaurant.getId()).getName());
    }

    @Test
    public void deleteById() throws Exception {
        Restaurant restaurant = setupRestaurant();
        restaurantDao.add(restaurant);
        Restaurant restaurant2 = setupRestaurant();
        restaurantDao.add(restaurant2);

        restaurantDao.deleteById(restaurant.getId());
        restaurantDao.deleteById(restaurant2.getId());
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void getAllFoodtypesForARestaurant() throws Exception {
        Foodtype testFoodtype = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);

        Foodtype otherFoodtype = new Foodtype("Bar Food");
        foodtypeDao.add(otherFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.addRestaurantToFoodtype(testRestaurant, testFoodtype);
        restaurantDao.addRestaurantToFoodtype(testRestaurant, otherFoodtype);

        Foodtype[] foodtypes = {testFoodtype, otherFoodtype};

        assertEquals(restaurantDao.getAllFoodtypesForARestaurant(testRestaurant.getId()), Arrays.asList(foodtypes));
    }

    @Test
    public void deletingRestaurantAlsoUpdatesJoinTable() throws Exception {
        Foodtype testFoodtype = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Restaurant altRestaurant = setupRestaurant();
        restaurantDao.add(altRestaurant);

        restaurantDao.addRestaurantToFoodtype(testRestaurant,testFoodtype);
        restaurantDao.addRestaurantToFoodtype(altRestaurant, testFoodtype);

        restaurantDao.deleteById(testRestaurant.getId());
        assertEquals(0, restaurantDao.getAllFoodtypesForARestaurant(testRestaurant.getId()).size());


    }

    public Restaurant setupRestaurant(){
        return new Restaurant("taco bell", "1234 ne some street, Portland, OR", "1234", "12345678", "website.com", "email@mail.com");
    }

    public Foodtype setupFoodtype(){
        return new Foodtype("breakfast");
    }

}