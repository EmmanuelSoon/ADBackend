package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    Ingredient findByName(String name);
}
