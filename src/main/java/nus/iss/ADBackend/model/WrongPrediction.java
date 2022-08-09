package nus.iss.ADBackend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class WrongPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String actualIngredient;
    private String predictedIngredient;
    private int status;
    @Column(length = Integer.MAX_VALUE)
    private String photoString; 

    

    public WrongPrediction(String actualIngredient, String predictedIngredient, String photoString, int status) {
        super();
        this.actualIngredient = actualIngredient;
        this.predictedIngredient = predictedIngredient;
        this.photoString = photoString;
        this.status = status;
        
    }

}
