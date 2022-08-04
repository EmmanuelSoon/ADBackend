package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Ingredient findByName(String name);

    @Query("select i from Ingredient i where lower(i.name) like %?1%")
    List<Ingredient> findSimilarIngredientsByName(String name);

    @Query("select i from Ingredient i where i.calorie < ?1")
    List<Ingredient> findAllByMaxCalories(double val);
}
