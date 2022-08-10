package nus.iss.ADBackend.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nus.iss.ADBackend.model.WeightedIngredient;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {
    //private String image;
    private String content;
    private int userId;
    private int recipeId;
    private double rating;
}
