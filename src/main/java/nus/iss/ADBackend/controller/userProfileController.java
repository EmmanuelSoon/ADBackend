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
import nus.iss.ADBackend.helper.UserWithWeightHeight;
import nus.iss.ADBackend.model.Goal;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.Role;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value = "/userprofile", produces = "application/json")
public class userProfileController {

	private User updatedUser = new User();
	UserWithWeightHeight userWithWeightHeight = new UserWithWeightHeight();
	@Autowired
	UserService userService;

	@Autowired
	HealthRecordService hRecService;

	@RequestMapping("/getUserDetailswithHeightandWeight")
	public UserWithWeightHeight getUserDetailswithHeightandWeight(@RequestBody JSONObject requestBody)
			throws IOException, ParseException {

		updatedUser.setId(Integer.parseInt(requestBody.getAsString("id")));

		HealthRecord hrec = new HealthRecord();
		hrec = hRecService.findTopOneUserHealthRecord(updatedUser.getId());
		// Math.round(a * 100) / 100

		userWithWeightHeight.setUser(userService.findUserById(updatedUser.getId()));
		userWithWeightHeight.setUserHeight((double) Math.round(hrec.getUserHeight() * 100) / 100);
		userWithWeightHeight.setUserWeight((double) Math.round(hrec.getUserWeight() * 100) / 100);

		return userWithWeightHeight;
	}

	@RequestMapping("/updateUserDetails")
	public UserWithWeightHeight updateUserDetails(@RequestBody JSONObject requestBody)
			throws IOException, ParseException {

		updatedUser.setId(Integer.parseInt(requestBody.getAsString("id")));
		updatedUser.setName(requestBody.getAsString("name"));
		updatedUser.setUsername(requestBody.getAsString("username"));
		updatedUser.setPassword(requestBody.getAsString("password"));
		updatedUser.setDateofbirth(LocalDate.parse(requestBody.getAsString("dateofbirth")));
		updatedUser.setGender(requestBody.getAsString("gender"));
		updatedUser.setGoal(Goal.valueOf(requestBody.getAsString("goal")));
		updatedUser.setActivitylevel(requestBody.getAsString("activitylevel"));
		updatedUser.setWaterintake_limit_inml(Double.parseDouble(requestBody.getAsString("waterintake_limit_inml")));
		updatedUser.setRole(Role.NORMAL);

		hRecService.updateUserHeight(updatedUser.getId(), Double.parseDouble(requestBody.getAsString("userHeight")),
				LocalDate.now());
		hRecService.updateUserWeight(updatedUser.getId(), Double.parseDouble(requestBody.getAsString("userWeight")),
				LocalDate.now());

		HealthRecord hrec = new HealthRecord();
		hrec = hRecService.findTopOneUserHealthRecord(updatedUser.getId());

		updatedUser.setCalorieintake_limit_inkcal(
				Double.parseDouble(performCalculation(updatedUser.getGender(), String.valueOf(hrec.getUserWeight()),
						String.valueOf(hrec.getUserHeight()), updatedUser.getActivitylevel())));

		if (userService.findUserByUsername(updatedUser.getUsername()) == null
				|| userService.findUserByUsername(updatedUser.getUsername()).getId() == updatedUser.getId()) {
			if (userService.saveUser(updatedUser)) {
				userWithWeightHeight.setUser(userService.findUserByUserNameAndPassword(updatedUser.getUsername(),
						updatedUser.getPassword()));
				userWithWeightHeight.setUserWeight(hrec.getUserWeight());
				userWithWeightHeight.setUserHeight(hrec.getUserHeight());

				return userWithWeightHeight;
			}
		}

		return null;
	}

	private String performCalculation(String gender, String userWeight, String userHeight, String activityLevel) {
		Double calculatedCalorie = 0.0;

		if (gender.equals("M")) {
			calculatedCalorie = (10 * Double.parseDouble(userWeight)) + (6.25 * Double.parseDouble(userHeight))
					- (5 * calculateAge()) + 5;
		} else if (gender.equals("F")) {
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
