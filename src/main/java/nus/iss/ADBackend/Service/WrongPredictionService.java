package nus.iss.ADBackend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.ADBackend.Repo.WrongPredictionRepository;
import nus.iss.ADBackend.model.WrongPrediction;

@Service
public class WrongPredictionService {
    
    @Autowired
    private WrongPredictionRepository wpRepo;

    public void saveWrongPrediction(String actualIngredient, String predictedIngredient, String photoString){
        WrongPrediction wp = new WrongPrediction(actualIngredient, predictedIngredient, photoString);
        wpRepo.saveAndFlush(wp);
    }

    public List<WrongPrediction> getAllWrongPredictions(){
        return wpRepo.findAll();
    }
}
