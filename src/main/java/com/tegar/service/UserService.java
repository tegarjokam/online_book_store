package com.tegar.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tegar.model.UserModel;
import com.tegar.model.UserRequestModel;

public interface UserService extends UserDetailsService {
	
	UserModel register(UserRequestModel userRequestModel);
	UserModel findByUsername(String username);

}
