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
    private double iodine;
    private double vitamins;
    private double servingSize;

    public NutritionRecord(double totalCalories, double carbs, double proteins, double fats, double sugar, double sodium, double calcium, double iron, double iodine, double vitamins, double servingSize) {
        this.totalCalories = totalCalories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
        this.sugar = sugar;
        this.sodium = sodium;
        this.calcium = calcium;
        this.iron = iron;
        this.iodine = iodine;
        this.vitamins = vitamins;
        this.servingSize = servingSize;
    }

    public NutritionRecord(double totalCalories) {
        this.totalCalories = totalCalories;
    }
}
