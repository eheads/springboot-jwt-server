package com.attgroup.app.payload;

import lombok.Data;

@Data
public class JwtResponse {
	public JwtResponse (String email, String token, String username) {
		this.email = email;
		this.token = token;
		this.username = username;
	}
	
	private String email;
    private String token;
    private String type = "Bearer";
    private String username;
}