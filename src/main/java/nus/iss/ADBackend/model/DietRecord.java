package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String foodName;
    private double calorie;
    private double weight;
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public DietRecord(User user, String foodName, MealType meal, Double calCount, Double weight) {
        this.user = user;
        this.foodName = foodName;
        this.mealType = meal;
        this.calorie = calCount;
        this.weight = weight;
        this.date = LocalDate.now();
    }
    public DietRecord(User user) {
        this.user = user;
        this.date = LocalDate.now();
    }
    public DietRecord(LocalDate date, User user, String foodName, MealType meal, Double calCount, Double weight) {
        this.date = date;
        this.foodName = foodName;
        this.calorie =  calCount;
        this.weight = weight;
        this.mealType = meal;
        this.user = user;
    }
}
