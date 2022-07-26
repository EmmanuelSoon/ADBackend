package nus.iss.ADBackend.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.ADBackend.Repo.WrongPredictionRepository;
import nus.iss.ADBackend.model.WrongPrediction;

@Service
public class WrongPredictionService {
    
    @Autowired
    private WrongPredictionRepository wpRepo;

    public void createWrongPrediction(String actualIngredient, String predictedIngredient, String photoString){
        WrongPrediction wp = new WrongPrediction(actualIngredient, predictedIngredient, photoString, 0);
        wpRepo.saveAndFlush(wp);
    }

    public void saveWrongPrediction(WrongPrediction wp){
        wpRepo.save(wp);
    }

    public List<WrongPrediction> getAllWrongPredictions(){
        return wpRepo.findAll();
    }

    public List<WrongPrediction> getPendingPrediction(){
        return wpRepo.findAll().stream().filter(wp -> wp.getStatus() <= 0).collect(Collectors.toList());
    }

    public void deleteById(int id){
        WrongPrediction wp = wpRepo.findById(id).get();
        wpRepo.delete(wp);
    }

    public WrongPrediction findById(int id){
        return wpRepo.findById(id).get();
    }
}
