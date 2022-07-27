package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image;
    @Column(name = "calorie/100g")
    private double calorie;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutritionId")
    private NutritionRecord nutritionRecord;

    public Dish(String name, String image, double calorie) {
        this.name = name;
        this.image = image;
        this.calorie = calorie;
    }

    public Dish(String name, String image, double calorie, NutritionRecord nutritionRecord) {
        this.name = name;
        this.image = image;
        this.calorie = calorie;
        this.nutritionRecord = nutritionRecord;
    }


}
