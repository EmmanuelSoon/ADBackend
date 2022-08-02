package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.IngredientService;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.Ingredient;

@RestController
@RequestMapping(value= "/search", produces = "application/json")
public class searchController {

    @Autowired
    private IngredientService ingredientService;


    @RequestMapping("/ingredients")
    public List<Ingredient> getSearchResults(@RequestBody JSONObject response) throws IOException, ParseException{
        String search = response.getAsString("query");
        List<Ingredient> iList;
        try{
            double calSearch = Double.parseDouble(search);
            iList = ingredientService.findIngredientsWithCaloriesLowerThan(calSearch);
            System.out.println("getting double");

        }
        catch (NumberFormatException numEx){
            iList = ingredientService.findSimilarIngredients(search);
            System.out.println("getting string");
        }

        
        return iList;
    }
}