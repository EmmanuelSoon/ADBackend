package nus.iss.ADBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

}
