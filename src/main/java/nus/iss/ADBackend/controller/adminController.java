package nus.iss.ADBackend.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.RecipeService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.Service.WrongPredictionService;
import nus.iss.ADBackend.model.*;

@RestController
@RequestMapping(value= "/admin", produces = "application/json")
@CrossOrigin(origins = "http://localhost:3001")
public class adminController {
    
    @Autowired
    private WrongPredictionService wrongPredictionService;

    @Autowired UserService userService;

    @Autowired RecipeService recipeService;

    //GET METHODS 

    @GetMapping(value = "/wrongpredictlist")
    public List<WrongPrediction> getAllWrongPredictions(){
        return wrongPredictionService.getAllWrongPredictions();
    }


    @GetMapping(value = "/getusers")
    public List<User> getAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping(value = "/dashboard")
    public ResponseEntity<JSONObject> getDashboardInformation(){
        JSONObject obj = new JSONObject();

        // gather info
        int userCount = userService.findAllUsers() != null ? userService.findAllUsers().size() : 0;
        int recipeCount = recipeService.getAllRecipes() != null ? recipeService.getAllRecipes().size() : 0;

        int wpCount = wrongPredictionService.getAllWrongPredictions() != null ? wrongPredictionService.getAllWrongPredictions().size() : 0;
        int pendingWpCount = wrongPredictionService.getPendingPrediction() != null ? wrongPredictionService.getPendingPrediction().size() : 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String databaseCreated = userService.findUserByUsername("official-user@gmail.com").getDateCreated().format(formatter);

        // put in the information
        obj.put("userCount", userCount);
        obj.put("recipeCount", recipeCount);
        obj.put("wpCount", wpCount);
        obj.put("pendingWpCount", pendingWpCount);
        obj.put("databaseCreated", databaseCreated);
        obj.put("reportCount", 0);
        obj.put("pendingReport", 0);

        return new ResponseEntity<JSONObject>(obj, HttpStatus.OK);
    }

    // DELETE METHODS 

    @DeleteMapping(value = "/wrongpredictdelete/{id}")
    public ResponseEntity deleteWrongPrediction(@PathVariable("id") int id){
        try {
            wrongPredictionService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/userdelete/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") int id){
        try {
            Boolean check = userService.deleteUserById(id);
            if(check){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }   


    // PUT METHODS

    @PutMapping(value = "/wrongpredictstatus/{id}")
    public List<WrongPrediction> changePredictionStatus(@PathVariable("id") int id){
        WrongPrediction wp = wrongPredictionService.findById(id);
        wp.setStatus(wp.getStatus() > 0 ? 0 : 1);
        wrongPredictionService.saveWrongPrediction(wp);
        return wrongPredictionService.getAllWrongPredictions();
    }

    @PutMapping(value = "/setuserrole/{id}")
    public User changeUserRole(@PathVariable("id") int id){
        User user = userService.findUserById(id);
        user.setRole(user.getRole() == Role.NORMAL ? Role.ADMIN : Role.NORMAL);
        userService.saveUser(user);
        return user;
    }
}
