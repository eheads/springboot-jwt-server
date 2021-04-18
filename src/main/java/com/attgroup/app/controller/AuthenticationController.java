package com.attgroup.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attgroup.app.data.entity.User;
import com.attgroup.app.dto.UserDto;
import com.attgroup.app.payload.JwtResponse;
import com.attgroup.app.payload.LoginRequest;
import com.attgroup.app.payload.MessageResponse;
import com.attgroup.app.payload.SignupRequest;
import com.attgroup.app.security.jwt.JwtUtils;
import com.attgroup.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	//@Autowired
	//PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest){
		log.debug("loginRequest.getUsername(): {}", loginRequest.getUsername());
		
		//String encryptedPass = passwordEncoder.encode(loginRequest.getPassword());
		//log.debug("encryptedPass: {}", encryptedPass);
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		log.debug("jwt: {}", jwt);
		log.debug("password: {}", loginRequest.getPassword());
		
		UserDetails user = (UserDto) authentication.getPrincipal();
		log.debug("saved password: {}", user.getPassword());
		if(!user.getPassword().equals(loginRequest.getPassword())) {
			return ResponseEntity.ok(new JwtResponse("", "",  user.getUsername()));
		}
		return ResponseEntity.ok(new JwtResponse("", jwt,  user.getUsername()));
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/welcome")
	public ResponseEntity<?> welcome(){
		log.debug("Calling welcome page...");
		
		return ResponseEntity.ok(new MessageResponse("Successful!"));
	}

	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest){
		log.debug("signupRequest.getUsername(): {}", signupRequest.getUsername());
		User user = userService.findByUsername(signupRequest.getUsername());
		log.debug("user: {}",user);
		if(user != null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
		}
		userService.saveUser(signupRequest);
		return ResponseEntity.ok(new MessageResponse("User registration is successful!"));
	}

}
