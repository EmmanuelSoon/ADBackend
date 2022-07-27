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
    @OneToOne
    @JoinColumn(name = "nutritionId")
    private NutritionRecord nutritionRecord;
}
