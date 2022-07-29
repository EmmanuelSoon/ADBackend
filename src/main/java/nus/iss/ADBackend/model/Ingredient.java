package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image;
    @Column(name = "calories/100g")
    private double calorie;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutritionId")
    private NutritionRecord nutritionRecord;

    public Ingredient(String name, String image, double calorie, NutritionRecord nutritionRecord) {
        this.name = name;
        this.image = image;
        this.calorie = calorie;
        this.nutritionRecord = nutritionRecord;
    }

    public Ingredient(String name, double calorie) {
        this.name = name;
        this.calorie = calorie;
    }

    public Ingredient(String name) {
        this.name = name;
    }




}
