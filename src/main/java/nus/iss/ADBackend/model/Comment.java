package nus.iss.ADBackend.model;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double rating;
    private String content;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    public Comment(double rating, String content, User user, LocalDateTime dateTime, Recipe recipe) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.dateTime = dateTime;
        this.recipe = recipe;
    }
}
