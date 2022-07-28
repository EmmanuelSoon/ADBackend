package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.CommentRepository;
import nus.iss.ADBackend.Repo.RecipeRepository;
import nus.iss.ADBackend.Repo.ReportRepository;
import nus.iss.ADBackend.model.Dish;
import nus.iss.ADBackend.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository rRepo;
    @Autowired
    CommentRepository cRepo;
    @Autowired
    ReportRepository rpRepo;

    public void createRecipe(Recipe recipe) {
        rRepo.saveAndFlush(recipe);
    }
    public boolean saveRecipe(Recipe recipe) {
        if (rRepo.findById(recipe.getId()) != null) {
            rRepo.saveAndFlush(recipe);
            return true;
        }
        return false;
    }

    public List<Recipe> findRecipesByDish(Dish dish) {
        return rRepo.findAllByDishId(dish.getId());
    }

    @Transactional
    public boolean deleteRecipeById(int id) {
        if (rRepo.findById(id) != null) {
            //delete all the comments
            cRepo.deleteAllByRecipeId(id);
            //delete all the report
            rpRepo.deleteAllByRecipeId(id);
            rRepo.deleteById(id);
        }
        return false;
    }
    @Transactional
    public boolean deleteRecipeById(Recipe recipe) {
        return deleteRecipeById(recipe.getId());
    }

}
