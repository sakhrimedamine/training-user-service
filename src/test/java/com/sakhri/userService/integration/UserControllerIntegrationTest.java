package com.sakhri.userService.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakhri.userService.dto.ApiResponseDto;
import com.sakhri.userService.dto.CreateUserResponse;
import com.sakhri.userService.dto.CreateUserResquest;
import com.sakhri.userService.dto.UpdateUserResquest;
import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.service.UserService;


@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
class UserControllerIntegrationTest {

	@Autowired
	private UserService userService;

    private static MockMvc mvc;

    
    @BeforeAll
    public static void init(WebApplicationContext context) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
    
    @AfterEach
    public void cleanDB() {
    	userService.deleteAllUsers();
    }
    
	@Test
	void whenCreateUserWithValidDTOThenCreateItAndReturn201Status() throws Exception {
		// Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final String email = "user1@gmail.com";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		CreateUserResquest dto = CreateUserResquest.builder()
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();		
		

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dto);
    	
		// When
		MvcResult mvcResult= mvc.perform(post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(json)
							).andReturn();

		// then
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		
		CreateUserResponse myResponse = objectMapper.readValue(responseAsString, CreateUserResponse.class);
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
		assertEquals(firstName, myResponse.getFirstName());
		assertEquals(lastName, myResponse.getLastName());
		assertEquals(email, myResponse.getEmail());
	}
	
	@Test
	void whenCreateUserWithoutEmailThenReturn400Status() throws Exception {
		// Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		CreateUserResquest dto = CreateUserResquest.builder()
				.firstName(firstName)
				.lastName(lastName)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();		
		

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(dto);
    	

		// When
		MvcResult mvcResult= mvc.perform(post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(json)
							).andReturn();

		// then
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		
		ApiResponseDto myResponse = objectMapper.readValue(responseAsString, ApiResponseDto.class);
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
		
		assertThat(myResponse.getMessage()).contains("Email cannot be null");
	}

	
	@Test
	void whenUpdateExistingUserWithValidDTOThenUpdateIt() throws Exception {
		// Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final String email = "userUpdate@gmail.com";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		final short age2 = 20;
		final short height2 = 120;
		final short weight2 = 80;

		UserDto dto = UserDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();		
		
		final UserDto createUser = userService.createUser(dto);

		UpdateUserResquest updateUser = UpdateUserResquest.builder()
			.age(age2)
			.height(height2)
			.weight(weight2)
			.firstName(firstName)
			.lastName(lastName)
			.build();
		
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(updateUser);
    	

		// When
		MvcResult mvcResult= mvc.perform(put("/users/"+createUser.getUserId())
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(json)
							).andReturn();

		// then
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		
		Boolean myResponse = objectMapper.readValue(responseAsString, Boolean.class);
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);

		assertTrue(myResponse);
		
	}
	
	@Test
	void whenDeletingNonExistingUserWithValidDTOThenDeleteIt() throws Exception {
		// Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final String email = "userdelete@gmail.com";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		UserDto dto = UserDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();		
		
		final UserDto createUser = userService.createUser(dto);
    	

		// When
		MvcResult mvcResult= mvc.perform(delete("/users/"+createUser.getUserId())
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
							).andReturn();

		// then
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		
		Boolean myResponse = objectMapper.readValue(responseAsString, Boolean.class);
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);

		assertTrue(myResponse);
		
	}
	
	@Test
	void whenDeletingUserWithNonExistingIDThenReturn400Status() throws Exception {

		// When
		MvcResult mvcResult= mvc.perform(delete("/users/NO_EXISTING_ID")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
							).andReturn();

		// then	
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ApiResponseDto myResponse = objectMapper.readValue(responseAsString, ApiResponseDto.class);
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
		
		assertThat(myResponse.getMessage()).contains("User not found");	
	
	}
	
	
	@Test
	void whenFiltringUsersThenReturnCorrespondingUsers() throws Exception {
		// Given
		final String firstName1 = "firstName1";
		final String firstName2 = "firstName2";

		final String lastName1 = "lastname1";
		final String lastName2 = "lastname2";
		final String email1 = "user1@gmail.com";
		final String email2 = "user2@gmail.com";

		final short age = 22;
		final short height = 180;
		final short weight = 88;

		UserDto dto1 = UserDto.builder()
				.firstName(firstName1)
				.lastName(lastName1)
				.email(email1)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();
				
		UserDto dto2 = UserDto.builder()
				.firstName(firstName2)
				.lastName(lastName2)
				.email(email2)
				.password("abcdefghijklmn")
				.role("User")
				.height(height)
				.weight(weight)
				.age(age)
				.build();
		
		 userService.createUser(dto1);
		 userService.createUser(dto2);


		// When
		final String filters = "firstName=" + firstName1 + "&lastName=" + 
					"&userId=" + "&weight=" + "&height=" + "&age=";
		
		MvcResult mvcResult= mvc.perform(get("/users?"+filters)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
							).andReturn();

		// then
		String responseAsString = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		
		List<UserDto> myResponse = objectMapper.readValue(responseAsString,new TypeReference<List<UserDto>>(){});
	
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);

		assertEquals(1, myResponse.size());
		
		// When
		final String filters2 = "firstName=" + "&lastName=" + 
					"&userId=" + "&weight=" + "&height=" + "&age=" + age;
		
		MvcResult mvcResult2= mvc.perform(get("/users?"+filters2)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
							).andReturn();

		// then
		responseAsString = mvcResult2.getResponse().getContentAsString();
		
		
		myResponse = objectMapper.readValue(responseAsString,new TypeReference<List<UserDto>>(){});
	
		assertThat(mvcResult2.getResponse().getStatus()).isEqualTo(200);

		assertEquals(2, myResponse.size());
		
		// When
		final String filters3 = "firstName=" + firstName1 + "&lastName=" + lastName2 +
					"&userId=" + "&weight=" + "&height=" + "&age=" + age;
		
		MvcResult mvcResult3= mvc.perform(get("/users?"+filters3)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
							).andReturn();

		// then
		responseAsString = mvcResult3.getResponse().getContentAsString();
		
		
		myResponse = objectMapper.readValue(responseAsString,new TypeReference<List<UserDto>>(){});
	
		assertThat(mvcResult3.getResponse().getStatus()).isEqualTo(200);

		assertEquals(0, myResponse.size());
		
	}
}
