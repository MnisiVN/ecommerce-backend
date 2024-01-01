package com.ntsako.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ntsako.ecommerce.config.JwtProvider;
import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.User;
import com.ntsako.ecommerce.repository.UserRepository;
import com.ntsako.ecommerce.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImplementation implements UserService{
	
	private UserRepository userRepository;
	private JwtProvider jwtProvider;

	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User> optional = userRepository.findById(userId);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new UserException("User with user Id: " + userId + " doesnt exist.");
	}

	@Override
	public User findUserProfileByJwt(String jwl) throws UserException {
		String email = jwtProvider.getEmailFromToken(jwl);
		
		User user = userRepository.findByEmail(email);
		
		if (user == null) { 
			throw new UserException("user not found with email: " + email);
		}
		return user;
	}

	@Override
	public boolean isUserAlreadyRegisterd(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

	@Override
	public User creatUser(User user) throws UserException {
		User savedUser = userRepository.save(user);
		return savedUser;
	}

}
