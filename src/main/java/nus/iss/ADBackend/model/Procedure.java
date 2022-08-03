package nus.iss.ADBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int steps;
    @Column(length = 512)
    private String detail;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    public Procedure(int steps, String detail, Recipe recipe) {
        this.steps = steps;
        this.detail = detail;
        this.recipe = recipe;
    }
}
