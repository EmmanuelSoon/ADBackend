package nus.iss.ADBackend.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nus.iss.ADBackend.model.WeightedIngredient;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeForm {

    private List<WeightedIngredient> weightedIngredients;
    private int portion;
    private String image;
    private List<String> procedures;
    private String name;
    private int userId;
}
