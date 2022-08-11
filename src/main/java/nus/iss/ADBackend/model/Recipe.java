package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(length = Integer.MAX_VALUE)
    private String image;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "dishId")
    private Dish dish = null;
    @Column(name = "createdOn")
    private LocalDateTime dateTime;
    private String searchWords;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WeightedIngredient> ingredientList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Procedure> procedures = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutritionId")
    private NutritionRecord nutritionRecord;
    private Boolean flagged = false;

    private int portion;
    public Recipe(String image, User user, Dish dish, LocalDateTime dateTime) {
        this.image = image;
        this.user = user;
        this.dish = dish;
        this.dateTime = dateTime;
    }

    public void setIngredientList(List<WeightedIngredient> weightedIngredients){
        this.ingredientList.clear();
        if(weightedIngredients != null){
            this.ingredientList.retainAll(weightedIngredients);
            this.ingredientList.addAll(weightedIngredients);
        }
    }

    public void setProcedures(List<Procedure> procedures){
        this.procedures.clear();
        if(procedures != null){
            this.procedures.retainAll(procedures);
            this.procedures.addAll(procedures);
        }
    }

    public double getTotalCalories() {
        double total = 0.0;
        for (WeightedIngredient weightedIngredient : ingredientList) {
            total += weightedIngredient.getCalorie();
        }
        return total;
    }
    public NutritionRecord getNutritionRecord(){
        if (dish != null) {
            return dish.getNutritionRecord();
        }
        else {
            return nutritionRecord;
        }
    }

    public double getCalPerServing(){
        return getTotalCalories()/portion;
    }
}
