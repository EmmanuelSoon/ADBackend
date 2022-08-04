package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class WeightedIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "ingredientId")
    private Ingredient ingredient;
    private double weight;
    // getter for json response
    public double getCalorie() {
        return weight * ingredient.getCalorie()/100.0;
    }

    public WeightedIngredient(Ingredient ingredient, double weight) {
        this.ingredient = ingredient;
        this.weight = weight;
    }
}
