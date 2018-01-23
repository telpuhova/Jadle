package dao;

import com.sun.org.apache.regexp.internal.RE;
import models.Foodtype;
import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;


public class Sql2oFoodtypeDaoTest {

    Connection con;
    Sql2o sql2o;
    Sql2oFoodtypeDao foodtypeDao;
    Sql2oRestaurantDao restaurantDao;


    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        sql2o = new Sql2o(connectionString, "", "");
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        con = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        con.close();
    }

    @Test
    public void add() throws Exception {
        Foodtype foodtype = setupFoodtype();
        foodtypeDao.add(foodtype);
        assertEquals(1, foodtype.getId());
    }

    @Test
    public void getAll() throws Exception {
        Foodtype foodtype = setupFoodtype();
        foodtypeDao.add(foodtype);
        Foodtype foodtype2 = setupFoodtype();
        foodtypeDao.add(foodtype2);
        assertEquals(2, foodtypeDao.getAll().size());
    }

    @Test
    public void deleteById() throws Exception {
        Foodtype foodtype = setupFoodtype();
        foodtypeDao.add(foodtype);
        Foodtype foodtype2 = setupFoodtype();
        foodtypeDao.add(foodtype2);
        foodtypeDao.deleteById(foodtype.getId());
        foodtypeDao.deleteById(foodtype2.getId());
        assertEquals(0, foodtypeDao.getAll().size());
    }

    @Test
    public void addFoodTypeToRestaurant() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        Restaurant altRestaurant = setupRestaurant();

        restaurantDao.add(testRestaurant);
        restaurantDao.add(altRestaurant);

        Foodtype testFoodtype = setupFoodtype();

        foodtypeDao.add(testFoodtype);

        foodtypeDao.addFoodtypeToRestaurant(testFoodtype, testRestaurant);
        foodtypeDao.addFoodtypeToRestaurant(testFoodtype, altRestaurant);

        assertEquals(2, foodtypeDao.getAllRestaurantsForAFoodtype(testFoodtype.getId()).size());
    }

    @Test
    public void delete_updatesJoinTable() throws Exception {
        Foodtype foodtype1 = new Foodtype("Seafood");
        foodtypeDao.add(foodtype1);

        Restaurant restaurant1 = setupRestaurant();
        restaurantDao.add(restaurant1);

        Restaurant restaurant2 = setupRestaurant();
        restaurantDao.add(restaurant2);

        restaurantDao.addRestaurantToFoodtype(restaurant1, foodtype1);
        restaurantDao.addRestaurantToFoodtype(restaurant2, foodtype1);

        restaurantDao.deleteById(restaurant1.getId());
        assertEquals(0, restaurantDao.getAllFoodtypesForARestaurant(restaurant1.getId()).size());
    }

    public Foodtype setupFoodtype(){
        return new Foodtype("breakfast");
    }

    public Restaurant setupRestaurant(){
        return new Restaurant("taco bell", "1234 ne some street, Portland, OR", "1234", "12345678", "website.com", "email@mail.com");
    }
}