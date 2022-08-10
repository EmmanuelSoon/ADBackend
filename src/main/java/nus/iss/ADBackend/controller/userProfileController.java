package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.HealthRecordService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.Goal;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.Role;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value = "/userprofile", produces = "application/json")
public class userProfileController {

	private User updatedUser = new User();
	@Autowired
	UserService userService;

	@Autowired
	HealthRecordService hRecService;

	@RequestMapping("/updateUserDetails")
	public User updateUserDetails(@RequestBody JSONObject requestBody) throws IOException, ParseException {

		updatedUser.setId(Integer.parseInt(requestBody.getAsString("id")));
		updatedUser.setName(requestBody.getAsString("name"));
		updatedUser.setUsername(requestBody.getAsString("username"));
		updatedUser.setPassword(requestBody.getAsString("password"));
		updatedUser.setDateofbirth(LocalDate.parse(requestBody.getAsString("dateofbirth")));
		updatedUser.setGender(requestBody.getAsString("gender"));
		updatedUser.setGoal(Goal.valueOf(requestBody.getAsString("goal")));
		updatedUser.setActivitylevel(requestBody.getAsString("activitylevel"));
//		updatedUser.setCalorieintake_limit_inkcal(
//				Double.parseDouble(requestBody.getAsString("calorieintake_limit_inkcal")));
		updatedUser.setWaterintake_limit_inml(Double.parseDouble(requestBody.getAsString("waterintake_limit_inml")));
		updatedUser.setRole(Role.NORMAL);

		HealthRecord hrec = new HealthRecord();
		hrec = hRecService.findTopOneUserHealthRecord(updatedUser.getId());

		updatedUser.setCalorieintake_limit_inkcal(
				Double.parseDouble(performCalculation(updatedUser.getGender(), String.valueOf(hrec.getUserWeight()),
						String.valueOf(hrec.getUserHeight()), updatedUser.getActivitylevel())));

		if (userService.findUserByUsername(updatedUser.getUsername()) == null
				|| userService.findUserByUsername(updatedUser.getUsername()).getId() == updatedUser.getId()) {
			if (userService.saveUser(updatedUser)) {
				return userService.findUserByUserNameAndPassword(updatedUser.getUsername(), updatedUser.getPassword());
			}
		}

		return null;
	}

	private String performCalculation(String gender, String userWeight, String userHeight, String activityLevel) {
		Double calculatedCalorie = 0.0;

		if (gender.equals("M")) {
			// 13.397W + 4.799H - 5.677A + 88.362
			// new: 10 x weight (kg) + 6.25 x height (cm) – 5 x age (y) + 5 (kcal / day)
			calculatedCalorie = (10 * Double.parseDouble(userWeight)) + (6.25 * Double.parseDouble(userHeight))
					- (5 * calculateAge()) + 5;
		} else if (gender.equals("F")) {
			// 9.247W + 3.098H - 4.330A + 447.593
			// new: 10 x weight (kg) + 6.25 x height (cm) – 5 x age (y) -161 (kcal / day)
			calculatedCalorie = (10 * Double.parseDouble(userWeight)) + (6.25 * Double.parseDouble(userHeight))
					- (5 * calculateAge()) - 161;
		}

		switch (activityLevel) {
		case "Lightly Active":
			calculatedCalorie = calculatedCalorie * 1.375;
			break;
		case "Moderately Active":
			calculatedCalorie = calculatedCalorie * 1.550;
			break;
		case "Very Active":
			calculatedCalorie = calculatedCalorie * 1.725;
			break;
		case "Extra Active":
			calculatedCalorie = calculatedCalorie * 1.9;
			break;
		default:
			calculatedCalorie = calculatedCalorie * 1.2;
		}

		if (updatedUser.getGoal().equals(Goal.WEIGHTLOSS)) {
			calculatedCalorie -= (0.15 * calculatedCalorie);
		} else if (updatedUser.getGoal().equals(Goal.WEIGHTGAIN)) {
			calculatedCalorie += 500;
		}

		calculatedCalorie = Math.ceil(calculatedCalorie);

		return calculatedCalorie.toString();
	}

	private Integer calculateAge() {
		LocalDate currDate = LocalDate.now();
		return Period.between(updatedUser.getDateofbirth(), currDate).getYears();
	}

}
