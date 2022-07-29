package nus.iss.ADBackend.DataSeedingService;

import nus.iss.ADBackend.Repo.DishRepository;
import nus.iss.ADBackend.Repo.IngredientRepository;
import nus.iss.ADBackend.Repo.NutritionRecordRepository;
import nus.iss.ADBackend.Repo.UserRepository;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.*;
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
public class DataSeedingService {

    @Autowired
    DishRepository dRepo;

    @Autowired
    NutritionRecordRepository nRepo;
    @Autowired
    IngredientRepository iRepo;
    @Autowired
    UserRepository uRepo;

    public void seedIngredientFromCSV(String path) throws IOException {
        Reader reader = new FileReader(path);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);
        List<Ingredient> list = new ArrayList<>();
        for (CSVRecord record : records) {
            NutritionRecord nutritionRecord = new NutritionRecord(
                    Double.parseDouble(record.get(1)),
                    Double.parseDouble(record.get(2)),
                    Double.parseDouble(record.get(3)),
                    Double.parseDouble(record.get(4)),
                    Double.parseDouble(record.get(5)),
                    Double.parseDouble(record.get(6)),
                    Double.parseDouble(record.get(7)),
                    Double.parseDouble(record.get(8)),
                    Double.parseDouble(record.get(9)),
                    Double.parseDouble(record.get(10)),
                    Double.parseDouble(record.get(11)),
                    Double.parseDouble(record.get(12)),
                    Double.parseDouble(record.get(13)),
                    Double.parseDouble(record.get(14)),
                    Double.parseDouble(record.get(15)),
                    Double.parseDouble(record.get(16)),
                    Double.parseDouble(record.get(17))
            );
            Ingredient ingredient = new Ingredient(record.get(0));
            ingredient.setNutritionRecord(nutritionRecord);
            ingredient.setCalorie(nutritionRecord.getTotalCalories());
            list.add(ingredient);
        }
        iRepo.saveAllAndFlush(list);
    }

    //seed dish together with nutrition data to database
/*    public void seedDishDataFromCSV(String path) {
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
    }*/

    public void seedUserFromCSV(String path) throws IOException {
        Reader  reader = new FileReader(path);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);
        List<User> list = new ArrayList<>();
        for (CSVRecord record : records) {
            User u = new User();
            String name = record.get(0);
            u.setName(name);
            u.setUsername(record.get(1));
            u.setPassword(record.get(2));
            u.setRole(record.get(3).equals("1") ? Role.ADMIN : Role.NORMAL);
            list.add(u);
        }
        uRepo.saveAllAndFlush(list);
    }
    public void launchSeeding() throws IOException {
        seedUserFromCSV("./src/main/resources/data/userdata.csv");
        seedIngredientFromCSV("./src/main/resources/data/data0.csv");
        seedIngredientFromCSV("./src/main/resources/data/data1.csv");
        seedIngredientFromCSV("./src/main/resources/data/data2.csv");
    }
}
