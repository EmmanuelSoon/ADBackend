package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Dish;
import nus.iss.ADBackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("select d from Dish d where d.name = ?1")
    public Dish findByName(String name);
    Dish findById(int id);
    @Query("select d from Dish d where d.ingredients like %?1%")
    List<Dish> findAllByIngredient(String ingredient);
    @Modifying
    @Query("delete from Dish d where d.id = ?1")
    void deleteById(int id);

    @Query("select d from Dish d where d.calorie < ?1")
    List<Ingredient> findAllByMaxCalories(double val);
}
