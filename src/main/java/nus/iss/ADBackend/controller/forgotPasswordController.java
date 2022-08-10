package nus.iss.ADBackend.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value="/forgotPassword", produces="application/json" )
public class forgotPasswordController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping("validateUsername")
	public User ValdiateUsername(@RequestBody JSONObject requestBody)
		throws IOException, ParseException{
		
		User user = userService.findUserByUsername(requestBody.getAsString("email"));
		
		if(user != null)
			return user;
		
		return null;
		
	}
}
