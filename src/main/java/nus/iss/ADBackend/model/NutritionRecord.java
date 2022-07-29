package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class NutritionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double totalCalories;
    private double carbs;
    private double proteins;
    private double fats;
    private double sugar;
    private double sodium;
    private double calcium;
    private double iron;
    private double vitaminA;
    private double vitaminB12;
    private double vitaminB6;
    private double vitaminC;
    private double vitaminE;
    private double vitaminK;
    private double fiber;
    private double water;
    private double cholesterol;
    //default value
    private double servingSize = 100.0;

    public NutritionRecord(double totalCalories, double carbs, double proteins, double fats, double sugar, double sodium, double calcium, double iron, double iodine, double vitamins, double servingSize) {
        this.totalCalories = totalCalories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
        this.sugar = sugar;
        this.sodium = sodium;
        this.calcium = calcium;
        this.iron = iron;
        this.servingSize = servingSize;
    }

    public NutritionRecord(double totalCalories) {
        this.totalCalories = totalCalories;
    }


    public NutritionRecord(double carbs,
                           double cholesterol,
                           double fiber,
                           double totalCalories,
                           double proteins,
                           double sugar,
                           double water,
                           double fats,
                           double calcium,
                           double iron,
                           double sodium,
                           double vitaminA,
                           double vitaminB12,
                           double vitaminB6,
                           double vitaminC,
                           double vitaminE,
                           double vitaminK
                           ) {
        this.totalCalories = setterHelper(totalCalories);
        this.carbs = setterHelper(carbs);
        this.proteins = setterHelper(proteins);
        this.fats = setterHelper(fats);
        this.sugar = setterHelper(sugar);
        this.sodium = setterHelper(sodium);
        this.calcium = setterHelper(calcium);
        this.iron = setterHelper(iron);
        this.vitaminA = setterHelper(vitaminA);
        this.vitaminB12 = setterHelper(vitaminB12);
        this.vitaminB6 = setterHelper(vitaminB6);
        this.vitaminC = setterHelper(vitaminC);
        this.vitaminE = setterHelper(vitaminE);
        this.vitaminK = setterHelper(vitaminK);
        this.fiber = fiber;
        this.cholesterol = cholesterol;
        this.water = water;
    }
    private double setterHelper(double val) {
        if (val < 0)
            return -1.0; //NA data
        else
            return val;
    }



}
