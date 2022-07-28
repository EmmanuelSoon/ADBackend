package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class DietRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @FutureOrPresent
    private LocalDate date;
    private String foodName;
    private double calorie;
    private double weight;
    @Enumerated(EnumType.STRING)
    private MealType type;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public DietRecord(User user, String foodName, MealType meal, Double calCount, Double weight) {
        this.user = user;
        this.foodName = foodName;
        this.type = meal;
        this.calorie = calCount;
        this.weight = weight;

        this.date = LocalDate.now();

    }

}
