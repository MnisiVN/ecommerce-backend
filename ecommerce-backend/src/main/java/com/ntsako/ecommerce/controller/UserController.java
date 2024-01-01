package com.ntsako.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private UserService userService;

	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) throws UserException {
		
		User user = userService.findUserById(userId);
		
		return new ResponseEntity<> (user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/user")
	public ResponseEntity<User> getUserProfileByJwt(@RequestParam String jwt) throws UserException {
		
		User user = userService.findUserProfileByJwt(jwt);
		
		return new ResponseEntity<> (user, HttpStatus.ACCEPTED);
	}
}
