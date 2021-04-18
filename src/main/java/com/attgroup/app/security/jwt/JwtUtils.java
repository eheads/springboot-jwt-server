package com.attgroup.app.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.attgroup.app.data.entity.User;
import com.attgroup.app.property.JwtProperties;
import com.attgroup.app.security.JwtSecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {
    @Autowired
    JwtProperties jwtProperties;
    
    public String generateJwtToken(Authentication authentication) {
    	UserDetails user = (UserDetails) authentication.getPrincipal();
    	Map<String, Object> claims = new HashMap<>();
    	return Jwts.builder()
    			.setClaims(claims)
    			.setSubject(user.getUsername())
    			.setIssuedAt(new Date())
    			.setExpiration(new Date((new Date()).getTime() + jwtProperties.getJwtExpiration()))
    			.signWith(SignatureAlgorithm.HS512, jwtProperties.getJwtSecret())
    			.compact();
    }
    
    public String getUserNameFromJwtToken(String token) {
    	return Jwts.parser().setSigningKey(jwtProperties.getJwtSecret()).parseClaimsJws(token).getBody().getSubject();
    }
    
    private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtProperties.getJwtSecret()).parseClaimsJws(token).getBody();
	}
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    
  	public Date getExpirationDateFromToken(String token) {
  		return getClaimFromToken(token, Claims::getExpiration);
  	}
  	
  	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
  	
	
	public String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(JwtSecurityConstants.HEADER_STRING);
		log.debug("headerAuth: {}", headerAuth);
		
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith(JwtSecurityConstants.TOKEN_PREFIX)) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}
    
    public boolean validateJwtToken(String token, UserDetails userDetail) {
    	try {
    		final String username = getUserNameFromJwtToken(token);
    		return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));
    	}catch (SignatureException e) {
			log.error("Invalid JWT Signature: {}", e.getMessage());
		}catch (MalformedJwtException e) {
			log.error("Invalid JWT Token: {}", e.getMessage());
		}catch (ExpiredJwtException e) {
			log.error("JWT Token is expired: {}", e.getMessage());
		}catch(UnsupportedJwtException e) {
			log.error("JWT Token is unsupported: {}", e.getMessage());
		}catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
    	return false;
    }
}
