package com.attgroup.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attgroup.app.data.entity.User;
import com.attgroup.app.data.repository.UserRepository;
import com.attgroup.app.dto.UserDto;
import com.attgroup.app.payload.SignupRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;

	public Long saveUser(SignupRequest signupRequest) {
		//signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		log.debug("password: {}", signupRequest.getPassword());
		return userRepository.save(new User(signupRequest)).getId();
	}
	
	public User findByUsername(String username) {
		Optional<User> optional = userRepository.findByUsername(username);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	/**
	 * This method is used by security context of Spring
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optional = userRepository.findByUsername(username);
		if(optional.isEmpty()) {
			throw new UsernameNotFoundException("Username {} not found"+username);
		}
		return UserDto.build(optional.get());
	}
}