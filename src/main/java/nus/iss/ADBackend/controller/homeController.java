package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import nus.iss.ADBackend.Service.DietRecordService;
import nus.iss.ADBackend.Service.HealthRecordService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.DietRecord;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value= "/home", produces = "application/json")
public class homeController {
	@Autowired
    HealthRecordService healthRecService;

    @Autowired
    UserService userService;
    
    @RequestMapping("/gethealthrecords")
    public List<HealthRecord> getHealthRecords(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        String dateString = response.getAsString("date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
//        System.out.println((username));
//        System.out.println(date);

        User curr = userService.findUserByUsername(username);
        List<HealthRecord> hrList = healthRecService.findHealthRecordListByUserIdAndDate(curr.getId(), date);
        return hrList;
    }
    
    @RequestMapping("/gethealthrecordsbyUserName")
    public List<HealthRecord> getHealthRecordsbyUserName(@RequestBody JSONObject response) throws IOException, ParseException{
        String username = response.getAsString("username");
        System.out.println((username));

        User curr = userService.findUserByUsername(username);
        List<HealthRecord> hrList = healthRecService.findAllHealthRecordsByUserId(curr.getId());
        return hrList;
    }
}
