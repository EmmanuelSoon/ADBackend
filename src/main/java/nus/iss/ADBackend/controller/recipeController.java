package nus.iss.ADBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.ADBackend.Service.RecipeService;
import nus.iss.ADBackend.model.*;;

@RestController
@RequestMapping(value= "/recipe", produces = "application/json")
@CrossOrigin(origins = "http://localhost:3000")
public class recipeController {
    
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/all")
    public List<Recipe> getStudents(){
        return recipeService.getAllRecipes();
    }
}
