package com.ntsako.ecommerce.service;

import com.ntsako.ecommerce.exception.UserException;
import com.ntsako.ecommerce.model.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwl) throws UserException;
	
	public boolean isUserAlreadyRegisterd(String email);
	
	public User creatUser(User user) throws UserException;

}
