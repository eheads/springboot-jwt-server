package com.attgroup.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.attgroup.app.data.entity.User;
import com.attgroup.app.data.repository.UserRepository;
import com.attgroup.app.service.UserService;

@SpringBootTest
class AppApplicationTests {
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
		User expected = new User();
		expected.setEmail("test@email.com");
		expected.setPassword(encoder.encode("myPassword"));
		expected.setUsername("myUserName");
		userRepository.save(expected);
		
		Optional<User> optional = userRepository.findByUsername(expected.getUsername());
		assertTrue(optional.isPresent());
		
		User actual = optional.get();
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getPassword(), actual.getPassword());
		assertEquals(expected.getUsername(), actual.getUsername());
	}
}