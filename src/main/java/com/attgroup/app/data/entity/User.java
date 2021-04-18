package com.attgroup.app.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.attgroup.app.payload.SignupRequest;

import lombok.Data;

@Data
@Entity
public class User {
	public User() {}
	public User (SignupRequest signupRequest) {
		this.email = signupRequest.getEmail();
		this.password = signupRequest.getPassword();
		this.username = signupRequest.getUsername();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
}
