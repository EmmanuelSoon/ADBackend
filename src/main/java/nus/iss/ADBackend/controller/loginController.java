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
@RequestMapping(value="/login", produces ="application/json")
public class loginController {
	@Autowired
	UserService userService;
	
	@RequestMapping("/authentication")
	public String getPassword(@RequestBody JSONObject response)
		throws IOException, ParseException{
		
		System.out.println("LoginController");
		String username = response.getAsString("username");
		System.out.println(username);
		if(username != null) {
			String password = userService.findUserByUsername(username).getPassword();
			System.out.println(password);
			return password;
		}
							
		return null;
	}	
}
