package com.attgroup.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.attgroup.app.controller.AuthenticationController;
import com.attgroup.app.payload.MessageResponse;
import com.attgroup.app.payload.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = {AuthenticationController.class})
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

	@Autowired
	MockMvc mockMvc;

	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsBytes(object);
	}

	public static <T> Object convertBytesToObject(byte[] object, Class<T> clazz) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(object, clazz);
	}

	@Test
	public void testSignup() throws Exception {
		//Test successful signup
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("myemail@email.com");
		signupRequest.setPassword("myPassword");
		signupRequest.setUsername("myUsername");

		MvcResult mvcResult = mockMvc
				.perform(post("/api/auth/signup")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).secure(true)
						.content(convertObjectToJsonBytes(signupRequest)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		assertNotNull(mvcResult.getResponse().getContentAsByteArray());
		MessageResponse messageResponse = (MessageResponse) convertBytesToObject(mvcResult.getResponse().getContentAsByteArray(), MessageResponse.class);
		assertEquals("User registration is successful!", messageResponse.getMessage());

		//Test unsuccessful signup
		signupRequest = new SignupRequest();
		signupRequest.setEmail("myemail@email.com");
		signupRequest.setPassword("myPassword");
		signupRequest.setUsername("myUsername");

		mvcResult = mockMvc
				.perform(post("/api/auth/signup")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).secure(true)
						.content(convertObjectToJsonBytes(signupRequest)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		assertNotNull(mvcResult.getResponse().getContentAsByteArray());
		messageResponse = (MessageResponse) convertBytesToObject(mvcResult.getResponse().getContentAsByteArray(), MessageResponse.class);
		assertEquals("Username is already taken!", messageResponse.getMessage());

	}
	
	@Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/welcome")).andExpect(status().isUnauthorized());
    }
}
