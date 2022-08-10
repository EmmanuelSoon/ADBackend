package nus.iss.ADBackend.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportForm {
    private String content;
    private int userId;
    private int recipeId;
    private int categoryId;
}
