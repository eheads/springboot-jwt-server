package com.attgroup.app.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
@Validated
public class JwtProperties {

	@NotNull
	private String jwtSecret;
	
	@NotNull
	private int jwtExpiration;
}
