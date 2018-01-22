package dao;

import models.Foodtype;
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


    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        sql2o = new Sql2o(connectionString, "", "");
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
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

    public Foodtype setupFoodtype(){
        return new Foodtype("breakfast");
    }
}