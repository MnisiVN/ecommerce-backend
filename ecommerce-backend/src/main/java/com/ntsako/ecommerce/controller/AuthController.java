package com.ntsako.ecommerce.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.ecommerce.config.JwtProvider;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.request.LoginRequest;
import com.ntsako.ecommerce.request.UserRequest;
import com.ntsako.ecommerce.response.AuthResponse;
import com.ntsako.ecommerce.service.CartService;
import com.ntsako.ecommerce.service.UserService;
import com.ntsako.ecommerce.service.impl.CustomUserDetailsServiceImplementation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	private UserService userService;
	private JwtProvider jwtProvider;
	private PasswordEncoder passwordEncoder;
	private CustomUserDetailsServiceImplementation customUserService;
	private CartService cartService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody UserRequest userRequest) throws UserException {

		boolean isEmailExist = userService.isUserAlreadyRegisterd(userRequest.getEmail());

		if (isEmailExist) {
			throw new UserException("Email is Already Used in Another Account...");
		}

		User creatUser = new User();
		creatUser.setEmail(userRequest.getEmail());
		creatUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		creatUser.setFirstName(userRequest.getFirstName());
		creatUser.setLastName(userRequest.getLastName());
		creatUser.setMobile(userRequest.getMobile());
		creatUser.setCreatedAt(LocalDateTime.now());

		User savedUser = userService.creatUser(creatUser);
		cartService.creatCart(savedUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse(token, "Signup Sucess");

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse(token, "Signin Sucess");

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

	}

	private Authentication authenticate(String email, String password) {
		
		UserDetails userDetails = customUserService.loadUserByUsername(email);

		if (userDetails == null) {
			throw new BadCredentialsException("Invalid Username...");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password...");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
