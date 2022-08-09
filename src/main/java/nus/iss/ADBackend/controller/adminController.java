package nus.iss.ADBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.ADBackend.Service.WrongPredictionService;
import nus.iss.ADBackend.model.WrongPrediction;

@RestController
@RequestMapping(value= "/admin", produces = "application/json")
@CrossOrigin(origins = "http://localhost:3001")
public class adminController {
    
    @Autowired
    private WrongPredictionService wrongPredictionService;

    @GetMapping(value = "/wrongpredictlist")
    public List<WrongPrediction> getAllWrongPredictions(){
        return wrongPredictionService.getAllWrongPredictions();
    }



    @DeleteMapping(value = "wrongpredictdelete/{id}")
    public ResponseEntity deleteWrongPrediction(@PathVariable("id") int id){
        try {
            wrongPredictionService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "wrongpredictstatus/{id}")
    public List<WrongPrediction> changePredictionStatus(@PathVariable("id") int id){
        WrongPrediction wp = wrongPredictionService.findById(id);
        wp.setStatus(wp.getStatus() > 0 ? 0 : 1);
        wrongPredictionService.saveWrongPrediction(wp);
        return wrongPredictionService.getAllWrongPredictions();
    }
}
