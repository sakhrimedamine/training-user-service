package com.sakhri.userService.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakhri.userService.dto.ApiResponseDto;
import com.sakhri.userService.dto.JwtResponse;
import com.sakhri.userService.dto.LoginRequest;
import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.service.impl.UserServiceImpl;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
class JwtAuthenticationControllerIntegrationTest {

	@Autowired
	private UserServiceImpl userServiceImpl;

    private static MockMvc mvc;

    
    @BeforeAll
    public static void init(WebApplicationContext context) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
    
	@Test
	void whenCreateAuthenticationTokenWithValidCredentialsThenReturn200Status() throws Exception {
		// Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final String email = "user1@gmail.com";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		UserDto dto = UserDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.userId(UUID.randomUUID().toString())
				.email(email)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();		
		
		userServiceImpl.createUser(dto);

		LoginRequest loginRequest = new LoginRequest(email,"abcdefghijklmn");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(loginRequest);
    	

		// When
		MvcResult mvcResult= mvc.perform(post("/authenticate")
								.contentType(MediaType.APPLICATION_JSON)
								.content(json)
							).andReturn();

		// then
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); // com.fasterxml.jackson.databind.ObjectMapper
		
		JwtResponse myResponse = objectMapper.readValue(responseAsString, JwtResponse.class);
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
		assertEquals(firstName, myResponse.getFirstName());
		assertEquals(lastName, myResponse.getLastName());
		assertThat(myResponse.getAge()).isEqualTo(age);
		assertThat(myResponse.getHeight()).isEqualTo(height);
		assertThat(myResponse.getWeight()).isEqualTo(weight);
		assertThat(myResponse.getToken()).isNotNull();

	}
	
    
	@Test
	void whenCreateAuthenticationTokenWithInvalidCredentialsThenReturn401Status() throws Exception {
		// Given
		LoginRequest loginRequest = new LoginRequest("userjj1@gmail.com","abcdefghijklmn");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(loginRequest);
    	

		// When
		MvcResult mvcResult= mvc.perform(post("/authenticate")
								.contentType(MediaType.APPLICATION_JSON)
								.content(json)
							).andReturn();

		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); // com.fasterxml.jackson.databind.ObjectMapper
		
		ApiResponseDto myResponse = objectMapper.readValue(responseAsString, ApiResponseDto.class);

		// Then
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(401);
		assertEquals("INVALID_CREDENTIALS", myResponse.getMessage());

	}

}
