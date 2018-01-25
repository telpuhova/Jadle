package dao;

import models.Restaurant;
import models.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.*;


public class Sql2oReviewDaoTest {

    private Connection con;
    private Sql2oReviewDao reviewDao;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        reviewDao = new Sql2oReviewDao(sql2o);
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        con = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        con.close();
    }

    @Test
    public void add() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Captain Kirk", 3, "great", testRestaurant.getId());
        reviewDao.add(testReview);
        assertEquals(1, testReview.getId());
    }


    @Test
    public void getAllReviewsByRestaurant() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Restaurant newRestaurant = setupRestaurant();
        restaurantDao.add(newRestaurant);

        Review testReview = new Review("mike", 3, "okay",  testRestaurant.getId());
        reviewDao.add(testReview);

        Review otherReview = new Review("jack", 1, "passable", testRestaurant.getId());
        reviewDao.add(otherReview);

        assertEquals(2, reviewDao.getAllReviewsByRestaurant(testRestaurant.getId()).size());
        assertEquals(0, reviewDao.getAllReviewsByRestaurant(newRestaurant.getId()).size());
    }

    @Test
    public void timeStampIsReturnedCorrectly() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Captain Kirk", 3, "foodcoma!", testRestaurant.getId());
        reviewDao.add(testReview);

        long creationTime = testReview.getCreatedat();
        long savedTime = reviewDao.getAll().get(0).getCreatedat();
        String formattedCreationTime = testReview.getFormattedCreatedAt();
        String formattedSavedTime = reviewDao.getAll().get(0).getFormattedCreatedAt();
        assertEquals(formattedCreationTime, formattedSavedTime);
        assertEquals(creationTime, reviewDao.getAll().get(0).getCreatedat());
    }

    @Test
    public void reviewsAreReturnedInCorrectOrder() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Captain Kirk", 3, "foodcome!", testRestaurant.getId());
        reviewDao.add(testReview);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Review testSecondReview = new Review("mike", 1, "passable", testRestaurant.getId());
        reviewDao.add(testSecondReview);

        List<Review> sorted = reviewDao.getAllReviewsByRestaurantSortedNewestToOldest(testRestaurant.getId());

        assertEquals("passable", sorted.get(0).getContent());

    }



    //helper

    public Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }
}