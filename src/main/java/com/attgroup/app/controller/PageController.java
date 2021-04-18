package com.attgroup.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.attgroup.app.dto.UserDto;
import com.attgroup.app.payload.LoginRequest;
import com.attgroup.app.security.jwt.JwtUtils;
import com.attgroup.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PageController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtUtils jwtUtils;


	@GetMapping("/login")
	public String login(Model model, @RequestParam(value="param1", required=false) String userId, @RequestParam(value="param2", required=false) String pass) {
		log.debug("Calling login page...");

		log.debug("userId: {}", userId);
		log.debug("pass: {}", pass);

		model.addAttribute("userId", userId);
		model.addAttribute("password", pass);
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup() {
		log.debug("Calling signup page...");
		return "signup";
	}

}
