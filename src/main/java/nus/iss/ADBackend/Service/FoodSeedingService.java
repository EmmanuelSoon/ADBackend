package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.DishRepository;
import nus.iss.ADBackend.Repo.IngredientRepository;
import nus.iss.ADBackend.Repo.NutritionRecordRepository;
import nus.iss.ADBackend.model.Dish;
import nus.iss.ADBackend.model.Ingredient;
import nus.iss.ADBackend.model.NutritionRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodSeedingService {

    @Autowired
    DishRepository dRepo;
    @Autowired
    NutritionRecordRepository nRepo;
    @Autowired
    IngredientRepository iRepo;
    String[] headers = {"name","totalCalories", "carbs", "proteins", "fats", "sugar", "sodium", "calcium", "iron", "iodine", "vitamins", "servingSize"};
    //seed ingredient together with nutrition data to database
    public void seedIngredientFromCSV(String path) {
        Reader reader = null;
        Iterable<CSVRecord> records = null;
        try {
            reader = new FileReader(path);
            records = CSVFormat.DEFAULT.withHeader(headers).withFirstRecordAsHeader().parse(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("failed to get CSV file at " + path);
        }
        catch (IOException e) {
            throw new RuntimeException("hearders are not match !");
        }
        List<Ingredient> list = new ArrayList<>();
        for (CSVRecord record : records) {
            Ingredient ingredient = new Ingredient(record.get(headers[0]));
            NutritionRecord nutritionRecord = new NutritionRecord(
                    Double.parseDouble(record.get(headers[1])),
                    Double.parseDouble(record.get(headers[2])),
                    Double.parseDouble(record.get(headers[3])),
                    Double.parseDouble(record.get(headers[4])),
                    Double.parseDouble(record.get(headers[5])),
                    Double.parseDouble(record.get(headers[6])),
                    Double.parseDouble(record.get(headers[7])),
                    Double.parseDouble(record.get(headers[8])),
                    Double.parseDouble(record.get(headers[9])),
                    Double.parseDouble(record.get(headers[10])),
                    Double.parseDouble(record.get(headers[11]))
                    );
            ingredient.setNutritionRecord(nutritionRecord);
            ingredient.setCalorie(nutritionRecord.getTotalCalories());
            list.add(ingredient);
        }
        iRepo.saveAllAndFlush(list);
    }

    //seed dish together with nutrition data to database
    public void seedDishDataFromCSV(String path) {
        Reader reader = null;
        Iterable<CSVRecord> records = null;
        try {
            reader = new FileReader(path);
            records = CSVFormat.DEFAULT.withHeader(headers).withFirstRecordAsHeader().parse(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("failed to get CSV file at " + path);
        }
        catch (IOException e) {
            throw new RuntimeException("hearders are not match !");
        }
        List<Dish> list = new ArrayList<>();
        for (CSVRecord record : records) {
            Dish dish = new Dish(record.get(headers[0]));
            NutritionRecord nutritionRecord = new NutritionRecord(
                    Double.parseDouble(record.get(headers[1])),
                    Double.parseDouble(record.get(headers[2])),
                    Double.parseDouble(record.get(headers[3])),
                    Double.parseDouble(record.get(headers[4])),
                    Double.parseDouble(record.get(headers[5])),
                    Double.parseDouble(record.get(headers[6])),
                    Double.parseDouble(record.get(headers[7])),
                    Double.parseDouble(record.get(headers[8])),
                    Double.parseDouble(record.get(headers[9])),
                    Double.parseDouble(record.get(headers[10])),
                    Double.parseDouble(record.get(headers[11]))
            );
            dish.setNutritionRecord(nutritionRecord);
            dish.setCalorie(nutritionRecord.getTotalCalories());
            list.add(dish);
        }
        dRepo.saveAllAndFlush(list);
    }

}
