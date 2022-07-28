package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.IngredientRepository;
import nus.iss.ADBackend.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IngredientService {
    @Autowired
    IngredientRepository iRepo;


    void createIngredient(Ingredient ingredient) {
        iRepo.saveAndFlush(ingredient);
    }
    @Transactional
    boolean saveIngredient(Ingredient ingredient) {
        if (iRepo.findById(ingredient.getId()) != null) {
            iRepo.saveAndFlush(ingredient);
            return true;
        }
        //if Ingredient not existed
        return false;
    }

    List<Ingredient> findSimilarIngredients(String ingredientName) {
        return iRepo.findSimilarIngredientsByName(ingredientName.toLowerCase());
    }
    List<Ingredient> findIngredientsWithCaloriesLowerThan(double val) {
        return iRepo.findAllByMaxCalories(val);
    }
}
