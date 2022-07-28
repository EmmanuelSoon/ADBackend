package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.DishRepository;
import nus.iss.ADBackend.Repo.RecipeRepository;
import nus.iss.ADBackend.model.Dish;
import nus.iss.ADBackend.model.Ingredient;
import nus.iss.ADBackend.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {
    @Autowired
    DishRepository dRepo;
    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository rRepo;

    public void createDish(Dish dish) {
        dRepo.saveAndFlush(dish);
    }
    @Transactional
    public boolean saveDish(Dish dish) {
        if (dRepo.findById(dish.getId()) != null) {
            //dish not existed
            return false;
        }
        dRepo.saveAndFlush(dish);
        return true;
    }

    public List<Dish> findAllDish() {
        return dRepo.findAll();
    }
    public List<Dish> findDishByIngredientName(String ingredient) {
        return dRepo.findAllByIngredient(ingredient);
    }

    @Transactional
    public boolean deleteDish(Dish dish) {
        if (dRepo.findById(dish.getId() )!= null) {
            //remove all recipe associated with dish
            List<Recipe> recipeList = rRepo.findAllByDishId(dish.getId());
            for (Recipe r : recipeList) {
                recipeService.deleteRecipeById(r);
            }
            dRepo.deleteById(dish.getId());
            return true;
        }
        return false;
    }
    public List<Dish> findAllDishesByIngredient(String ingredientName) {
        return dRepo.findAllByIngredient(ingredientName);
    }

    List<Ingredient> findDishesWithCaloriesLowerThan(double val) {
        return dRepo.findAllByMaxCalories(val);
    }


}
