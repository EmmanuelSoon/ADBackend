package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.DataSeedingService.DataSeedingService;
import nus.iss.ADBackend.Repo.CommentRepository;
import nus.iss.ADBackend.Repo.RecipeRepository;
import nus.iss.ADBackend.Repo.ReportRepository;
import nus.iss.ADBackend.Repo.UserRepository;
import nus.iss.ADBackend.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository rRepo;
    @Autowired
    CommentRepository cRepo;
    @Autowired
    ReportRepository rpRepo;
    @Autowired
    UserRepository uRepo;

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

    public List<Recipe> getAllRecipes(){
        return rRepo.findAll();
    }

    public boolean createRecipe(int userId, List<WeightedIngredient> weightedIngredients, List<String> steps, String dataUrl, int portion, String name){
        Recipe recipe = new Recipe();
        User u = uRepo.findById(userId);
        List<Procedure> proceduresList = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            Procedure p = new Procedure(i, steps.get(i), recipe);
            proceduresList.add(p);
        }
        String keywords = "";
        for (int i = 0; i < weightedIngredients.size(); i++) {
            if (i != 0) {
                keywords += ",";
            }
            keywords += weightedIngredients.get(i).getIngredient().getName();
        }
        recipe.setProcedures(proceduresList);
        recipe.setIngredientList(weightedIngredients);
        recipe.setUser(u);
        recipe.setDateTime(LocalDateTime.now());
        recipe.setImage(dataUrl);
        recipe.setNutritionRecord(new DataSeedingService().createNutritionRecordByList(weightedIngredients));
        recipe.setPortion(portion);
        recipe.setName(name);
        recipe.setSearchWords(keywords);
        rRepo.saveAndFlush(recipe);
        return true;
    }

    public Recipe findRecipeById(int id){
        return rRepo.findById(id);
    }

    public boolean editRecipe(int userId, List<WeightedIngredient> weightedIngredients, List<String> steps, String dataUrl, int portion, String name, Recipe editedRecipe){
        User u = uRepo.findById(userId);
        List<Procedure> proceduresList = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            Procedure p = new Procedure(i, steps.get(i), editedRecipe);
            proceduresList.add(p);
        }
        editedRecipe.setProcedures(proceduresList);
        editedRecipe.setIngredientList(weightedIngredients);
        editedRecipe.setUser(u);
        editedRecipe.setDateTime(LocalDateTime.now());
        editedRecipe.setImage(dataUrl);
        editedRecipe.setNutritionRecord(new DataSeedingService().createNutritionRecordByList(weightedIngredients));
        editedRecipe.setPortion(portion);
        editedRecipe.setName(name);
        rRepo.saveAndFlush(editedRecipe);
        return true;
    }


}
