package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import nus.iss.ADBackend.Service.DietRecordService;
import nus.iss.ADBackend.Service.HealthRecordService;
import nus.iss.ADBackend.Service.IngredientService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.helper.UserCombinedData;
import nus.iss.ADBackend.helper.WeekMonthData;
import nus.iss.ADBackend.model.*;

@RestController
@RequestMapping(value= "/user", produces = "application/json")
public class loggerController {

    @Autowired
    DietRecordService dietRecordService;

    @Autowired
    UserService userService;

    @Autowired
    HealthRecordService hrService;

    @Autowired
    IngredientService ingredientService;
    

    @RequestMapping("/gethealthrecorddate")
    public HealthRecord getHealthRecord(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
        LocalDate date = LocalDate.parse(dateString);

        User curr = userService.findUserByUsername(username); 
        HealthRecord myHr = hrService.findHealthRecordByUserIdAndDate(curr.getId(), date);

        if (myHr != null){
        return myHr;
    }
        else {
            return new HealthRecord(date, 0, 0, 0, 0, curr);
        }

    }

    @RequestMapping("/getuserrecords")
    public UserCombinedData getUserCombinedData(@RequestBody JSONObject response) throws IOException, ParseException{
        UserCombinedData ucd = new UserCombinedData();
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
        String graphFilter = response.getAsString("graphFilter");
        LocalDate date = LocalDate.parse(dateString);
        User curr = userService.findUserByUsername(username); 
		List<HealthRecord> hrList = new ArrayList<HealthRecord>();
		List<WeekMonthData> weekData = new ArrayList<WeekMonthData>();
		List<WeekMonthData> monthData = new ArrayList<WeekMonthData>();
		        

        	hrList = getDailyFilterRecords(curr.getId());
        	weekData = getWeeklyFilterRecords(curr.getId());
        	monthData = getMonthlyFilterRecords(curr.getId());
        
        List<DietRecord> dList = dietRecordService.findByUserIdAndDate(curr.getId(), date);

        //add new health record for the day
        if (hrList.get(0).getDate().isBefore(LocalDate.now())){
            HealthRecord newHr = hrService.createHealthRecordIfAbsent(curr.getId(), LocalDate.now());
            newHr.setUserWeight(hrList.get(0).getUserWeight());
            newHr.setUserHeight(hrList.get(0).getUserHeight());
            hrService.saveHealthRecord(newHr);
            hrList.add(0, newHr);
        }

        ucd.setWeekList(weekData);
        ucd.setMonthList(monthData);
        ucd.setMyDietRecord(dList);
        ucd.setMyHrList(hrList);

        return ucd;
    }



    //check if still using this
    @RequestMapping("/gethealthrecords")
    public List<HealthRecord> getAllHealthRecord(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        User curr = userService.findUserByUsername(username); 
        List<HealthRecord> hrList = hrService.findAllHealthRecordsByUserId(curr.getId());

        return hrList;
    }

    @RequestMapping("/getdietrecords")
    public List<DietRecord> getDietRecords(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateString);
        // System.out.println((username));
        

        User curr = userService.findUserByUsername(username);
        List<DietRecord> dList = new ArrayList<>();
        dList = dietRecordService.findByUserIdAndDate(curr.getId(), date);
        
        return dList;
    }

    @RequestMapping("/adddietrecord")
    @ResponseStatus(HttpStatus.OK)
    public void addDietRecord (@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        User user = userService.findUserByUsername(username);
        String dateString = response.getAsString("date");
        LocalDate date = LocalDate.parse(dateString);
        String mealType = response.getAsString("mealType");

        response.remove("username");
        response.remove("date");
        response.remove("mealType");
        Iterator<String> keys = response.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
                Ingredient curr = ingredientService.findIngredientById(Integer.parseInt(key));
                //String mealName = curr.getName();
                double weight = Double.valueOf(response.getAsString(key));
                double mealCals = weight/100 * curr.getCalorie();

                DietRecord myDr = new DietRecord(date, user, curr, MealType.valueOf(mealType), mealCals, weight);
                dietRecordService.createDietRecord(myDr);
                double newCalTotal = dietRecordService.getTotalCaloriesByUserIdAndDate(user.getId(), date);
                HealthRecord hr = hrService.createHealthRecordIfAbsent(user.getId(), date);
                hr.setCalIntake(newCalTotal);
                hrService.saveHealthRecord(hr);
        }
    }

    @RequestMapping("/editdietrecord")
    @ResponseStatus(HttpStatus.OK)
    public void editDietRecord (@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        User user = userService.findUserByUsername(username);
        String dateString = response.getAsString("date");
        LocalDate date = LocalDate.parse(dateString);
        String mealType = response.getAsString("mealType");
        
        List<DietRecord> dietRecords = dietRecordService.findByUserIdAndDate(user.getId(), date);

        response.remove("username");
        response.remove("date");
        response.remove("mealType");
        Iterator<String> keys = response.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
                Ingredient curr = ingredientService.findIngredientById(Integer.parseInt(key));
                double weight = Double.valueOf(response.getAsString(key));
                double mealCals = weight/100 * curr.getCalorie();
                boolean check = true;
                for (DietRecord dr : dietRecords){
                    if(dr.getIngredient().equals(curr) && dr.getMealType().equals(MealType.valueOf(mealType))){
                        check = false;
                        dr.setWeight(weight);
                        dr.setCalorie(mealCals);
                        dietRecordService.saveDietRecord(dr);
                    }
                }
                if(check){
                    DietRecord myDr = new DietRecord(date, user, curr, MealType.valueOf(mealType), mealCals, weight);
                    dietRecordService.createDietRecord(myDr);
                }
        } 
        double newCalTotal = dietRecordService.getTotalCaloriesByUserIdAndDate(user.getId(), date);
        HealthRecord hr = hrService.createHealthRecordIfAbsent(user.getId(), date);
        hr.setCalIntake(newCalTotal);
        hrService.saveHealthRecord(hr);
    }

    @RequestMapping("/deletedietrecord")
    public ResponseEntity<String> deleteDietRecord(@RequestBody JSONObject response) throws IOException, ParseException{
        int drId = (Integer)response.getAsNumber("dietRecordId");
        if(dietRecordService.deleteDietRecordById(drId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    
    @RequestMapping("/getmealrecords")
    public List<DietRecord> getMealRecords(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
        String mealString = response.getAsString("meal");
        LocalDate date = LocalDate.parse(dateString);
        User curr = userService.findUserByUsername(username);
        List<DietRecord> dList = new ArrayList<>();
        dList = dietRecordService.findByUserIdAndDate(curr.getId(), date);

        return dList.stream().filter(d -> d.getMealType().equals(MealType.valueOf(mealString.toUpperCase()))).collect(Collectors.toList());
    }

 

    @RequestMapping("/addwater")
    public void addWater(@RequestBody JSONObject response) throws IOException, ParseException{
        Integer hrId = (Integer) response.getAsNumber("hrID");
        Double waterMils = response.getAsNumber("addMils").doubleValue();
        HealthRecord myHr = hrService.findHealthRecordById(hrId);
        waterMils = waterMils + myHr.getWaterIntake() > 0 ? waterMils + myHr.getWaterIntake() : 0;
        myHr.setWaterIntake(waterMils);
        hrService.saveHealthRecord(myHr);
    }
    
    @RequestMapping("/updateheight")
    public ResponseEntity updateHeight(@RequestBody JSONObject response){
        try{
            String username = response.getAsString("username");
            double height = response.getAsNumber("height").doubleValue();
            String dateString = response.getAsString("date");
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int userId = userService.findUserByUsername(username).getId();
            hrService.updateUserHeight(userId, height, date);

            User curr = userService.findUserByUsername(username); 
            HealthRecord myHr = hrService.findHealthRecordByUserIdAndDate(curr.getId(), date);

            return ResponseEntity.ok(myHr);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @RequestMapping("/updateweight")
    public ResponseEntity updateWeight(@RequestBody JSONObject response){
        try{
            String username = response.getAsString("username");
            double weight = response.getAsNumber("weight").doubleValue();
            String dateString = response.getAsString("date");
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int userId = userService.findUserByUsername(username).getId();
            hrService.updateUserWeight(userId, weight, date);

            User curr = userService.findUserByUsername(username); 
            HealthRecord myHr = hrService.findHealthRecordByUserIdAndDate(curr.getId(), date);
            return ResponseEntity.ok(myHr);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }

    }
    
    
    private List<HealthRecord> getDailyFilterRecords(Integer userId)
    {
    	
    	return hrService.getDailyFilterRecords(userId);
    }
    
    private List<WeekMonthData> getWeeklyFilterRecords(Integer userId)
    {
    	
    	return hrService.getWeeklyFilterRecords(userId);
    }
    
    private List<WeekMonthData> getMonthlyFilterRecords(Integer userId)
    {
    	
    	return hrService.getMonthlyFilterRecords(userId);
    }

}
