package com.tegar.implement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.tegar.entity.Role;
import com.tegar.entity.User;
import com.tegar.entity.Persistence.Status;
import com.tegar.model.UserModel;
import com.tegar.model.UserRequestModel;
import com.tegar.repository.RoleRepository;
import com.tegar.repository.UserRepository;
import com.tegar.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		
		user.getRoles().forEach(role -> {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
			grantedAuthorities.add(grantedAuthority);
		});
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserModel register(UserRequestModel userRequestModel) {
		
		User userByUsername = userRepository.findByUsername(userRequestModel.getUsername());
		if (userByUsername != null && userByUsername.getId() != null ) 
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Username already exists.");
		
		User userByEmail = userRepository.findByUsername(userRequestModel.getEmail());
		if (userByEmail != null && userByEmail.getEmail() != null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Email already exists.");
		
		Role role = roleRepository.findByRoleName(Role.RoleName.ROLE_USER.toString());
		
		User newUser = new User();
		newUser.setUsername(userRequestModel.getUsername());
		newUser.setFullName(userRequestModel.getFullName());
		newUser.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));
		newUser.setEmail(userRequestModel.getEmail());
		newUser.setPhoneNumber(userRequestModel.getPhoneNumber());
		newUser.setAddress(userRequestModel.getAddress());
		newUser.setRoles(Collections.singleton(role));
		newUser.setStatus(Status.ACTIVE);
		newUser = userRepository.save(newUser);
		
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(newUser, userModel);
		
		return userModel;
	}

	@Override
	public UserModel findByUsername(String username) {
		
		User user = userRepository.findByUsername(username);
		if (user == null) 
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "username not found.");
		 
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(user, userModel);
		
		logger.info(userModel.toString());
		return userModel;
	}
	
	

}
