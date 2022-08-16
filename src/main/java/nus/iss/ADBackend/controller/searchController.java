package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.IngredientService;
import nus.iss.ADBackend.Service.RecipeService;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.Ingredient;
import nus.iss.ADBackend.model.Recipe;

@RestController
@RequestMapping(value= "/search", produces = "application/json")
public class searchController {

    @Autowired
    private IngredientService ingredientService;

    @Autowired RecipeService recipeService;


    @RequestMapping("/ingredients")
    public List<Ingredient> getSearchResults(@RequestBody JSONObject response) throws IOException, ParseException{
        String search = response.getAsString("query");
        double maxCal = response.getAsNumber("maxCal").doubleValue();
        List<Ingredient> iList = ingredientService.findSimilarIngredients(search);
        return iList.stream().filter(ingredient -> ingredient.getCalorie() <= maxCal).collect(Collectors.toList());
    }

    @RequestMapping("/recipes")
    public List<Recipe> getRecipeResults(@RequestBody JSONObject response) throws IOException, ParseException{
        String search = response.getAsString("query");
        List<Recipe> rList;
        rList = recipeService.findAllRecipesBySearch(search);

        return rList;
    }
}