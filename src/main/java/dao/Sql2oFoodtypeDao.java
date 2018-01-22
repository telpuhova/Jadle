package dao;


import models.Foodtype;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oFoodtypeDao implements FoodtypeDao{

    Sql2o sql2o;

    public Sql2oFoodtypeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Foodtype foodtype) {
        String sql = "INSERT INTO foodtypes (name) VALUES (:name)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(foodtype)
                    .executeUpdate()
                    .getKey();
            foodtype.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    //Foodtype findById(int id);

    @Override
    public List<Foodtype> getAll() {
        String sql = "SELECT * FROM foodtypes";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Foodtype.class);
        }
    }

    //List<Restaurant> getAllRestaurantsForAFoodtype(int id);

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM foodtypes WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
