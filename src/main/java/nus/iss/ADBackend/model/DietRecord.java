package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.description.field.FieldDescription.InGenericShape;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class DietRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    @OneToOne
    @JoinColumn(name = "ingredientId")
    private Ingredient ingredient;
    private double calorie;
    private double weight;
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public DietRecord(User user, Ingredient ingredient, MealType meal, Double calCount, Double weight) {
        this.user = user;
        this.ingredient = ingredient;
        this.mealType = meal;
        this.calorie = calCount;
        this.weight = weight;
        this.date = LocalDate.now();
    }
    public DietRecord(User user) {
        this.user = user;
        this.date = LocalDate.now();
    }
    public DietRecord(LocalDate date, User user, Ingredient ingredient, MealType meal, Double calCount, Double weight) {
        this.date = date;
        this.ingredient = ingredient;
        this.calorie =  calCount;
        this.weight = weight;
        this.mealType = meal;
        this.user = user;
    }
}
