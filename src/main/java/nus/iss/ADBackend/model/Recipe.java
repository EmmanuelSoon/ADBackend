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
    private String image;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "dishId")
    private Dish dish;
    @Column(name = "createdOn")
    private LocalDateTime dateTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WeightedIngredient> ingredientList = new ArrayList<>();
    /*    @Column(length = Integer.MAX_VALUE)
        private String procedures;*/
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Procedure> procedures = new ArrayList<>();

    public Recipe(String image, User user, Dish dish, LocalDateTime dateTime) {
        this.image = image;
        this.user = user;
        this.dish = dish;
        this.dateTime = dateTime;
    }

    public double getTotalCalories() {
        double total = 0.0;
        for (WeightedIngredient weightedIngredient : ingredientList) {
            total += weightedIngredient.getCalorie();
        }
        return total;
    }
}
