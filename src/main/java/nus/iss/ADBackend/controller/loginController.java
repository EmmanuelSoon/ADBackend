package nus.iss.ADBackend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value = "/login", produces = "application/json")
public class loginController {

	@Autowired
	UserService userService;

	@RequestMapping("/validateUserDetails")
	public User validateUserDetails(@RequestBody JSONObject requestBody) throws IOException, ParseException {

		User user = userService.findUserByUserNameAndPassword(requestBody.getAsString("username"),
				requestBody.getAsString("password"));

		return user;
	}

	@PostMapping ("/check")
	public User authenticateUser(@RequestBody User user) {
		return userService.findUserByUserNameAndPassword(user.getUsername(), user.getPassword());
	}
}
