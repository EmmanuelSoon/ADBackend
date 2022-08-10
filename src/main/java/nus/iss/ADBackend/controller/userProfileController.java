package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.Goal;
import nus.iss.ADBackend.model.Role;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value = "/userprofile", produces = "application/json")
public class userProfileController {
	@Autowired
	UserService userService;

	@RequestMapping("/updateUserDetails")
	public User updateUserDetails(@RequestBody JSONObject requestBody) throws IOException, ParseException {

		User updatedUser = new User();

		updatedUser.setId(Integer.parseInt(requestBody.getAsString("id")));
		updatedUser.setName(requestBody.getAsString("name"));
		updatedUser.setUsername(requestBody.getAsString("username"));
		updatedUser.setPassword(requestBody.getAsString("password"));
		updatedUser.setDateofbirth(LocalDate.parse(requestBody.getAsString("dateofbirth")));
		updatedUser.setGender(requestBody.getAsString("gender"));
		updatedUser.setGoal(Goal.valueOf(requestBody.getAsString("goal")));
		updatedUser.setCalorieintake_limit_inkcal(
				Double.parseDouble(requestBody.getAsString("calorieintake_limit_inkcal")));
		updatedUser.setWaterintake_limit_inml(Double.parseDouble(requestBody.getAsString("waterintake_limit_inml")));
		updatedUser.setRole(Role.NORMAL);
		
		if (userService.findUserByUsername(updatedUser.getUsername()) == null || userService.findUserByUsername(updatedUser.getUsername()).getId() == updatedUser.getId()) {
			if (userService.saveUser(updatedUser)) {
				return userService.findUserByUserNameAndPassword(updatedUser.getUsername(), updatedUser.getPassword());
			}
		}

		return null;
	}

}
